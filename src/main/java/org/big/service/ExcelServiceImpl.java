package org.big.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static org.big.common.ValueJudger.isIntegerForDouble;

@Service
public class ExcelServiceImpl implements ExcelService {


	@Override
	public String getStringValueFromCell(Cell thisCell) {
		if(thisCell==null){
			return "";
		}else{
			String cellType=thisCell.getCellType().toString();
			if(cellType.equalsIgnoreCase("STRING")){
				return thisCell.getStringCellValue().trim();
			}else if(cellType.equalsIgnoreCase("NUMERIC")){
				String valueStr=String.valueOf(thisCell.getNumericCellValue());
				if(isIntegerForDouble(thisCell.getNumericCellValue())){
					return valueStr.substring(0,valueStr.indexOf(".")).trim();
				}
				else{
					return valueStr.trim();
				}
			}else if(cellType.equalsIgnoreCase("BOOLEAN")){
				return String.valueOf(thisCell.getBooleanCellValue()).trim();
			}else if(cellType.equalsIgnoreCase("Date")){
				return String.valueOf(thisCell.getDateCellValue()).trim();
			}else if(cellType.equals("RichTextString")){
				return String.valueOf(thisCell.getRichStringCellValue()).trim();
			}else if(cellType.equals("byte")){
				return String.valueOf(thisCell.getErrorCellValue()).trim();
			}else {
				return null;
			}
		}
	}

	@Override
	public Boolean judgeRowConsistent(int cellNum, String[] standardArray, Row thisRow) {
		if(thisRow.getPhysicalNumberOfCells() >= cellNum){
			for(int i=0;i<standardArray.length;i++){
				if(!thisRow.getCell(i).getStringCellValue().trim().contains(standardArray[i])){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}

	@Override
	public JSONArray uniqSerialNum(List<String> rowList) {
		JSONArray rows = new JSONArray();
		Set<String> set = new HashSet<String>();
		for (int i=0;i<rowList.size();i++) {
			JSONObject json = new JSONObject();
			if(!set.add(rowList.get(i))){
				if(rowList.get(i)!=""){
					json.put("rowNum", rowList.get(i));
					json.put("rowIndex", (i));
					rows.add(json);
				}
			}
		}
		return rows;
	}
		
	@Override
	public List<String> getRowList(Sheet sheet, Row row) {
		List<String> rowList = new ArrayList<>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
		    row = sheet.getRow(i);
		    if (null == sheet.getRow(i)) {
		        rowList.add("");
		        continue;
		    }else {
		        //将任何数据类型都转成字符串型
		        rowList.add(this.getStringValueFromCell(row.getCell(0)));
		    }
		}
		return rowList;
	}
}
