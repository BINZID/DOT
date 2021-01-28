package org.big.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface NamesService {
	/**
     *<b>导出名称数据</b>
     *<p> 导出名称数据</p>
     * @author BINZI
	 * @param response
	 * @return
	 */
	void export(HttpServletResponse response) throws IOException;

}
