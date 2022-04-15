package org.springblade.modules.out_buy_low.utils;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.PdfPageRotateAngle;
import com.spire.pdf.PdfPageSize;
import com.spire.pdf.graphics.PdfCanvas;
import com.spire.pdf.graphics.PdfImage;
import com.spire.pdf.graphics.PdfMargins;
import com.spire.pdf.graphics.PdfTemplate;
import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.common.utils.CenterFileUtil;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprVO;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhouxiaofeng
 * @Date: 2022/3/15 16:43
 * @Description:
 */
public class QprDownLoadUtils {

    private static final String TEMPLATE_PATH = CenterFileUtil.getFilePath("qpr_template.xlsx");

    private static final Logger logger =	LoggerFactory.getLogger(QprDownLoadUtils.class);

    private static PdfDocument convertPdf(PdfDocument pdfDocument) {
		PdfDocument newPdf = new PdfDocument();
		for (int i = 0; i < pdfDocument.getPages().getCount(); i++) {
			PdfPageBase newPage = newPdf.getPages().add(PdfPageSize.A4, new PdfMargins(-20, -20, -20, -20));

			//将原PDF内容写入新页面
			pdfDocument.getPages().get(i).createTemplate().draw(newPage, new Point(0, 0));
		}
		newPdf.getViewerPreferences().setFitWindow(true);
		return newPdf;
	}

    private static PdfDocument excelToPdf(Workbook wb) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		logger.info("excel写入byte");
		wb.saveToStream(outputStream, FileFormat.PDF);

		logger.info("byte转化为pdf");
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.loadFromBytes(outputStream.toByteArray());
		return pdfDocument;
	}

	private static void setImage(PdfDocument pdfDocument, InputStream inputStream) throws FileNotFoundException {
    	if (inputStream == null) {
    		return;
		}
		PdfPageBase page = pdfDocument.getPages().get(0);
		PdfCanvas canvas = page.getCanvas();
		if (PdfPageRotateAngle.Rotate_Angle_180.equals(page.getRotation())) {
			canvas.rotateTransform(180);
			canvas.translateTransform(0 - page.getActualSize().getWidth(), 0 - page.getActualSize().getHeight());
		}
		// 加载印章图片
		PdfImage image = PdfImage.fromStream(inputStream);

		// 创建 PdfTemplate 对象
		PdfTemplate template = new PdfTemplate(213, 139);
		// 将图片绘制到模板
		template.getGraphics().drawImage(image, 0, 0, 213, 139);

		canvas.drawImage(image, 51, 232, 213, 139);
	}

    /**
     * 下载qpr
     * @param lowApproval
     * @param response
     */
    public static void downLoad(OutBuyQprVO lowApproval, HttpServletResponse response) throws IOException {

    	// 判断模板是否存在
		File file = new File(TEMPLATE_PATH);
		if (!file.exists()) {
			throw new ServiceException("模板不存在");
		}
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        try {
            String fileName = URLEncoder.encode("QPR.pdf","utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			handler(lowApproval, response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat dateSeparateFormat = new SimpleDateFormat("yyyy/M/d");
	private static final Map<Integer, String> levelMap;
	private static final Map<Integer, String> triggerAddressMap;

	static {
		levelMap = new HashMap<>();
		levelMap.put(0, "R");
		levelMap.put(1, "S");
		levelMap.put(2, "A");
		levelMap.put(3, "B");
		levelMap.put(4, "C");
		levelMap.put(5, "批量");
		levelMap.put(6, "停线");

		triggerAddressMap = new HashMap<>();
		triggerAddressMap.put(0, "TNGA#1");
		triggerAddressMap.put(1, "TNGA#2");
		triggerAddressMap.put(2, "TNGA#3");
		triggerAddressMap.put(3, "TNGA#4");
		triggerAddressMap.put(4, "TNGA#5");
		triggerAddressMap.put(5, "TNGA#6");
	}

    private static void handler(OutBuyQprVO lowApproval, HttpServletResponse response) throws IOException {
		logger.info("开始下载文件");
		Date date = new Date();
		// 加载Excel文档.
		Workbook wb = new Workbook();

		logger.info("获取模板路径:{}", TEMPLATE_PATH);
		wb.loadFromFile(TEMPLATE_PATH);
		logger.info("excel加载完成: {}", TEMPLATE_PATH);
		wb.replace("${providerName}", lowApproval.getDutyDeptName());
		wb.replace("${timeCode}", dateFormat.format(date));
		wb.replace("${partCode}", lowApproval.getDesignation());
		wb.replace("${partName}", lowApproval.getName());
		wb.replace("${apparatusType}", "TNGA#1");
		wb.replace("${time}", dateSeparateFormat.format(date));
		wb.replace("${findQuantity}", lowApproval.getFindQuantity() + "个");
		wb.replace("${level}", levelMap.get(new Integer(lowApproval.getLevel() + "")));
		wb.replace("${triggerAddress}", triggerAddressMap.get(new Integer(lowApproval.getTriggerAddress() + "")));
		wb.replace("${eventRemark}", lowApproval.getEventRemark());
		wb.replace("${pleaseContent}", lowApproval.getPleaseContent());
		wb.replace("${dispose}", getDispose(lowApproval));

		logger.info("excel转pdf开始");
		PdfDocument pdfDocument = excelToPdf(wb);
		logger.info("excel转pdf结束");

		logger.info("设置图片开始");
		setImage(pdfDocument, getImgInputStream(lowApproval));
		logger.info("设置图片结束");

		//创建一个新的PdfDocument实例

		logger.info("转化新的pdf开始");
		PdfDocument newPdf = convertPdf(pdfDocument);
		logger.info("转化新的pdf结束");
		newPdf.saveToStream(response.getOutputStream());
		logger.info("文件下载完成");
	}

	private static InputStream getImgInputStream(OutBuyQprVO outBuyQprVO){
		List<BusFileVO> imgReportFiles = outBuyQprVO.getImgReportFiles();
		BusFileVO busFileVO = imgReportFiles.get(0);
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(busFileVO.getUrl()).openConnection();
			connection.setReadTimeout(5000);
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				return inputStream;
			}
		} catch (IOException e) {
			return null;
		}
		return null;
	}

	private static final String dispostTemplate = "${0}返还(Return)[ ${return} 个(Piece(s)]　${1}保留(Hold)　${2}废弃(Disposal)　${3}其他(Others)(  ${Others}  )";

	private static String getDispose(OutBuyQprVO lowApproval) {
		HashMap<String, String> map = new HashMap<>();
		map.put("0", "□");
		map.put("1", "□");
		map.put("2", "□");
		map.put("3", "□");
		map.put("return", "  ");
		map.put("Others", "  ");
		map.put(lowApproval.getDispostType() + "", "■");

		if (lowApproval.getDispostType() == 0) {
			map.put("return", lowApproval.getDispost());
		}
		if (lowApproval.getDispostType() == 3) {
			map.put("Others", lowApproval.getDispost());
		}
		return CommonUtil.placeHolderReplace(dispostTemplate, map);
	}
}
