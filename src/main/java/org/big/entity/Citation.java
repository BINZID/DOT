package org.big.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *<p><b>Citation的Entity类</b></p>
 *<p> Citation的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:14</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Citation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", columnDefinition="varchar(50) COMMENT 'UUID唯一标识'")
	private String id;
	@Column(name="sciname", columnDefinition="varchar(200) COMMENT '拉丁名'" )
	private String sciname;
	@Column(name="authorship", columnDefinition="varchar(200) COMMENT '命名信息'" )
	private String authorship;
	@Column(name="nametype", columnDefinition="varchar(50) COMMENT '名称类型'" )
	private String nametype;
	@Column(name="citationstr", columnDefinition="varchar(500) COMMENT '完整引证'" )
	private String citationstr;
	@Column(name="sources_id", columnDefinition="varchar(50) COMMENT '数据源'" )
	private String sourcesId;
	@Column(name="shortrefs", columnDefinition="varchar(500) COMMENT '短引证'" )
	private String shortrefs;
	@Column(name="inputer", columnDefinition="varchar(50) COMMENT '录入人'" )
	private String inputer;
	@Column(name="status", columnDefinition="int(11) DEFAULT 1 COMMENT '状态（默认1、可用；0、不可用）'" )
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputTime;//录入时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;//同步时间
	@Column(name="synchstatus", columnDefinition="int(11) DEFAULT 1 COMMENT '同步状态（默认1、已同步；0、未同步）'" )
	private Integer synchstatus;
	@Column(name="remark", columnDefinition="varchar(500) COMMENT '备注'" )
	private String remark;

	@ManyToOne
	private Taxon taxon;

	public Citation() {

	}

	public Taxon getTaxon() {
		return taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}
}