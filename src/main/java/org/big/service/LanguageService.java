package org.big.service;

public interface LanguageService {
	/**
     *<b>根据语言码查找描述</b>
     *<p> 根据语言码查找描述</p>
     * @author WangTianshan（王天山）
     * @return String
     */
	String findLanguageByCode(String code);
	
	/**
	 * <b>语言下拉选</b>
	 * <p> 语言下拉选</p>
	 * @author BINZI
	 * @param language
	 * @return
	 */
	String handleLanguageDropdown(String language);
	/**
	 * <b>语言下拉选的Value</b>
	 * <p> 语言下拉选的Value</p> 
	 * @param languages
	 * @return
	 */
	String getValueofLanguage(String languages);
}
