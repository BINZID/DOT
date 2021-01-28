package org.big.service;

import java.util.List;

import com.alibaba.fastjson.JSON;

public interface BaseinfotmpService {
	/**
	 * <b>通过文件标记、文件类型，序号查询对应的数据源的Id</b>
	 * <p> 通过文件标记、文件类型，序号查询对应的数据源的Id</p>
	 * @author BINZI
	 * @param dsFileMark
	 * @param serialNum
	 * @param fileType
	 * @return
	 */
	String findDsIdByFilemarkAndSerialNumAndFileType(String dsFileMark, String serialNum, Integer fileType);
	
	/**
	 * <b>通过文件标记、文件类型，序号查询对应的参考文献的Id</b>
	 * <p> 通过文件标记、文件类型，序号查询对应的参考文献的Id</p>
	 * @author BINZI
	 * @param refFileMark
	 * @param fileType
	 * @param serialNum
	 * @return
	 */
	List<String> findRefIdByFilemarkAndSerialNumAndFileType(String refFileMark, Integer fileType, String[] refStr);

	/**
	 * <b>通过文件标记、文件类型，序号查询对应的专家的Id</b>
	 * <p> 通过文件标记、文件类型，序号查询对应的专家的Id</p>
	 * @author BINZI
	 * @param expFileMark
	 * @param fileType
	 * @param serialNum
	 * @return
	 */
	String findExpIdByFilemarkAndSerialNumAndFileType(String expFileMark, Integer fileType, String serialNum);
	/**
	 * <b>清空临时表数据</b>
	 * <p> 清空临时表数据</p>
	 * @author BINZI
	 */
	JSON clearBaseinfotmpData();
	
}
