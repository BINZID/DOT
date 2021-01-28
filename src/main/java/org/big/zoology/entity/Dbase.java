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
public class Dbase implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String dbname;

	/**
	 * 简介
	 */
	private String dbabstract;

	private String edbname;

	/**
	 * 简介(英文)
	 */
	private String edbabstract;

	/**
	 * 共享声明(英文)
	 */
	private String eshareinfo;

	/**
	 * 链接
	 */
	private String dburl;

	/**
	 * 图片链接
	 */
	private String imageurl;

	/**
	 * 元数据
	 */
	private String metadata;

	/**
	 * 共享声明
	 */
	private String shareinfo;

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

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getDbabstract() {
		return dbabstract;
	}

	public void setDbabstract(String dbabstract) {
		this.dbabstract = dbabstract;
	}

	public String getEdbname() {
		return edbname;
	}

	public void setEdbname(String edbname) {
		this.edbname = edbname;
	}

	public String getEdbabstract() {
		return edbabstract;
	}

	public void setEdbabstract(String edbabstract) {
		this.edbabstract = edbabstract;
	}

	public String getEshareinfo() {
		return eshareinfo;
	}

	public void setEshareinfo(String eshareinfo) {
		this.eshareinfo = eshareinfo;
	}

	public String getDburl() {
		return dburl;
	}

	public void setDburl(String dburl) {
		this.dburl = dburl;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getShareinfo() {
		return shareinfo;
	}

	public void setShareinfo(String shareinfo) {
		this.shareinfo = shareinfo;
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
