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
public class Citation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String scientificname;

	private String namestatus;

	/**
	 * 命名信息
	 */
	private String authorship;

	/**
	 * 完整引证
	 */
	private String citationstr;

	/**
	 * 对citaitonstr进行拆分得到的短引证
	 */
	private String shortrefs;

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

	public String getScientificname() {
		return scientificname;
	}

	public void setScientificname(String scientificname) {
		this.scientificname = scientificname;
	}

	public String getNamestatus() {
		return namestatus;
	}

	public void setNamestatus(String namestatus) {
		this.namestatus = namestatus;
	}

	public String getAuthorship() {
		return authorship;
	}

	public void setAuthorship(String authorship) {
		this.authorship = authorship;
	}

	public String getCitationstr() {
		return citationstr;
	}

	public void setCitationstr(String citationstr) {
		this.citationstr = citationstr;
	}

	public String getShortrefs() {
		return shortrefs;
	}

	public void setShortrefs(String shortrefs) {
		this.shortrefs = shortrefs;
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
