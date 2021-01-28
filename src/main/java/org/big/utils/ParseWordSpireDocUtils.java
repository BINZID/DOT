package org.big.utils;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.spire.doc.*;
import com.spire.doc.documents.DocumentObjectType;
import com.spire.doc.fields.DocPicture;
import com.spire.doc.interfaces.ICompositeObject;
import com.spire.doc.interfaces.IDocumentObject;

public class ParseWordSpireDocUtils {
	public static void main(String[] args) throws IOException {
		//ExtractText();
		ExtractImg();
	}
	/**
	 * 提取Word文字
	 * @throws IOException
	 */
	public static void ExtractText() throws IOException {
		Document doc = new Document();
        doc.loadFromFile("E:\\SCB\\a.docx");
		// 获取文本保存为String
		String text = doc.getText();
		// 将String写入Txt
        writeStringToTxt(text,"提取文本.txt");
	}
	
	public static void writeStringToTxt(String content, String txtFileName) throws IOException {
        FileWriter fWriter= new FileWriter(txtFileName, true);
        try {
        	String[] split = content.split("\n");
        	StringBuilder sb = new StringBuilder();
        	for (int i = 0; i < split.length; i++) {
        		String text = split[i].trim();
        		if (StringUtils.isNotBlank(text) && !text.contains("目") && !text.contains("科") && !text.contains("分类地位") 
						&& !text.contains("地理分布") && !text.contains("寄主：")) {
        			int index = indexOfFirstLetter(text);
        			if (-1 != index && text.substring(index).length() > 1) {
						sb.append("&");
						sb.append(text);
						/*sb.append("#");*/
					/*}else {
						sb.append("  ");
						sb.append(text);*/
					}
        		}
        	}
        	List<String> list = handleSpeciesAndPngNames(sb.toString());
        	for (String pngName : list) {
				System.out.println(pngName);
			}
            fWriter.write(content);
        }catch(IOException ex){
            ex.printStackTrace();
        }finally{
            try{
                fWriter.flush();
                fWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

	/**
	 * 提取图片
	 * @throws IOException 
	 */
	public static void ExtractImg() throws IOException {
		//加载Word文档
        Document document = new Document();
        document.loadFromFile("E:/SCB/原核生物及病毒类.doc");
 
        //创建Queue对象
        Queue nodes = new LinkedList();
        nodes.add(document);
 
        //创建List对象
        List images = new ArrayList();
 
        //遍历文档中的子对象
        while (nodes.size() > 0) {
            ICompositeObject node = (ICompositeObject) nodes.poll();
            for (int i = 0; i < node.getChildObjects().getCount(); i++) {
                IDocumentObject child = node.getChildObjects().get(i);
                if (child instanceof ICompositeObject) {
                    nodes.add((ICompositeObject) child);
                    //获取图片并添加到List
                    if (child.getDocumentObjectType() == DocumentObjectType.Picture) {
                        DocPicture picture = (DocPicture) child;
                        images.add(picture.getImage());
                    }
                }
            }
        }
 
        //将图片保存为PNG格式文件
        for (int i = 0; i < images.size(); i++) {
            File file = new File(String.format("-%d.jpg", i));
            ImageIO.write((RenderedImage) images.get(i), "JPG", file);
        }
    }
	
	/**
	 * 拉丁名、图片名拼接处理
	 * @param string
	 */
	private static List<String> handleSpeciesAndPngNames(String line) {
		List<String> list = new ArrayList<>();
		line = line.substring(1);
		String[] split = line.split("&");
		for (String info : split) {
			/* 图片名称处理
			String[] speciesAndAuthorAndPngName = info.split("#");
			String speciesAndAuthor = speciesAndAuthorAndPngName[0];
			String pngNames = speciesAndAuthorAndPngName[1].trim().replaceAll("( )+", " ").trim();
			String[] pngName = pngNames.split(" ");
			for (String imgName : pngName) {
				list.add(speciesAndAuthor + "-" + imgName);
			}*/
			list.add(info);
		}
		return list;
	}
	/**
	 * 中文判断
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		String regEx = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		} else {
			return false;
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
}



