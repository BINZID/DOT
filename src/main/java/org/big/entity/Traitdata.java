package org.big.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 *<p><b>Traitdata的Entity类</b></p>
 *<p> Traitdata的Entity类</p>
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
@Table(name="traitdata", schema = "biodata")
public class Traitdata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String traitjson;	// 术语、属性、属性值、单位、测量依据
	private String refjson;
	private String sourcesId;
	private String expert;

	private String desid;		// 描述
	private int status;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String trainsetid;
	
	private String remark;

	@ManyToOne
	private Taxon taxon;

	public Traitdata() {
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}
}