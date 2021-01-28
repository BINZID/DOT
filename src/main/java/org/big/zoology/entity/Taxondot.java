package org.big.zoology.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName 物种对接表
 * @Description: TODO
 * @Author NY
 * @Date: 2019/11/26 9:15
 * @return
 * @Version V1.0
 */
public class Taxondot implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * taxonID
	 */
	private String taxonId;

	/**
	 * 采集系统ID
	 */
	private String oldtaxonId;

	/**
	 * 采集系统数据源ID
	 */
	private String oldsourcesId;

	/**
	 * 采集系统分类单元集ID
	 */
	private String taxasetId;

	/**
	 * 状态（0:自身、1:拉丁名相同中文相同、2:拉丁名相同中文名不同、3:拉丁名不同中文名相同）
	 */
	private Integer relations;

	/**
	 * 采集系统分类地位
	 */
	private String oldsortjson;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaxonId() {
		return taxonId;
	}

	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
	}

	public String getOldtaxonId() {
		return oldtaxonId;
	}

	public void setOldtaxonId(String oldtaxonId) {
		this.oldtaxonId = oldtaxonId;
	}

	public String getOldsourcesId() {
		return oldsourcesId;
	}

	public void setOldsourcesId(String oldsourcesId) {
		this.oldsourcesId = oldsourcesId;
	}

	public String getTaxasetId() {
		return taxasetId;
	}

	public void setTaxasetId(String taxasetId) {
		this.taxasetId = taxasetId;
	}

	public Integer getRelations() {
		return relations;
	}

	public void setRelations(Integer relations) {
		this.relations = relations;
	}

	public String getOldsortjson() {
		return oldsortjson;
	}

	public void setOldsortjson(String oldsortjson) {
		this.oldsortjson = oldsortjson;
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
