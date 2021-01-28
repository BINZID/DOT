package org.big.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.big.common.ExcelUtil;
import org.big.repository.NamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class NamesServiceImpl implements NamesService {
	@Autowired
	private NamesRepository namesRepository;

	@SuppressWarnings("resource")
	@Override
	public void export(HttpServletResponse response) throws IOException {
		String[] columnName = {"拉丁名", "中文名", "命名信息", "数据源"};

		List<Object[]> thisList = this.namesRepository.findAllByTeamId();
		
		/*HSSFWorkbook wb = new HSSFWorkbook();*/
		SXSSFWorkbook wb = new SXSSFWorkbook();
		long dataIndex = 0;
		SXSSFSheet sheet = wb.createSheet("名称数据");
		/*HSSFSheet sheet = wb.createSheet("名称数据");*/
		
		sheet.setColumnWidth(0, 12 * 256);
		sheet.setColumnWidth(1, 12 * 256);
		sheet.setColumnWidth(2, 15 * 256);
		sheet.setColumnWidth(3, 18 * 256);
		SXSSFRow title = sheet.createRow(0);
		/*HSSFRow title = sheet.createRow(0);*/
		title.setHeightInPoints(20);
		for (int i = 0; i < columnName.length; i++) {
			/*HSSFCell cell = title.createCell(i);*/
			SXSSFCell cell = title.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}
		
		try {
			for (int i = 0; i < thisList.size(); i++) {
				SXSSFRow rows = sheet.createRow(i + 1);
				Object[] obj = thisList.get(i);
				rows.createCell(0).setCellValue((null == obj[0]) ? null: obj[0].toString());
				rows.createCell(1).setCellValue((null == obj[1]) ? null: obj[1].toString());
				rows.createCell(2).setCellValue((null == obj[2]) ? null: obj[2].toString());
				rows.createCell(3).setCellValue((null == obj[3]) ? null: obj[3].toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.setHeader("content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("名称数据".getBytes("gb2312"), "iso8859-1") + ".xlsx");
		OutputStream out = response.getOutputStream();
		try {
			wb.write(out);
		} catch (Exception e) {
		} finally {
			out.close();
		}
	}

}
