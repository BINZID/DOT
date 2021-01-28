package org.big.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;

import com.alibaba.fastjson.JSON;

public interface AParseUtilsService {
	/**
	 * 解析入侵动物参考文献
	 * @param path
	 */
	void parseRQDWRef(String path) throws Exception;
	/**
	 * 解析入侵动物分类单元
	 * @param path
	 * @throws Exception
	 */
	void parseRQDWTaxon(String path) throws Exception;
	
	/**
	 * 解析入侵动物多媒体
	 * @param path
	 * @throws Exception
	 */
	void parseRQDWMedia(String path) throws Exception;
	
	/**
	 * 上传入侵动物图片
	 * @param path
	 */
	void uploadRQDWImg(HttpServletRequest request, String path, String uploadPath) throws Exception;
	/**
	 * 解析入侵动物其他信息
	 * @param path
	 * @throws Exception
	 */
	void parseRQDWInfo(String path) throws Exception;
	/**
	 * 补充入侵动物描述数据的参考文献
	 */
	void getRQDWRefOfDesc();
	/**
	 * 补充入侵动物描述数据的分布数据
	 */
	void getRQDWDisOfDesc();
	/**
	 * 更新参考文献
	 * @param path
	 */
	void updateRef(String path) throws IOException;
	
	/**
	 * 俗名拼音
	 */
	void commonnameOfPY() throws DocumentException ;
	/**
	 * 解析入侵植物的分类单元
	 * @param path
	 */
	void parseRQZWTaxon(String path) throws Exception;
	/**
     * 解析入侵植物分类单元2
     * @return
     * @throws Exception
     */
	void parseRQZW2Taxon(String path) throws Exception;
	
	/**
     * 解析入侵植物分类单元命名信息
     * @return
     * @throws Exception
     */
	void parseRQZWTaxonInfo(String path) throws Exception;
	/**
     * 解析入侵植物引证信息
     * @return
     * @throws Exception
     */
	void parseRQZWCitation(String path) throws Exception;
	/**
	 * 解析入侵植物俗名信息
	 * @return
	 * @throws Exception
	 */
	void parseRQZWCommonname(String path) throws Exception;
	/**
	 * 解析入侵植物描述信息
	 * @return
	 * @throws Exception
	 */
	void parseRQZWDesc(String path) throws Exception;
	/**
	 * 解析入侵植物多媒体信息
	 * @return
	 * @throws Exception
	 */
	void parseRQZWMultimedia(String path) throws Exception;
	/**
	 * 解析入侵植物分布数据
	 * @return
	 * @throws Exception
	 */
	void parseRQZWDescdata(String path) throws Exception;
	/**
	 * 入侵植物图片
	 * @param request
	 * @param path
	 * @param string
	 */
	void uploadRQZWImg(HttpServletRequest request, String path, String string);
    /**
     * 入侵植物入侵分布图片
     * @param request
     * @return
     * @throws Exception
     */
	void uploadRQZWDiscImg(HttpServletRequest request, String path, String string);
	/**
	 * 解析入侵植物描述
	 * @param path
	 * @throws Exception
	 */
	void parseRQZWDescdis(String path) throws Exception;
	void handleRQDW(String path) throws Exception;
	void synchdataToLocal(String path) throws Exception;
	void trainAnimalData(String path) throws Exception;
	void taxKeyTrim(String path)throws Exception;
	void compareDataTrim(String path)throws Exception;
	void search(String path)throws Exception;
	void updatePisLatin(String path)throws Exception;
	void handleYD(String path)throws Exception;
	JSON countCitation();
	JSON handleCitation();
	JSON citationMatchRef();
	JSON contionsMatchRef(String path) throws Exception;
	JSON parseExcelRef(String path) throws Exception;
	JSON parseExcelTaxon(String path) throws Exception;
	JSON parseExcelCitation(String path) throws Exception;
	JSON parseExcelDescription(String path) throws Exception;
	JSON parseExcelCommonname(String path) throws Exception;
	JSON parseExcelDistributiondate(String path) throws Exception;
	JSON parseExcelTraitdata(String path) throws Exception;
	JSON parseExcelMultimedia(String path) throws Exception;
	JSON citationMatchRefYD() throws Exception;
	JSON citationMatchRefYDPageNum() throws Exception;
	JSON parseExcelCitationOfRef(String path) throws Exception;
	JSON matchCitationOfRefIsNull() throws Exception;
	JSON unParseExcelCitationOfRef(String path) throws Exception;
	JSON reUnParseExcelCitationOfRef() throws Exception;
	JSON parseProtect(String path)throws Exception;
	JSON addDistributionPid(String path)throws Exception;
	JSON compareTo2020() throws Exception;
	JSON addPpid() throws Exception;
	JSON match() throws Exception;
	JSON distinct(String path) throws Exception;
	JSON getDistributData();
	JSON addProvinceAdcode(String path) throws Exception;
	JSON getLevel(String path) throws Exception;
	JSON getLevelPlus(String path) throws Exception;
	JSON addCountyOfCity(String path) throws Exception;
	JSON matchAddessRef(String path) throws Exception;
	JSON mergeDesc();
	void setRQUUID();
	void getTaxonData();
	void getRQMedia(String path, HttpServletRequest request, String uploadPath) throws Exception;
	void parseRQZWDist(String path) throws Exception;
	void parseRQDWRefInfo();
	String mergeRuqinData(String[] taxasetsP);
}
