package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Taxon的Entity类</b></p>
 *<p> Taxon的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "taxon")
public class Taxon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String scientificname;
	private String chname;
	private String authorstr;
	private String nomencode;	// 命名法规
	private String rankid;
	
	private String refClassSys;	// 参考的分类体系
	
	private String refjson;

	private String sourcesid;
	
	private String expert; // Taxon审核人
	
	private String remark;	// 原始数据
	
	private String comment;	// 备注
	
	private String epithet;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private int status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;
	private String tci;
	private int taxonCondition;	// Taxon审核状态 0 - 未提交审核， 1 - 已提交审核， -1 - 审核不通过， 2 - 审核通过
	private String country;
	private String thing;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getThing() {
		return thing;
	}

	public void setThing(String thing) {
		this.thing = thing;
	}


	@Column(nullable=false, name="order_num", columnDefinition="int default 0")
	private int orderNum;
	
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	

	public Taxon(String id, String scientificname, String chname, String rankid, String sourcesid, String remark,
			String inputer, Date inputtime, int status, Date synchdate, int synchstatus, Taxaset taxaset, Rank rank) {
		this.id = id;
		this.scientificname = scientificname;
		this.chname = chname;
		this.rankid = rankid;
		this.sourcesid = sourcesid;
		this.remark = remark;
		this.inputer = inputer;
		this.inputtime = inputtime;
		this.status = status;
		this.synchdate = synchdate;
		this.synchstatus = synchstatus;
		this.taxaset = taxaset;
		this.rank = rank;
	}


	//bi-directional many-to-one association to Citation
	@OneToMany(mappedBy="taxon")
	private List<Citation> citations;

	//bi-directional many-to-one association to Description
	@OneToMany(mappedBy="taxon")
	private List<Description> descriptions;

	//bi-directional many-to-one association to Distributiondata
	@OneToMany(mappedBy="taxon")
	private List<Distributiondata> distributiondata;

	//bi-directional many-to-one association to Molecular
	@OneToMany(mappedBy="taxon")
	private List<Molecular> moleculars;

	//bi-directional many-to-one association to Multimedia
	@OneToMany(mappedBy="taxon")
	private List<Multimedia> multimedias;

	//bi-directional many-to-one association to Occurrence
	@OneToMany(mappedBy="taxon")
	private List<Occurrence> occurrences;

	//bi-directional many-to-one association to Protection
	@OneToMany(mappedBy="taxon")
	private List<Protection> protections;

	//bi-directional many-to-one association to Specimendata
	@OneToMany(mappedBy="taxon")
	private List<Specimendata> specimendata;

	//bi-directional many-to-one association to Taxkey
	@OneToMany(mappedBy="taxon")
	private List<Taxkey> taxkeys;

	@OneToMany(mappedBy="taxon")
	private List<Commonname> commonnames;

	//bi-directional many-to-one association to Taxaset
	@ManyToOne
	private Taxaset taxaset;

	//bi-directional many-to-many association to Taxtree
	@ManyToMany(mappedBy="taxons")
	private List<Taxtree> taxtrees;

	//bi-directional many-to-one association to Traitdata
	@OneToMany(mappedBy="taxon")
	private List<Traitdata> traitdata;
	
	//bi-directional many-to-one association to Rank
	@ManyToOne
	private Rank rank;
	public Taxon() {
	}
	
	public Taxon(String scientificname) {
		super();
		this.scientificname = scientificname;
	}

	public Taxon(String scientificname, String chname, String authorstr, String nomencode, String rankid,
			String refClassSys, String refjson, String sourcesid, String expert, String remark, String comment) {
		super();
		this.scientificname = scientificname;
		this.chname = chname;
		this.authorstr = authorstr;
		this.nomencode = nomencode;
		this.rankid = rankid;
		this.refClassSys = refClassSys;
		this.refjson = refjson;
		this.sourcesid = sourcesid;
		this.expert = expert;
		this.remark = remark;
		this.comment = comment;
	}
	
	public Taxon(String id, String rankid) {
		this.id = id;
		this.rankid = rankid;
		Rank r = new Rank();
		r.setId(rankid);
		this.rank = r;
	}

	public String getRefClassSys() {
		return refClassSys;
	}

	public void setRefClassSys(String refClassSys) {
		this.refClassSys = refClassSys;
	}

	public List<Commonname> getCommonnames() {
		return commonnames;
	}

	public void setCommonnames(List<Commonname> commonnames) {
		this.commonnames = commonnames;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChname() {
		return chname;
	}

	public void setChname(String chname) {
		this.chname = chname;
	}

	public String getAuthorstr() {
		return this.authorstr;
	}

	public void setAuthorstr(String authorstr) {
		this.authorstr = authorstr;
	}

	public String getEpithet() {
		return this.epithet;
	}

	public void setEpithet(String epithet) {
		this.epithet = epithet;
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

	public String getNomencode() {
		return this.nomencode;
	}

	public void setNomencode(String nomencode) {
		this.nomencode = nomencode;
	}

	public String getRankid() {
		return rankid;
	}

	public void setRankid(String rankid) {
		this.rankid = rankid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRefjson() {
		return this.refjson;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getScientificname() {
		return this.scientificname;
	}

	public void setScientificname(String scientificname) {
		this.scientificname = scientificname;
	}

	public String getSourcesid() {
		return sourcesid;
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

	public String getTci() {
		return this.tci;
	}

	public void setTci(String tci) {
		this.tci = tci;
	}

	public List<Citation> getCitations() {
		return this.citations;
	}

	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}

	public Citation addCitation(Citation citation) {
		getCitations().add(citation);
		citation.setTaxon(this);

		return citation;
	}

	public Citation removeCitation(Citation citation) {
		getCitations().remove(citation);
		citation.setTaxon(null);

		return citation;
	}

	public List<Description> getDescriptions() {
		return this.descriptions;
	}

	public void setDescriptions(List<Description> descriptions) {
		this.descriptions = descriptions;
	}

	public Description addDescription(Description description) {
		getDescriptions().add(description);
		description.setTaxon(this);

		return description;
	}

	public Description removeDescription(Description description) {
		getDescriptions().remove(description);
		description.setTaxon(null);

		return description;
	}

	public List<Distributiondata> getDistributiondata() {
		return this.distributiondata;
	}

	public void setDistributiondata(List<Distributiondata> distributiondata) {
		this.distributiondata = distributiondata;
	}

	public Distributiondata addDistributiondata(Distributiondata distributiondata) {
		getDistributiondata().add(distributiondata);
		distributiondata.setTaxon(this);

		return distributiondata;
	}

	public Distributiondata removeDistributiondata(Distributiondata distributiondata) {
		getDistributiondata().remove(distributiondata);
		distributiondata.setTaxon(null);

		return distributiondata;
	}

	public List<Molecular> getMoleculars() {
		return this.moleculars;
	}

	public void setMoleculars(List<Molecular> moleculars) {
		this.moleculars = moleculars;
	}

	public Molecular addMolecular(Molecular molecular) {
		getMoleculars().add(molecular);
		molecular.setTaxon(this);

		return molecular;
	}

	public Molecular removeMolecular(Molecular molecular) {
		getMoleculars().remove(molecular);
		molecular.setTaxon(null);

		return molecular;
	}

	public List<Multimedia> getMultimedias() {
		return this.multimedias;
	}

	public void setMultimedias(List<Multimedia> multimedias) {
		this.multimedias = multimedias;
	}

	public Multimedia addMultimedia(Multimedia multimedia) {
		getMultimedias().add(multimedia);
		multimedia.setTaxon(this);

		return multimedia;
	}

	public Multimedia removeMultimedia(Multimedia multimedia) {
		getMultimedias().remove(multimedia);
		multimedia.setTaxon(null);

		return multimedia;
	}

	public List<Occurrence> getOccurrences() {
		return this.occurrences;
	}

	public void setOccurrences(List<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}

	public Occurrence addOccurrence(Occurrence occurrence) {
		getOccurrences().add(occurrence);
		occurrence.setTaxon(this);

		return occurrence;
	}

	public Occurrence removeOccurrence(Occurrence occurrence) {
		getOccurrences().remove(occurrence);
		occurrence.setTaxon(null);

		return occurrence;
	}

	public List<Protection> getProtections() {
		return this.protections;
	}

	public void setProtections(List<Protection> protections) {
		this.protections = protections;
	}

	public Protection addProtection(Protection protection) {
		getProtections().add(protection);
		protection.setTaxon(this);

		return protection;
	}

	public Protection removeProtection(Protection protection) {
		getProtections().remove(protection);
		protection.setTaxon(null);

		return protection;
	}

	public List<Specimendata> getSpecimendata() {
		return this.specimendata;
	}

	public void setSpecimendata(List<Specimendata> specimendata) {
		this.specimendata = specimendata;
	}

	public Specimendata addSpecimendata(Specimendata specimendata) {
		getSpecimendata().add(specimendata);
		specimendata.setTaxon(this);

		return specimendata;
	}

	public Specimendata removeSpecimendata(Specimendata specimendata) {
		getSpecimendata().remove(specimendata);
		specimendata.setTaxon(null);

		return specimendata;
	}

	public List<Taxkey> getTaxkeys() {
		return this.taxkeys;
	}

	public void setTaxkeys(List<Taxkey> taxkeys) {
		this.taxkeys = taxkeys;
	}

	public Taxkey addTaxkey(Taxkey taxkey) {
		getTaxkeys().add(taxkey);
		taxkey.setTaxon(this);

		return taxkey;
	}

	public Taxkey removeTaxkey(Taxkey taxkey) {
		getTaxkeys().remove(taxkey);
		taxkey.setTaxon(null);

		return taxkey;
	}

	public Rank getRank() {
		return this.rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public Taxaset getTaxaset() {
		return this.taxaset;
	}

	public void setTaxaset(Taxaset taxaset) {
		this.taxaset = taxaset;
	}

	public List<Taxtree> getTaxtrees() {
		return this.taxtrees;
	}

	public void setTaxtrees(List<Taxtree> taxtrees) {
		this.taxtrees = taxtrees;
	}

	public List<Traitdata> getTraitdata() {
		return this.traitdata;
	}

	public void setTraitdata(List<Traitdata> traitdata) {
		this.traitdata = traitdata;
	}

	public Traitdata addTraitdata(Traitdata traitdata) {
		getTraitdata().add(traitdata);
		traitdata.setTaxon(this);

		return traitdata;
	}

	public Traitdata removeTraitdata(Traitdata traitdata) {
		getTraitdata().remove(traitdata);
		traitdata.setTaxon(null);

		return traitdata;
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public int getTaxonCondition() {
		return taxonCondition;
	}

	public void setTaxonCondition(int taxonCondition) {
		this.taxonCondition = taxonCondition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorstr == null) ? 0 : authorstr.hashCode());
		result = prime * result + ((chname == null) ? 0 : chname.hashCode());
		result = prime * result + ((citations == null) ? 0 : citations.hashCode());
		result = prime * result + ((commonnames == null) ? 0 : commonnames.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((distributiondata == null) ? 0 : distributiondata.hashCode());
		result = prime * result + ((epithet == null) ? 0 : epithet.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((moleculars == null) ? 0 : moleculars.hashCode());
		result = prime * result + ((multimedias == null) ? 0 : multimedias.hashCode());
		result = prime * result + ((nomencode == null) ? 0 : nomencode.hashCode());
		result = prime * result + ((occurrences == null) ? 0 : occurrences.hashCode());
		result = prime * result + ((protections == null) ? 0 : protections.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((rankid == null) ? 0 : rankid.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((scientificname == null) ? 0 : scientificname.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + ((specimendata == null) ? 0 : specimendata.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxaset == null) ? 0 : taxaset.hashCode());
		result = prime * result + ((taxkeys == null) ? 0 : taxkeys.hashCode());
		result = prime * result + taxonCondition;
		result = prime * result + ((expert == null) ? 0 : expert.hashCode());
		result = prime * result + ((taxtrees == null) ? 0 : taxtrees.hashCode());
		result = prime * result + ((tci == null) ? 0 : tci.hashCode());
		result = prime * result + ((traitdata == null) ? 0 : traitdata.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Taxon other = (Taxon) obj;
		if (authorstr == null) {
			if (other.authorstr != null)
				return false;
		} else if (!authorstr.equals(other.authorstr))
			return false;
		if (chname == null) {
			if (other.chname != null)
				return false;
		} else if (!chname.equals(other.chname))
			return false;
		if (citations == null) {
			if (other.citations != null)
				return false;
		} else if (!citations.equals(other.citations))
			return false;
		if (commonnames == null) {
			if (other.commonnames != null)
				return false;
		} else if (!commonnames.equals(other.commonnames))
			return false;
		if (descriptions == null) {
			if (other.descriptions != null)
				return false;
		} else if (!descriptions.equals(other.descriptions))
			return false;
		if (distributiondata == null) {
			if (other.distributiondata != null)
				return false;
		} else if (!distributiondata.equals(other.distributiondata))
			return false;
		if (epithet == null) {
			if (other.epithet != null)
				return false;
		} else if (!epithet.equals(other.epithet))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inputer == null) {
			if (other.inputer != null)
				return false;
		} else if (!inputer.equals(other.inputer))
			return false;
		if (inputtime == null) {
			if (other.inputtime != null)
				return false;
		} else if (!inputtime.equals(other.inputtime))
			return false;
		if (moleculars == null) {
			if (other.moleculars != null)
				return false;
		} else if (!moleculars.equals(other.moleculars))
			return false;
		if (multimedias == null) {
			if (other.multimedias != null)
				return false;
		} else if (!multimedias.equals(other.multimedias))
			return false;
		if (nomencode == null) {
			if (other.nomencode != null)
				return false;
		} else if (!nomencode.equals(other.nomencode))
			return false;
		if (occurrences == null) {
			if (other.occurrences != null)
				return false;
		} else if (!occurrences.equals(other.occurrences))
			return false;
		if (protections == null) {
			if (other.protections != null)
				return false;
		} else if (!protections.equals(other.protections))
			return false;
		if (rank == null) {
			if (other.rank != null)
				return false;
		} else if (!rank.equals(other.rank))
			return false;
		if (rankid == null) {
			if (other.rankid != null)
				return false;
		} else if (!rankid.equals(other.rankid))
			return false;
		if (refjson == null) {
			if (other.refjson != null)
				return false;
		} else if (!refjson.equals(other.refjson))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (scientificname == null) {
			if (other.scientificname != null)
				return false;
		} else if (!scientificname.equals(other.scientificname))
			return false;
		if (sourcesid == null) {
			if (other.sourcesid != null)
				return false;
		} else if (!sourcesid.equals(other.sourcesid))
			return false;
		if (specimendata == null) {
			if (other.specimendata != null)
				return false;
		} else if (!specimendata.equals(other.specimendata))
			return false;
		if (status != other.status)
			return false;
		if (synchdate == null) {
			if (other.synchdate != null)
				return false;
		} else if (!synchdate.equals(other.synchdate))
			return false;
		if (synchstatus != other.synchstatus)
			return false;
		if (taxaset == null) {
			if (other.taxaset != null)
				return false;
		} else if (!taxaset.equals(other.taxaset))
			return false;
		if (taxkeys == null) {
			if (other.taxkeys != null)
				return false;
		} else if (!taxkeys.equals(other.taxkeys))
			return false;
		if (taxonCondition != other.taxonCondition)
			return false;
		if (expert == null) {
			if (other.expert != null)
				return false;
		} else if (!expert.equals(other.expert))
			return false;
		if (taxtrees == null) {
			if (other.taxtrees != null)
				return false;
		} else if (!taxtrees.equals(other.taxtrees))
			return false;
		if (tci == null) {
			if (other.tci != null)
				return false;
		} else if (!tci.equals(other.tci))
			return false;
		if (traitdata == null) {
			if (other.traitdata != null)
				return false;
		} else if (!traitdata.equals(other.traitdata))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "id=" + id + ", scientificname=" + scientificname + ", refjson=" + refjson;
	}

	
}