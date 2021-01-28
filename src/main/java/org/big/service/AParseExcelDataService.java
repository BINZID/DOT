package org.big.service;

import org.dom4j.DocumentException;

public interface AParseExcelDataService {
	/**
	 * <p><b>解析Excel，导入category数据</b></p>
	 * <p> 解析Excel，导入category数据</p>
	 * @author Bin
	 * @return
	 * @throws Exception
	 */
	void parseCategoryData(String path) throws Exception;

	String getPY(String cname)throws Exception;

	void parseProdbData(String path)throws Exception;

	void getEnnameOfDistribution(String path)throws Exception;

}
