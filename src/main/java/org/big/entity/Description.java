package org.big.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *<p><b>Description的Entity类</b></p>
 *<p> Description的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:37</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Description implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String destitle;
	private String desdate;
	private String describer;
	private String language;
	private String destypeid;
	private String descontent;
	private String remark;
	private String sourcesid;
	private String specialist;
	private String rightsholder;
	private String copyright;
	private String licenseid;

	private String relationDes;
	private String relationId;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private Integer synchstatus;

	@ManyToOne
	private Descriptiontype descriptiontype;

	@ManyToOne
	private Taxon taxon;

	public void setDescriptiontype(Descriptiontype descriptiontype) {
		this.descriptiontype = descriptiontype;
	}

	public Descriptiontype getDescriptiontype() {
		return descriptiontype;
	}

	public Description() {

	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}
}