package org.big.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 *<p><b>Category的Entity类</b></p>
 *<p> Category的Entity类</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", columnDefinition="varchar(50) COMMENT 'UUID唯一标识'")
	private String id;
	
	@Column(name="table_cn", columnDefinition="varchar(100) COMMENT '表名中文'" )
	private String tableCn;
	
	@Column(name="table_en", columnDefinition="varchar(100) COMMENT '表名英文'" )
	private String tableEn;
	
	@Column(name="field_cn", columnDefinition="varchar(100) COMMENT '表字段名中文'" )
	private String fieldCn;
	
	@Column(name="field_en", columnDefinition="varchar(100) COMMENT '表字段名英文'" )
	private String fieldEn;
	
	@Column(name="content", columnDefinition="varchar(200) COMMENT '类别内容'" )
	private String content;
	
	@Column(name="pid", columnDefinition="varchar(50) COMMENT '父级ID'" )
	private String pid;
	
	@Column(name="sort", columnDefinition="int COMMENT '排序字段'" )
	private Integer sort;
	
	@Column(name="status", columnDefinition="INT DEFAULT 1 COMMENT '状态（默认1.可用；0.不可用）'" )
	private Integer status;
	
	public Category() {

	}

	public Category(String id, String tableCn, String tableEn, String fieldCn, String fieldEn, String content,
			String pid, Integer sort, Integer status) {
		super();
		this.id = id;
		this.tableCn = tableCn;
		this.tableEn = tableEn;
		this.fieldCn = fieldCn;
		this.fieldEn = fieldEn;
		this.content = content;
		this.pid = pid;
		this.sort = sort;
		this.status = status;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableCn() {
		return tableCn;
	}

	public void setTableCn(String tableCn) {
		this.tableCn = tableCn;
	}

	public String getTableEn() {
		return tableEn;
	}

	public void setTableEn(String tableEn) {
		this.tableEn = tableEn;
	}

	public String getFieldCn() {
		return fieldCn;
	}

	public void setFieldCn(String fieldCn) {
		this.fieldCn = fieldCn;
	}

	public String getFieldEn() {
		return fieldEn;
	}

	public void setFieldEn(String fieldEn) {
		this.fieldEn = fieldEn;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", tableCn=" + tableCn + ", tableEn=" + tableEn + ", fieldCn=" + fieldCn
				+ ", fieldEn=" + fieldEn + ", content=" + content + ", pid=" + pid + ", sort=" + sort + ", status="
				+ status + "]";
	}

}