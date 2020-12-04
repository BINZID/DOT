package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


/**
 *<p><b>Occurrence的Entity类</b></p>
 *<p> Occurrence的Entity类</p>
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
public class Occurrence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String city;
	private String country;
	private String county;
	private String eventdate;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private double lat;
	private double lng;
	private String locality;
	private String location;
	private String province;
	private String sourcesid;
	private int status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;
	@Column(name="taxon_rank_id")
	private String taxonRankId;
	@Column(name="taxon_taxaset_id")
	private String taxonTaxasetId;
	private String type;

	@ManyToOne
	private Taxon taxon;
	
	private String title;

	public Occurrence() {
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}
}