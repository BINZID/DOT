package org.big.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *<p><b>Keyitem的Entity类</b></p>
 *<p> Keyitem的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "keyitem")
public class Keyitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private int branchid;

	private int innerorder;

	private String item;

	private int orderid;

	private String pid;

	private String taxonid;

	private String keytype;
	
	//bi-directional many-to-one association to Keyitem
	@OneToMany(mappedBy="keyitem")
	private List<Resource> resources;
	//bi-directional many-to-one association to Taxkey
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxkey taxkey;

	public Keyitem() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBranchid() {
		return branchid;
	}

	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}

	public void setFeatureimgjson(String featureimgjson) {
	}

	public int getInnerorder() {
		return innerorder;
	}

	public void setInnerorder(int innerorder) {
		this.innerorder = innerorder;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTaxonid() {
		return taxonid;
	}

	public void setTaxonid(String taxonid) {
		this.taxonid = taxonid;
	}

	public String getKeytype() {
		return keytype;
	}

	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public Taxkey getTaxkey() {
		return taxkey;
	}

	public void setTaxkey(Taxkey taxkey) {
		this.taxkey = taxkey;
	}

}