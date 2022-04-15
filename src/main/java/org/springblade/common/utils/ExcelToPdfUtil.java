package org.springblade.common.utils;

import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;

import java.io.InputStream;
import java.io.OutputStream;

public class ExcelToPdfUtil {

    public static void main(String[] args) throws Exception {
        excelToPdf("C:\\Users\\wai\\Desktop\\岗位表.xlsx", "C:\\Users\\wai\\Desktop\\岗位表.pdf");
    }

    /**
     * Excel文件转Pdf.
     * @throws Exception .
     */
    public static void excelToPdf(String path, OutputStream out) throws Exception {
        // 加载Excel文档.
        Workbook wb = new Workbook();
        wb.loadFromFile(path);
		// 调用方法保存为PDF格式.
		wb.saveToStream(out, FileFormat.PDF);
    }

    /**
     * Excel文件转Pdf.
     * @throws Exception .
     */
    public static void excelToPdf(InputStream in, OutputStream out) throws Exception {
        // 加载Excel文档.
        Workbook wb = new Workbook();
        wb.loadFromStream(in);
        // 调用方法保存为PDF格式.
        wb.saveToStream(out, FileFormat.PDF);
    }

    /**
     * Excel文件转Pdf.
     * @param excelPath Excel文件路径.
     * @param pdfPath Pdf文件路径.
     * @throws Exception .
     */
    public static void excelToPdf(String excelPath, String pdfPath) throws Exception {
        // 加载Excel文档.
        Workbook wb = new Workbook();
        wb.loadFromFile(excelPath);
        // 调用方法保存为PDF格式.
        wb.saveToFile(pdfPath, FileFormat.PDF);
    }

    /**
     * Excel文件转Pdf.
     * @param excelPath Excel文件路径.
     * @param pdfPath Pdf文件路径.
     * @param sheetIndex sheet页序号.
     * @throws Exception .
     */
    public static void excelToPdf(String excelPath, String pdfPath, int sheetIndex) throws Exception {
        // 加载Excel文档.
        Workbook wb = new Workbook();
        wb.loadFromFile(excelPath);

        Worksheet sheet = wb.getWorksheets().get(sheetIndex);
        // 调用方法保存为PDF格式.
        wb.saveToFile(pdfPath, FileFormat.PDF);
    }

}
