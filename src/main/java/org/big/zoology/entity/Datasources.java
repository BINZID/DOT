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
public class Datasources implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String dsname;

	private String dstype;

	private String version;

	/**
	 * 版权声明
	 */
	private String copyright;

	/**
	 * 版权所有者
	 */
	private String rightsholder;

	private String creater;

	private Date createtime;

	private String dsabstract;

	/**
	 * 关键字
	 */
	private String dskeyword;

	/**
	 * 链接
	 */
	private String dsurl;

	/**
	 * 审核专家
	 */
	private String specialistId;

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

	/**
	 * 备注
	 */
	private String remark;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDsname() {
		return dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	public String getDstype() {
		return dstype;
	}

	public void setDstype(String dstype) {
		this.dstype = dstype;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getRightsholder() {
		return rightsholder;
	}

	public void setRightsholder(String rightsholder) {
		this.rightsholder = rightsholder;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getDsabstract() {
		return dsabstract;
	}

	public void setDsabstract(String dsabstract) {
		this.dsabstract = dsabstract;
	}

	public String getDskeyword() {
		return dskeyword;
	}

	public void setDskeyword(String dskeyword) {
		this.dskeyword = dskeyword;
	}

	public String getDsurl() {
		return dsurl;
	}

	public void setDsurl(String dsurl) {
		this.dsurl = dsurl;
	}

	public String getSpecialistId() {
		return specialistId;
	}

	public void setSpecialistId(String specialistId) {
		this.specialistId = specialistId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
