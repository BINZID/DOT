package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Taxaset的Entity类</b></p>
 *<p> Taxaset的Entity类</p>
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
public class Taxaset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String sourceid;
	private String tsinfo;
	private String tsname;
	private String bgurl;
	private String specialistId;
	private String license;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private String inputer;
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private Integer synchstatus;
	@OneToMany(mappedBy = "taxaset")
	private List<Taxon> taxons;

	public Taxaset() {

	}
}