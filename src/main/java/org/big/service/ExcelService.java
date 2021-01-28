package org.big.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.alibaba.fastjson.JSONArray;

/**
 *<p><b>处理Excel的Service类接口</b></p>
 *<p> 处理Excel的Service类接口，包括处理Excel的通用方法</p>
 * @author WangTianshan
 *<p>Created date: 2018/11/21 23:28</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface ExcelService {

	/**
     *<b>将所有类型的表格单元都转化成字符串型</b>
     *<p> 将所有类型的表格单元都转化成字符串型</p>
     * @author WangTianshan
     * @param thisCell 输入的单元
     * @return
     */
    String getStringValueFromCell(Cell thisCell);

    /**
     *<b>比对指定行的前n列与数组内容是否完全一致</b>
     *<p> 比对指定行的前n列与数组内容是否完全一致</p>
     * @author WangTianshan
     * @param cellNum 输入的行
     * @param standardArray 输入的行
     * @param thisRow 输入的行
     * @return
     */
    Boolean judgeRowConsistent(int cellNum, String[] standardArray, Row thisRow);

	/**
	 * <b>判断上传Excel文件序号是否重复</b>
	 * <p> 判断上传Excel文件序号是否重复</p>
	 * @author BINZI
	 * @param rowList	文件数据
	 * @return
	 */
	JSONArray uniqSerialNum(List<String> rowList);
	
	/**
	 * <b>获取上传文件的序号集合，用来判断序号是否重复</b>
	 * <p> 获取上传文件的序号集合，用来判断序号是否重复</p>
	 * @author BINZI
	 * @param sheet
	 * @param rowNum
	 * @param row
	 * @return
	 */
	List<String> getRowList(Sheet sheet, Row row);
}