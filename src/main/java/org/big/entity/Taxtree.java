package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Taxtree的Entity类</b></p>
 *<p> Taxtree的Entity类</p>
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
@Table(name="taxtree", schema = "biodata")
public class Taxtree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	private String sourcejson;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String taxtreecol;

	private String treeinfo;

	private String treename;

	private String bgurl;
	@ManyToMany
	@JoinTable(
		name="taxon_has_taxtree"
		, joinColumns={
			@JoinColumn(name="taxtree_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="taxon_id")
			}
		)
	private List<Taxon> taxons;

	public Taxtree() {
	}
}