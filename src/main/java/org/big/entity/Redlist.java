package org.big.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *<p><b>Redlist的Entity类</b></p>
 *<p> Redlist的Entity类</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "redlist")
public class Redlist {
	@Id
	@Column(name="id", columnDefinition="varchar(50) COMMENT 'UUID唯一标识'")
	private String id;
	
	@Column(name="family", columnDefinition="varchar(200) COMMENT '科拉丁名'" )
	private String family;
	
	@Column(name="family_c", columnDefinition="varchar(200) COMMENT '科中文名'" )
	private String familyC;
	
	@Column(name="scientificname", columnDefinition="varchar(300) COMMENT '物种拉丁名'" )
	private String scientificname;
	
	@Column(name="chname", columnDefinition="varchar(200) COMMENT '物种中文名'" )
	private String chname;
	
	@Column(name="status", columnDefinition="varchar(200) COMMENT '红色名录评估等级'" )
	private String status;
	
	@Column(name="assess", columnDefinition="varchar(500) COMMENT '评估依据'" )
	private String assess;
	
	@Column(name="endemic", columnDefinition="varchar(50) COMMENT '是否是特有种'" )
	private String endemic;
	
	@Column(name="type", columnDefinition="varchar(50) COMMENT '物种类群'" )
	private String type;
	
	@Column(name="version", columnDefinition="varchar(100) COMMENT '版本/年份'" )
	private String version;

	public Redlist() {
		
	}

	public Redlist(String id, String family, String familyC, String scientificname, String chname, String status,
			String assess, String endemic, String type) {
		this.id = id;
		this.family = family;
		this.familyC = familyC;
		this.scientificname = scientificname;
		this.chname = chname;
		this.status = status;
		this.assess = assess;
		this.endemic = endemic;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getFamilyC() {
		return familyC;
	}

	public void setFamilyC(String familyC) {
		this.familyC = familyC;
	}

	public String getScientificname() {
		return scientificname;
	}

	public void setScientificname(String scientificname) {
		this.scientificname = scientificname;
	}

	public String getChname() {
		return chname;
	}

	public void setChname(String chname) {
		this.chname = chname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssess() {
		return assess;
	}

	public void setAssess(String assess) {
		this.assess = assess;
	}

	public String getEndemic() {
		return endemic;
	}

	public void setEndemic(String endemic) {
		this.endemic = endemic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
