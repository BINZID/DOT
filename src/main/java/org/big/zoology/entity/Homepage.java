package org.big.zoology.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName 页面显示
 * @Description: TODO
 * @Author NY
 * @Date: 2019/11/25 15:19
 * @return
 * @Version V1.0
 */
public class Homepage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 学名
	 */
	private String scientificname;

	/**
	 * 中文名
	 */
	private String chinesename;

	/**
	 * 描述信息
	 */
	private String descontent;

	/**
	 * 页面推介类型
	 */
	private String type;

	/**
	 * 图片路径
	 */
	private String path;

	/**
	 * 显示顺序
	 */
	private String level;

	private String taxonId;

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

	public String getChinesename() {
		return chinesename;
	}

	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}

	public String getDescontent() {
		return descontent;
	}

	public void setDescontent(String descontent) {
		this.descontent = descontent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getTaxonId() {
		return taxonId;
	}

	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
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
