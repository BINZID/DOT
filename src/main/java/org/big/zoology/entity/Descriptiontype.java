package org.big.zoology.entity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author HM
 * @since 2019-10-18
 */
public class Descriptiontype implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String descterm;

	/**
	 * 术语含义
	 */
	private String meaning;

	private Integer pid;

	private String dtorder;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescterm() {
		return descterm;
	}

	public void setDescterm(String descterm) {
		this.descterm = descterm;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getDtorder() {
		return dtorder;
	}

	public void setDtorder(String dtorder) {
		this.dtorder = dtorder;
	}
}
