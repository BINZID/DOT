package org.big.zoology.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName 物种数据统计
 * @Description: TODO
 * @Author NY
 * @Date: 2019/11/25 15:36
 * @return
 * @Version V1.0
 */
public class Taxoncount implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 描述数量
	 */
	private Integer desccount;

	/**
	 * 形态描述数量
	 */
	private Integer descformcount;

	/**
	 * 多媒体数量
	 */
	private Integer multimediacount;

	/**
	 * 分类单元ID
	 */
	private String taxonId;

	/**
	 * 类别
	 */
	private String type;

	/**
	 * 二级分类
	 */
	private String assort;

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

	public Integer getDesccount() {
		return desccount;
	}

	public void setDesccount(Integer desccount) {
		this.desccount = desccount;
	}

	public Integer getDescformcount() {
		return descformcount;
	}

	public void setDescformcount(Integer descformcount) {
		this.descformcount = descformcount;
	}

	public Integer getMultimediacount() {
		return multimediacount;
	}

	public void setMultimediacount(Integer multimediacount) {
		this.multimediacount = multimediacount;
	}

	public String getTaxonId() {
		return taxonId;
	}

	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
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
