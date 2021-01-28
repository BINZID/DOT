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
public class Specialist implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;

	/**
	 * 专家姓名
	 */
	private String cnname;

	/**
	 * 专家英文名
	 */
	private String enname;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 单位地址
	 */
	private String cnaddress;

	/**
	 * 单位地址（英文）
	 */
	private String enaddress;

	/**
	 * 单位名称
	 */
	private String cncompany;

	/**
	 * 单位英文名称
	 */
	private String encompany;

	/**
	 * 专家主页
	 */
	private String cnhomepage;

	/**
	 * 专家主页（英文）
	 */
	private String enhomepage;

	/**
	 * 简介
	 */
	private String introduction;

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

	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCnaddress() {
		return cnaddress;
	}

	public void setCnaddress(String cnaddress) {
		this.cnaddress = cnaddress;
	}

	public String getEnaddress() {
		return enaddress;
	}

	public void setEnaddress(String enaddress) {
		this.enaddress = enaddress;
	}

	public String getCncompany() {
		return cncompany;
	}

	public void setCncompany(String cncompany) {
		this.cncompany = cncompany;
	}

	public String getEncompany() {
		return encompany;
	}

	public void setEncompany(String encompany) {
		this.encompany = encompany;
	}

	public String getCnhomepage() {
		return cnhomepage;
	}

	public void setCnhomepage(String cnhomepage) {
		this.cnhomepage = cnhomepage;
	}

	public String getEnhomepage() {
		return enhomepage;
	}

	public void setEnhomepage(String enhomepage) {
		this.enhomepage = enhomepage;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
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
