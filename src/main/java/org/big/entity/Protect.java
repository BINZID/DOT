package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *<p><b>Protect的Entity类</b></p>
 *<p> Protect的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/07/27 15:31</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "protect")
public class Protect implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", columnDefinition = "varchar(50) COMMENT 'UUID唯一标识'")
	private String id;

	@Column(name = "title", columnDefinition = "varchar(500) COMMENT '例子：中国保护植物第一批'")
	private String title;

	@Column(name = "kingdom", columnDefinition = "varchar(200) COMMENT '界'")
	private String kingdom;
	
	@Column(name = "kingdom_c", columnDefinition = "varchar(200) COMMENT '界中文名'")
	private String kingdomC;
	
	@Column(name = "phylum", columnDefinition = "varchar(200) COMMENT '门'")
	private String phylum;
	
	@Column(name = "phylum_c", columnDefinition = "varchar(200) COMMENT '门中文名'")
	private String phylumC;
	
	@Column(name = "class", columnDefinition = "varchar(200) COMMENT '纲'")
	private String clazz;
	
	@Column(name = "class_c", columnDefinition = "varchar(200) COMMENT '纲中文名'")
	private String classC;
	
	@Column(name = "orderen", columnDefinition = "varchar(200) COMMENT '目'")
	private String order;
	
	@Column(name = "order_c", columnDefinition = "varchar(200) COMMENT '目中文名'")
	private String orderC;
	
	@Column(name = "family", columnDefinition = "varchar(200) COMMENT '科'")
	private String family;
	
	@Column(name = "family_c", columnDefinition = "varchar(200) COMMENT '科中文名'")
	private String familyC;
	
	@Column(name = "genus", columnDefinition = "varchar(200) COMMENT '属'")
	private String genus;
	
	@Column(name = "genus_c", columnDefinition = "varchar(200) COMMENT '属中文名'")
	private String genusC;
	
	@Column(name = "scientific_name", columnDefinition = "varchar(200) COMMENT '物种拉丁名'")
	private String scientificName;
	
	@Column(name = "cnname", columnDefinition = "varchar(200) COMMENT '物种中文名'")
	private String cnname;
	
	@Column(name = "full_name", columnDefinition = "varchar(500) COMMENT '学名带命名人'")
	private String fullName;
	
	@Column(name = "protect_level", columnDefinition = "varchar(50) COMMENT '国家保护等级'")
	private String protectLevel;

	@Column(name = "IUCN", columnDefinition = "varchar(50) COMMENT 'IUCN'")
	private String iucn;

	@Column(name = "type", columnDefinition = "varchar(100) COMMENT '物种类群'")
	private String type;
	
	@Column(name = "rank", columnDefinition = "varchar(100) COMMENT '分类等级'")
	private String rank;
	
	@Column(name = "version", columnDefinition = "varchar(500) COMMENT '年份/版本'")
	private String version;
	
	@Column(name = "remark", columnDefinition = "varchar(500) COMMENT '备注'")
	private String remark;
	

	public Protect() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKingdom() {
		return kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getKingdomC() {
		return kingdomC;
	}

	public void setKingdomC(String kingdomC) {
		this.kingdomC = kingdomC;
	}

	public String getPhylum() {
		return phylum;
	}

	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	public String getPhylumC() {
		return phylumC;
	}

	public void setPhylumC(String phylumC) {
		this.phylumC = phylumC;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getClassC() {
		return classC;
	}

	public void setClassC(String classC) {
		this.classC = classC;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderC() {
		return orderC;
	}

	public void setOrderC(String orderC) {
		this.orderC = orderC;
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

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getGenusC() {
		return genusC;
	}

	public void setGenusC(String genusC) {
		this.genusC = genusC;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProtectLevel() {
		return protectLevel;
	}

	public void setProtectLevel(String protectLevel) {
		this.protectLevel = protectLevel;
	}

	public String getIucn() {
		return iucn;
	}

	public void setIucn(String iucn) {
		this.iucn = iucn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Protect(String id, String kingdom, String kingdomC, String phylum, String phylumC, String clazz,
			String classC, String order, String orderC, String family, String familyC, String genus, String genusC,
			String scientificName, String cnname, String fullName, String protectLevel, String iucn, String type,
			String rank, String version, String remark, String title) {
		super();
		this.id = id;
		this.kingdom = kingdom;
		this.kingdomC = kingdomC;
		this.phylum = phylum;
		this.phylumC = phylumC;
		this.clazz = clazz;
		this.classC = classC;
		this.order = order;
		this.orderC = orderC;
		this.family = family;
		this.familyC = familyC;
		this.genus = genus;
		this.genusC = genusC;
		this.scientificName = scientificName;
		this.cnname = cnname;
		this.fullName = fullName;
		this.protectLevel = protectLevel;
		this.iucn = iucn;
		this.type = type;
		this.rank = rank;
		this.version = version;
		this.remark = remark;
		this.title = title;
	}
}