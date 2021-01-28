package org.big.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import org.big.entity.License;

public interface LicenseService {
	/**
     *<b>License的select列表</b>
     *<p> 当前License下的检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询功能</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findLicenseList(HttpServletRequest request);
	
	/**
	 * <b>根据id查询License实体</b>
	 * <p> 根据id查询License实体</p>
	 * @param id
	 * @return
	 */
	License findOneById(String id);

	/**
     *<b>导出共享协议数据</b>
     *<p> 导出共享协议数据</p>
	 * @author BINZI
	 * @param response
	 * @return
	 */
	void export(HttpServletResponse response) throws IOException;
}
