package org.big.service;

import java.util.List;

import org.big.entity.UserDetail;
import org.big.repository.BaseinfotmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class BaseinfotmpServiceImpl implements BaseinfotmpService {
	@Autowired
	private BaseinfotmpRepository baseinfotmpRepository;

	@Override
	public String findDsIdByFilemarkAndSerialNumAndFileType(String dsFileMark, String sourcesid, Integer fileType) {
		return this.baseinfotmpRepository.findDsIdByFilemarkAndSerialNumAndFileType(dsFileMark, sourcesid, fileType);
	}

	@Override
	public List<String> findRefIdByFilemarkAndSerialNumAndFileType(String refFileMark, Integer fileType, String[] refStr) {
		return this.baseinfotmpRepository.findRefIdByFilemarkAndSerialNumAndFileType(refFileMark, fileType, refStr);
	}

	@Override
	public String findExpIdByFilemarkAndSerialNumAndFileType(String expFileMark, Integer fileType, String expert) {
		return this.baseinfotmpRepository.findExpIdByFilemarkAndSerialNumAndFileType(expFileMark, fileType, expert);
	}

	@Override
	public JSON clearBaseinfotmpData() {
		JSONObject thisResult = new JSONObject();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.baseinfotmpRepository.deleteAllByUid(thisUser.getId());
		thisResult.put("rsl", true);
		return thisResult;
	}
}
