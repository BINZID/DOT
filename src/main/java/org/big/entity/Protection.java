package org.big.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 *<p><b>Protection的Entity类</b></p>
 *<p> Protection的Entity类</p>
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
public class Protection implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String protlevel;
	private String proassessment;
	private String sourcesid;
	private String specialistId;
	private Integer status;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private Integer synchstatus;
	private String remark;

	@ManyToOne
	private Protectstandard protectstandard;

	@ManyToOne
	private Taxon taxon;

	public Protection() {
	}
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}
}