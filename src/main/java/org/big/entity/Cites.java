package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *<p><b>Cites的Entity类</b></p>
 *<p> Cites的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/07/27 15:31</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "cites")
public class Cites implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "taxon_id", columnDefinition = "varchar(50) COMMENT 'UUID唯一标识'")
	private String taxonId;

	@Column(name = "kingdom", columnDefinition = "varchar(200) COMMENT '界'")
	private String kingdom;
	
	@Column(name = "phylum", columnDefinition = "varchar(200) COMMENT '门'")
	private String phylum;
	
	@Column(name = "class", columnDefinition = "varchar(200) COMMENT '纲'")
	private String clazz;
	
	@Column(name = "orderen", columnDefinition = "varchar(200) COMMENT '目'")
	private String order;
	
	@Column(name = "family", columnDefinition = "varchar(200) COMMENT '科'")
	private String family;
	
	@Column(name = "genus", columnDefinition = "varchar(200) COMMENT '属'")
	private String genus;
	
	@Column(name = "species", columnDefinition = "varchar(200) COMMENT '种加词'")
	private String species;
	
	@Column(name = "subspecies", columnDefinition = "varchar(200) COMMENT '亚种加词'")
	private String subspecies;
	
	@Column(name = "full_name", columnDefinition = "varchar(300) COMMENT '物种拉丁名'")
	private String fullName;
	
	@Column(name = "author_year", columnDefinition = "text COMMENT '命名人及年份'")
	private String authorYear;
	
	@Column(name = "rank_name", columnDefinition = "text COMMENT '分类等级'")
	private String rankName;
	
	@Column(name = "current_listing", columnDefinition = "text COMMENT '附录等级'")
	private String currentListing;
	
	@Column(name = "full_annotation_english", columnDefinition = "text COMMENT '完整英语注释'")
	private String fullAnnotationEnglish;
	
	@Column(name = "annotation_english", columnDefinition = "text COMMENT '英文注释'")
	private String annotationEnglish;
	
	@Column(name = "annotation_spanish", columnDefinition = "text COMMENT '西班牙文注释'")
	private String annotationSpanish;
	
	@Column(name = "annotation_french", columnDefinition = "text COMMENT '法文注释'")
	private String annotationFrench;
	
	@Column(name = "annotation_symbol", columnDefinition = "text COMMENT '（#）注释1'")
	private String annotationSymbol;
	
	@Column(name = "annotation", columnDefinition = "text COMMENT '#注释2'")
	private String annotation;
	
	@Column(name = "synonyms_with_authors", columnDefinition = "text COMMENT '异名及其命名人信息'")
	private String synonymsWithAuthors;
	
	@Column(name = "english_names", columnDefinition = "text COMMENT '英文名'")
	private String englishNames;
	
	@Column(name = "spanish_names", columnDefinition = "text COMMENT '西班牙文名'")
	private String spanishNames;
	
	@Column(name = "french_names", columnDefinition = "text COMMENT '法文名'")
	private String frenchNames;
	
	@Column(name = "cites_accepted", columnDefinition = "varchar(50) COMMENT ''")
	private String citesAccepted;

	@Column(name = "all_distribution_full_names", columnDefinition = "text COMMENT '分布地全称'")
	private String allDistributionFullNames;
	
	@Column(name = "all_distribution_iso_codes", columnDefinition = "text COMMENT '分布ISO码'")
	private String allDistributionISOCodes;
	
	@Column(name = "native_distribution_full_names", columnDefinition = "text COMMENT '本土分布地全称'")
	private String nativeDistributionFullNames;

	@Column(name = "introduced_distribution", columnDefinition = "text COMMENT '引入分布地'")
	private String introducedDistribution;
	
	@Column(name = "introduced_distribution2", columnDefinition = "text COMMENT '引入分布地（？）'")
	private String introducedDistribution2;
	
	@Column(name = "reintroduced_distribution", columnDefinition = "text COMMENT '重引入分布地'")
	private String reintroducedDistribution;
	
	@Column(name = "extinct_distribution", columnDefinition = "text COMMENT '灭绝分布地'")
	private String extinctDistribution;

	@Column(name = "extinct_distribution2", columnDefinition = "text COMMENT '灭绝分布地（？）'")
	private String extinctDistribution2;

	@Column(name = "distribution_uncertain", columnDefinition = "text COMMENT '不确定分布地'")
	private String distributionUncertain;

	public Cites() {

	}

	public String getTaxonId() {
		return taxonId;
	}

	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
	}

	public String getKingdom() {
		return kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getPhylum() {
		return phylum;
	}

	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getSubspecies() {
		return subspecies;
	}

	public void setSubspecies(String subspecies) {
		this.subspecies = subspecies;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAuthorYear() {
		return authorYear;
	}

	public void setAuthorYear(String authorYear) {
		this.authorYear = authorYear;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getCurrentListing() {
		return currentListing;
	}

	public void setCurrentListing(String currentListing) {
		this.currentListing = currentListing;
	}

	public String getFullAnnotationEnglish() {
		return fullAnnotationEnglish;
	}

	public void setFullAnnotationEnglish(String fullAnnotationEnglish) {
		this.fullAnnotationEnglish = fullAnnotationEnglish;
	}

	public String getAnnotationEnglish() {
		return annotationEnglish;
	}

	public void setAnnotationEnglish(String annotationEnglish) {
		this.annotationEnglish = annotationEnglish;
	}

	public String getAnnotationSpanish() {
		return annotationSpanish;
	}

	public void setAnnotationSpanish(String annotationSpanish) {
		this.annotationSpanish = annotationSpanish;
	}

	public String getAnnotationFrench() {
		return annotationFrench;
	}

	public void setAnnotationFrench(String annotationFrench) {
		this.annotationFrench = annotationFrench;
	}

	public String getAnnotationSymbol() {
		return annotationSymbol;
	}

	public void setAnnotationSymbol(String annotationSymbol) {
		this.annotationSymbol = annotationSymbol;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getSynonymsWithAuthors() {
		return synonymsWithAuthors;
	}

	public void setSynonymsWithAuthors(String synonymsWithAuthors) {
		this.synonymsWithAuthors = synonymsWithAuthors;
	}

	public String getEnglishNames() {
		return englishNames;
	}

	public void setEnglishNames(String englishNames) {
		this.englishNames = englishNames;
	}

	public String getSpanishNames() {
		return spanishNames;
	}

	public void setSpanishNames(String spanishNames) {
		this.spanishNames = spanishNames;
	}

	public String getFrenchNames() {
		return frenchNames;
	}

	public void setFrenchNames(String frenchNames) {
		this.frenchNames = frenchNames;
	}

	public String getCitesAccepted() {
		return citesAccepted;
	}

	public void setCitesAccepted(String citesAccepted) {
		this.citesAccepted = citesAccepted;
	}

	public String getAllDistributionFullNames() {
		return allDistributionFullNames;
	}

	public void setAllDistributionFullNames(String allDistributionFullNames) {
		this.allDistributionFullNames = allDistributionFullNames;
	}

	public String getAllDistributionISOCodes() {
		return allDistributionISOCodes;
	}

	public void setAllDistributionISOCodes(String allDistributionISOCodes) {
		this.allDistributionISOCodes = allDistributionISOCodes;
	}

	public String getNativeDistributionFullNames() {
		return nativeDistributionFullNames;
	}

	public void setNativeDistributionFullNames(String nativeDistributionFullNames) {
		this.nativeDistributionFullNames = nativeDistributionFullNames;
	}

	public String getIntroducedDistribution() {
		return introducedDistribution;
	}

	public void setIntroducedDistribution(String introducedDistribution) {
		this.introducedDistribution = introducedDistribution;
	}

	public String getIntroducedDistribution2() {
		return introducedDistribution2;
	}

	public void setIntroducedDistribution2(String introducedDistribution2) {
		this.introducedDistribution2 = introducedDistribution2;
	}

	public String getReintroducedDistribution() {
		return reintroducedDistribution;
	}

	public void setReintroducedDistribution(String reintroducedDistribution) {
		this.reintroducedDistribution = reintroducedDistribution;
	}

	public String getExtinctDistribution() {
		return extinctDistribution;
	}

	public void setExtinctDistribution(String extinctDistribution) {
		this.extinctDistribution = extinctDistribution;
	}

	public String getExtinctDistribution2() {
		return extinctDistribution2;
	}

	public void setExtinctDistribution2(String extinctDistribution2) {
		this.extinctDistribution2 = extinctDistribution2;
	}

	public String getDistributionUncertain() {
		return distributionUncertain;
	}

	public void setDistributionUncertain(String distributionUncertain) {
		this.distributionUncertain = distributionUncertain;
	}

	
}