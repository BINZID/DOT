package org.big.service;

public interface AmphibianAndReptiliaService {
	/**
	 * 两栖、爬行 物种
	 * @param path
	 */
	void parseARTaxon(String path);

	/**
	 * 两栖、爬行 描述
	 * @param path
	 */
	void parseARDescription(String path);

	/**
	 * 两栖、爬行 分布
	 * @param path
	 */
	void parseARDist(String path);

}
