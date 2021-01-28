package org.big.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author BIN
 *
 */
public class BaiduTransUtils {
	private static final String APP_ID = "20200811000539945";
    private static final String SECURITY_KEY = "ZpwiMTC8ZR81d95VaZfR";

	public static void main(String[] args) throws InterruptedException {
		TransApi api = new TransApi(APP_ID, SECURITY_KEY);
		List<String> list = new ArrayList<>();
		list.add("苍山县");
		list.add("邢台地区");
		list.add("云和县");
		list.add("前郭尔罗斯蒙古族自治县");
		list.add("江东区");
		list.add("安国县");
		list.add("东坡区");
		list.add("原阳县");
		list.add("庄浪县");
		for (String query : list) {
			String result = api.getTransResult(query, "zh", "en");
			String arrStr = JSONObject.parseObject(result).getString("trans_result");
			JSONArray array = JSONArray.parseArray(arrStr);
			JSONObject jsonObj = array.getJSONObject(0);
			System.out.println(jsonObj.getString("dst"));
			Thread.sleep(1*1000L);
		}
	}
}
