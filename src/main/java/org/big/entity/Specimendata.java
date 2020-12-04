package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;

/**
 *<p><b>Specimendata的Entity类</b></p>
 *<p> Specimendata的Entity类</p>
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
public class Specimendata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String city;
	private String collectdate;
	private String collector;
	private String country;
	private String county;
	private String idenby;
	private String idendate;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private double lat;
	private double lng;
	private String locality;
	private String location;
	private String mediajson;
	private String province;
	private String sex;
	private String sourcesid;
	private String specimenno;
	private String specimentype;
	private int state;
	private String storedin;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;
	private String taxonid;
	@ManyToOne
	private Taxon taxon;

	public Specimendata() {
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}
}