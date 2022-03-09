package org.springblade.modules.out_buy_low.utils;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprVO;
import org.springblade.modules.process_low.excel.OutBuyQprExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/9 19:22
 * @Description:
 */
public class OutBuyQprExcelUtil {
	private static final String EXCEL_NAME = "外购件不良台账.xlsx";

	public static ExcelWriter export(List<OutBuyQprVO> lowVOList) {
		ExcelWriter writer = ExcelUtil.getWriter(true);
		setAlias(OutBuyQprExcel.class, writer);
		List<OutBuyQprExcel> collect = lowVOList.stream().map(OutBuyQprExcelUtil::convert).collect(Collectors.toList());
		writer.write(collect, true);
		return writer;
	}

	public static ExcelWriter export(List<OutBuyQprVO> lowVOList, HttpServletResponse response) {
		ExcelWriter export = export(lowVOList);

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/octet-stream");
		try {
			String fileName = URLEncoder.encode(EXCEL_NAME,"utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			export.flush(response.getOutputStream());
			export.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return export;
	}

	private static final Map<Integer, String> typeMap;
	private static final Map<Integer, String> levelMap;
	private static final Map<Integer, String> apparatusMap;
	private static final Map<Integer, String> triggerAddressMap;
	private static final Map<Integer, String> approveMap;
	private static final Map<Integer, String> bpmNodeMap;



	static {
		typeMap = new HashMap<>();
		typeMap.put(0, "外购件");
		typeMap.put(1, "内制件");
		typeMap.put(2, "其他");

		levelMap = new HashMap<>();
		levelMap.put(0, "R");
		levelMap.put(1, "S");
		levelMap.put(2, "A");
		levelMap.put(3, "B");
		levelMap.put(4, "C");
		levelMap.put(5, "批量");
		levelMap.put(6, "停线");

		apparatusMap = new HashMap<>();
		apparatusMap.put(0, "TNAG2.0");

		triggerAddressMap = new HashMap<>();
		triggerAddressMap.put(0, "TNGA#1");
		triggerAddressMap.put(1, "TNGA#2");
		triggerAddressMap.put(2, "TNGA#3");
		triggerAddressMap.put(3, "TNGA#4");
		triggerAddressMap.put(4, "TNGA#5");
		triggerAddressMap.put(5, "TNGA#6");

		approveMap = new HashMap<>();
		approveMap.put(0, "待审批");
		approveMap.put(1, "审批中");
		approveMap.put(2, "已结案");
		approveMap.put(3, "退回");
		approveMap.put(4, "自撤回");

		bpmNodeMap = new HashMap<>();
		bpmNodeMap.put(0, "不良联络书发行确认");
		bpmNodeMap.put(1, "不良联络书发行审批");
		bpmNodeMap.put(2, "不良调查");
		bpmNodeMap.put(3, "调查结果确认");
		bpmNodeMap.put(4, "调查结果审批");
	}

	private static OutBuyQprExcel convert(OutBuyQprVO lowVO) {
		OutBuyQprExcel copy = BeanUtil.copy(lowVO, OutBuyQprExcel.class);
		copy.setType(typeMap.get(lowVO.getType()));
		copy.setLevel(levelMap.get(Integer.parseInt(lowVO.getLevel() + "")));
		copy.setApparatusType(apparatusMap.get(lowVO.getApparatusType()));
		copy.setTriggerAddress(triggerAddressMap.get(Integer.parseInt(lowVO.getTriggerAddress() +"")));
		copy.setBpmStatus(approveMap.get(lowVO.getBpmStatus()));
		copy.setBpmNode(bpmNodeMap.get(lowVO.getBpmNode()));
		copy.setReleaseTime(CommonUtil.dateParseString(lowVO.getCreateTime()));
		copy.setFindTime(CommonUtil.dateParseString(lowVO.getFindTime()));
		if (lowVO.getImgReportFiles() != null) {
			copy.setImgReportList(StringUtil.join(lowVO.getImgReportFiles().stream().map(BusFileVO::getUrl).collect(Collectors.toList())));
		}
		copy.setCheckReplyTime(CommonUtil.dateParseString(lowVO.getCheckReplyTime()));
		copy.setCompleteTime(CommonUtil.dateParseString(lowVO.getCompleteTime()));
		copy.setCreateTime(CommonUtil.dateParseString(lowVO.getCreateTime()));
		return copy;
	}

	private static void setAlias(Class clazz, ExcelWriter export) {
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field declaredField : declaredFields) {
			ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
			if (annotation == null) {
				continue;
			}
			export.addHeaderAlias(declaredField.getName(), annotation.value());
		}
	}
}
