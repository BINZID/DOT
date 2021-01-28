package org.big.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

public class ParseWordXwpfUtils {
	public static void main(String[] args) {
		readDocxImage("E:\\SCB\\a.docx", "E:\\SCB\\image");
	}
	/**
	 * 读取srcFile源word文件docx文字
	 * 读取srcFile源word文件docx中的image图片并且存放在文件夹imageFile中
	 * @param srcFile
	 * @param imageFile
	 * @return
	 */
	public static String readDocxImage(String srcFile, String imageFile) {
		String path = srcFile;
		File file = new File(path);
		try {
			// 用XWPFWordExtractor来获取文字
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument document = new XWPFDocument(fis);
			XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
			String text = xwpfWordExtractor.getText();
			// System.out.println(text);
			// 将获取到的文字存放到对应文件名中的txt文件中
			String temp[] = srcFile.split("\\/");
			String temp1 = temp[temp.length - 1];
			String temp3[] = temp1.split("\\.");
			/*String txtFileName = imageFile + temp3[0] + ".txt";
			PrintStream ps = new PrintStream(txtFileName);
			ps.println(text);*/

			// 用XWPFDocument的getAllPictures来获取所有的图片
			List<XWPFPictureData> picList = document.getAllPictures();
			for (XWPFPictureData pic : picList) {
				// System.out.println(pic.getPictureType() + file.separator +
				// pic.suggestFileExtension() + file.separator
				// + pic.getFileName());
				byte[] bytev = pic.getData();
				// System.out.println(bytev.length);
				// 大于1000bites的图片我们才弄下来，消除word中莫名的小图片的影响
				if (bytev.length > 300) {
					FileOutputStream fos = new FileOutputStream(imageFile + pic.getFileName());
					fos.write(bytev);
				}
			}
			fis.close();
			return text;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
}



