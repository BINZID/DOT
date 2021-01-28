package org.big.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.*;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;

public class ParseWordHwpfUtils {
	public static void main(String[] args) throws IOException {
		//下面注释掉的是提取所有文字的
		FileInputStream in = new FileInputStream("E:/SCB/a.doc");
		WordExtractor extractor = new WordExtractor(in);
		
		String [] strArray = extractor.getParagraphText();
		String str = extractor.getText();
		System.out.println(str);
		for(int i = 0; i < strArray.length; ++i){
			System.out.println(strArray[i]);
		}

		//hwpfDocument是专门处理word的， 在poi中还有处理其他office文档的类
		HWPFDocument doc = new HWPFDocument(new FileInputStream("E:/SCB/a.doc"));

		//看看此文档有多少个段落
		Range range = doc.getRange();
		int numP = range.numParagraphs();
		System.out.println("number of Paragrah " + numP);

		long start2 = System.currentTimeMillis();

		//得到word的数据流
		byte[] dataStream = doc.getDataStream();

		System.out.println("size of dataStream " + dataStream.length);

		int numChar = range.numCharacterRuns();
		System.out.println("number of CharacterRun " + numChar);
		PicturesTable pTable = doc.getPicturesTable();
		for(int j = 0; j < numChar; ++j){
			CharacterRun cRun = range.getCharacterRun(j);
			//看看有没有图片
			boolean has = pTable.hasPicture(cRun);
			System.out.println("hasPicture " + has);
			if(has){
				Picture zhou = pTable.extractPicture(cRun, true);
				//目录你就自己设了，像保存什么的格式都可以
				zhou.writeImageContent(new FileOutputStream("E:/SCB/docImage" + j +".png"));
				System.out.println("extract Picture successfully! ");
				}
			}
		//时间花费，还是很快的
		long end2 = System.currentTimeMillis();
		System.out.println("time spend " + (end2 - start2));
	}
}



