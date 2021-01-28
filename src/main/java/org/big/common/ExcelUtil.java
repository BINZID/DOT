package org.big.common;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * <b>Excel导入导出工具类</b>
 * <p> Excel导入导出工具类</p>
 * @author BINZI
 */
public class ExcelUtil {
	
	public ExcelUtil() {
	}

	/**
	 * <b>表头单元格样式 以及表头的字体样式</b>
	 * <p> 表头单元格样式 以及表头的字体样式</p>
	 * @author BINZI
	 * @param wb
	 * @return
	 */
	public static CellStyle setTitleStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(VerticalAlignment.CENTER);	// 上下居中
		style.setAlignment(HorizontalAlignment.CENTER);			// 左右居中
		Font headerFont = wb.createFont();						// 创建字体样式
		headerFont.setBold(true); 								// 字体加粗
		style.setFont(headerFont);								// 为标题样式设置字体样式
		return style;
	}

	/**
	 * <b>为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中</b>
	 * <p> 为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中</p>
	 * @author BINZI
	 * @param wb
	 * @return
	 */
	public static CellStyle setContentStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setWrapText(true);								// 设置自动换行
		style.setVerticalAlignment(VerticalAlignment.CENTER);	// 创建一个上下居中格式
		style.setAlignment(HorizontalAlignment.CENTER);			// 左右居中
		return style;
	}
	
	/**
	 * <b>根据后缀名创建对应Excel版本</b>
	 * <p>根据后缀名创建对应Excel版本</p>
	 * @param file
	 * @return
	 */
	public static  Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith("xls")){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith("xlsx")){
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
        }
        return workbook;
    }
}
