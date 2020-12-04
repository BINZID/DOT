package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;

/**
 *<p><b>Molecular的Entity类</b></p>
 *<p> Molecular的Entity类</p>
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
public class Molecular implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String authorinstitution;
	private String authorname;
	private String city;
	private String country;
	private String county;
	private String fastaurl;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private Double lat;
	private Double lng;
	private String locality;
	private String location;
	private String ncbiid;
	private String otherinfo;
	private String province;
	@Lob
	private String sequence;
	private String sourcesjson;
	@Column(name="spe_id")
	private String speId;
	@Column(name="spe_location")
	private String speLocation;
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private Integer synchstatus;
	private String title;
	private String type;

	@ManyToOne
	private Taxon taxon;

	public Molecular() {

	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}

}