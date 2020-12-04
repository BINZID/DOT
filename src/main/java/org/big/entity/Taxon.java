package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Taxon的Entity类</b></p>
 *<p> Taxon的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/30 09:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Taxon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String scientificname;
	private String chname;
	private String pinyin;
	private String authorstr;
	private String nomencode;
	private String rankid;
	private String refClassSys;
	private String sourcesid;
	private String specialistId;	// Taxon审核人
	private String remark;			// 原始数据
	private String comment;			// 备注
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
	@Column(nullable=true, name="order_num", columnDefinition="int default 0")
	private Integer orderNum;

	@OneToMany(mappedBy="taxon")
	private List<Citation> citations;

	@OneToMany(mappedBy="taxon")
	private List<Description> descriptions;

	@OneToMany(mappedBy="taxon")
	private List<Distributiondata> distributiondata;

	@OneToMany(mappedBy="taxon")
	private List<Molecular> moleculars;

	@OneToMany(mappedBy="taxon")
	private List<Multimedia> multimedias;

	@OneToMany(mappedBy="taxon")
	private List<Occurrence> occurrences;

	@OneToMany(mappedBy="taxon")
	private List<Protection> protections;

	@OneToMany(mappedBy="taxon")
	private List<Specimendata> specimendata;

	@OneToMany(mappedBy="taxon")
	private List<Taxkey> taxkeys;

	@OneToMany(mappedBy="taxon")
	private List<Commonname> commonnames;

	@ManyToOne
	private Taxaset taxaset;

	@ManyToMany(mappedBy="taxons")
	private List<Taxtree> taxtrees;

	@OneToMany(mappedBy="taxon")
	private List<Traitdata> traitdata;
	
	@ManyToOne
	private Rank rank;
	public Taxon() {
	}

	public List<Citation> getCitations() {
		return citations;
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

}