package org.big.zoology.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Taxon
 * @Description: 实体类
 * @Author NY
 * @Date: 2019/10/18 9:49
 * @return
 * @Version V1.0
 */
public class Taxon implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String scientificname;

	private String chinesename;

	private String pinyin;

	private String type;

	private String assort;

	private String authorship;

	private String epithet;

	private String rankId;

	private String nomencode;

	private String tcid;

	private String refclassification;

	private String sourcesId;

	private String referencejson;

	private String specialistId;

	private String remark;

	private String ddbaseId;

	private Integer status;

	private Integer browse;

	private String inputer;

	private Date inputtime;

	private Date synchdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScientificname() {
		return scientificname;
	}

	public void setScientificname(String scientificname) {
		this.scientificname = scientificname;
	}

	public String getChinesename() {
		return chinesename;
	}

	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAssort() {
		return assort;
	}

	public void setAssort(String assort) {
		this.assort = assort;
	}

	public String getAuthorship() {
		return authorship;
	}

	public void setAuthorship(String authorship) {
		this.authorship = authorship;
	}

	public String getEpithet() {
		return epithet;
	}

	public void setEpithet(String epithet) {
		this.epithet = epithet;
	}

	public String getRankId() {
		return rankId;
	}

	public void setRankId(String rankId) {
		this.rankId = rankId;
	}

	public String getNomencode() {
		return nomencode;
	}

	public void setNomencode(String nomencode) {
		this.nomencode = nomencode;
	}

	public String getTcid() {
		return tcid;
	}

	public void setTcid(String tcid) {
		this.tcid = tcid;
	}

	public String getRefclassification() {
		return refclassification;
	}

	public void setRefclassification(String refclassification) {
		this.refclassification = refclassification;
	}

	public String getSourcesId() {
		return sourcesId;
	}

	public void setSourcesId(String sourcesId) {
		this.sourcesId = sourcesId;
	}

	public String getReferencejson() {
		return referencejson;
	}

	public void setReferencejson(String referencejson) {
		this.referencejson = referencejson;
	}

	public String getSpecialistId() {
		return specialistId;
	}

	public void setSpecialistId(String specialistId) {
		this.specialistId = specialistId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDdbaseId() {
		return ddbaseId;
	}

	public void setDdbaseId(String ddbaseId) {
		this.ddbaseId = ddbaseId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBrowse() {
		return browse;
	}

	public void setBrowse(Integer browse) {
		this.browse = browse;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public Date getSynchdate() {
		return synchdate;
	}

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
	}

}
