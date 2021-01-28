package org.big.zoology.entity;

import java.io.Serializable;

/**
 * @ClassName Rank
 * @Description: TODO
 * @Author NY
 * @Date: 2019/11/13 14:34
 * @return
 * @Version V1.0
 */
public class Rank implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 等级的中文名
	 */
	private String chname;

	/**
	 * 等级的拉丁、英文名
	 */
	private String enname;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getChname() {
		return chname;
	}

	public void setChname(String chname) {
		this.chname = chname;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

}
