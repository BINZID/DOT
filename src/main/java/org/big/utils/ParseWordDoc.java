package org.big.utils;
import java.io.*;
import java.util.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
public class ParseWordDoc {
	public static void main(String[] args) throws Exception {
		String path = "E:/SCB/c.doc";
		FileInputStream in = new FileInputStream(new File(path));
		HWPFDocument doc = new HWPFDocument(in);
		int length = doc.characterLength();
		PicturesTable pTable = doc.getPicturesTable();
		// int TitleLength=doc.getSummaryInformation().getTitle().length();

		// System.out.println(TitleLength);
		// System.out.println(length);
		for (int i = 0; i < length; i++) {
			Range range = new Range(i, i + 1, doc);

			CharacterRun cr = range.getCharacterRun(0);
			if (pTable.hasPicture(cr)) {
				Picture pic = pTable.extractPicture(cr, false);
				String afileName = pic.suggestFullFileName();
				OutputStream out = new FileOutputStream(
						new File("E:\\SCB\\docImage\\" + UUID.randomUUID() + afileName));
				pic.writeImageContent(out);

			}
		}
	}
	
}




