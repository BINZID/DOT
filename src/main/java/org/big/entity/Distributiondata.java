package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 *<p><b>Distributiondata的Entity类</b></p>
 *<p> Distributiondata的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Distributiondata implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String dismark;
	private String dismapstandard;
	private String distype ;
	private String province;
	private String city;
	private String county;
	private String locality;
	private Double lat;
	private Double lng;
	private String sourcesid;
	private String expert;
	private String discontent;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private Integer synchstatus;
	private String remark;
	private String descid;
	
/*	//bi-directional many-to-one association to TraitsetHasTraitontology
		@OneToMany(mappedBy="distributiondata")
		private List<DistributiondataHasGeoobject> distributiondataHasGeoobjects;*/

	@ManyToOne
	private Taxon taxon;

	public Distributiondata() {

	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}

}