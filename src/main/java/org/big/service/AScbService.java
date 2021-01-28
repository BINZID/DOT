package org.big.service;

import com.alibaba.fastjson.JSON;

public interface AScbService {

	JSON confirmSCB(String path) throws Exception;

	JSON shandongPis(String path) throws Exception;

	JSON parseShandongPis(String path) throws Exception;

	JSON wordTransformExcel(String path) throws Exception;
	
	JSON matchPis(String path1, String path2) throws Exception;
	
	JSON checkPis() throws Exception;

	JSON getFinalData(String path1, String path2) throws Exception;

	JSON trimData(String path) throws Exception;

	JSON getData() throws Exception;

	JSON rename(String path1, String path2) throws Exception;

	JSON matchRank(String path) throws Exception;

	JSON mergeData(String path) throws Exception;

	JSON getCustoms(String path) throws Exception;
}
