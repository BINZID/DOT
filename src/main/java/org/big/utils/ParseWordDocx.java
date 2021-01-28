package org.big.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
public class ParseWordDocx {
	public static void main(String[] args) {
		String path = "E:/SCB/昆虫a.docx";
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			// 获取Document对象
			XWPFDocument document = new XWPFDocument(fis);
			// 获取Extractor对象
			XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
			// 获取Word文本
			String wordText = xwpfWordExtractor.getText();
			
			String[] split = wordText.split("\n");
        	for (int i = 0; i < split.length; i++) {
        		String text = split[i].trim();
        		if (StringUtils.isNotBlank(text) && !text.contains("目") && !text.contains("科") && !text.contains("分类地位") 
						&& !text.contains("地理分布") && !text.contains("寄主：") && !text.contains("&&")) {
        			int index = indexOfFirstLetter(text);
        			if (-1 != index && text.substring(index).length() > 1 && !text.matches("^(a-zA-Z)")) {
        				System.out.println(text);
					}
        		}
        	}
        	
			List<XWPFPictureData> picList = document.getAllPictures();
			for (XWPFPictureData pic : picList) {
				/*System.out.println(pic.getPictureType() + file.separator + pic.suggestFileExtension() + file.separator
						+ pic.getFileName());*/
				/*byte[] bytev = pic.getData();
				FileOutputStream fos = new FileOutputStream("E:/SCB/docxImage/" + pic.getFileName());
				fos.write(bytev);*/
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Description 第一个英文字母的位置
	 * @param line
	 * @return
	 * @author ZXY
	 */
	public static int indexOfFirstLetter(String line) {
		int index = -1;
		for (int i = 0; i < line.length(); i++) {
			char charAt = line.charAt(i);
			if (String.valueOf(charAt).matches("[a-zA-Z]")) {
				return index + 1;
			}
			index++;
		}
		return index;
	}
	
	/**
	 * Species判断
	 * @param str
	 * @return
	 */
	public static boolean isSpecies(String str) {
		String regEx = "[\\u4E00-\\u9FA5（）\\s：]+[0-9]*[a-zA-Z]+[a-zA-Zéüè\\s().-]*";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}
}
