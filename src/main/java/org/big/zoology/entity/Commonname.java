package org.big.zoology.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author HM
 * @since 2019-10-18
 */
public class Commonname implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String commonname;

	/**
	 * 拼音
	 */
	private String pinxin;

	/**
	 * 语言:1-中文2-英语3-法语4-俄罗斯语5-西班牙语6-其他
	 */
	private String language;

	/**
	 * 数据源ID
	 */
	private String sourcesId;

	/**
	 * 参考文献
	 */
	private String referencejson;

	/**
	 * 审核专家
	 */
	private String specialistId;

	private String taxonId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 数据库
	 */
	private String dbaseId;

	/**
	 * 状态（默认1、可用；0、不可用）
	 */
	private Integer status;

	/**
	 * 录入人
	 */
	private String inputer;

	/**
	 * 录入时间
	 */
	private Date inputtime;

	/**
	 * 更新时间
	 */
	private Date synchdate;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCommonname() {
		return commonname;
	}

	public void setCommonname(String commonname) {
		this.commonname = commonname;
	}

	public String getPinxin() {
		return pinxin;
	}

	public void setPinxin(String pinxin) {
		this.pinxin = pinxin;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getTaxonId() {
		return taxonId;
	}

	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDbaseId() {
		return dbaseId;
	}

	public void setDbaseId(String dbaseId) {
		this.dbaseId = dbaseId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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