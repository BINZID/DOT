package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *<p><b>Citation的Entity类</b></p>
 *<p> Citation的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "citation")
public class Citation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String sciname; // 引证名称 
	
	private String authorship;

	private String nametype;
	private String citationstr;
	private String sourcesid;
	private String refjson;
	private String expert;

	private String shortrefs;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private int status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;
	
	private String remark;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;

	public Citation() {
	}
	
	public Citation(String sciname, String authorship, String nametype, String citationstr, String sourcesid, String refjson,
			String expert, Taxon taxon) {
		super();
		this.sciname = sciname;
		this.authorship = authorship;
		this.nametype = nametype;
		this.citationstr = citationstr;
		this.sourcesid = sourcesid;
		this.refjson = refjson;
		this.expert = expert;
		this.taxon = taxon;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthorship() {
		return this.authorship;
	}

	public void setAuthorship(String authorship) {
		this.authorship = authorship;
	}

	public String getCitationstr() {
		return this.citationstr;
	}

	public void setCitationstr(String citationstr) {
		this.citationstr = citationstr;
	}

	public String getInputer() {
		return this.inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return this.inputtime;
	}

	public void setInputtime(Timestamp inputtime) {
		this.inputtime = inputtime;
	}

	public String getNametype() {
		return nametype;
	}

	public void setNametype(String nametype) {
		this.nametype = nametype;
	}

	public String getRefjson() {
		return this.refjson;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
	}

	public String getSciname() {
		return this.sciname;
	}

	public void setSciname(String sciname) {
		this.sciname = sciname;
	}

	public String getShortrefs() {
		return this.shortrefs;
	}

	public void setShortrefs(String shortrefs) {
		this.shortrefs = shortrefs;
	}

	public String getSourcesid() {
		return this.sourcesid;
	}

	public void setSourcesid(String sourcesid) {
		this.sourcesid = sourcesid;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getSynchdate() {
		return this.synchdate;
	}

	public void setSynchdate(Timestamp synchdate) {
		this.synchdate = synchdate;
	}

	public int getSynchstatus() {
		return this.synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	public Taxon getTaxon() {
		return this.taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@Override
	public String toString() {
		return "id=" + id + ", sciname=" + sciname + ", refjson=" + refjson;
	}
	
}