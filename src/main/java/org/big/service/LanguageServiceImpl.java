package org.big.service;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class LanguageServiceImpl implements LanguageService {
	
	@Override
	public String findLanguageByCode(String code) {
		//加载json文件
		Resource resource = new ClassPathResource("static/json/language.json");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(resource.getFile());
			JSONArray languages=JSONArray.parseArray(IOUtils.toString(inputStream,"utf8"));
			for(int i=0;i<languages.size();i++){
				if(code.equals(languages.getJSONObject(i).get("id").toString()))
					return languages.getJSONObject(i).get("text").toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
    }

	@Override
	public String handleLanguageDropdown(String language) {
		String rsl = "";
		if (StringUtils.isNotBlank(language)) {
			switch (language) {
			case "1":
				rsl = "中文";
				break;
			case "2":
				rsl = "英文";
				break;
			case "3":
				rsl = "法文";
				break;
			case "4":
				rsl = "俄文";
				break;
			case "5":
				rsl = "西班牙文";
				break;
			default:
				rsl = "其他";
				break;
			}
		}
		return rsl;
	}

	@Override
	public String getValueofLanguage(String languages) {
		String rsl = null;
		if (StringUtils.isNotBlank(languages)) {
			switch (languages) {
			case "中文":
				rsl = "1";
				break;
			case "英文":
				rsl = "2";
				break;
			case "法文":
				rsl = "3";
				break;
			case "俄文":
				rsl = "4";
				break;
			case "西班牙文":
				rsl = "5";
				break;
			default:
				rsl = "6";
				break;
			}
		}
		return rsl;
	}
}
