package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;

/**
 *<p><b>Geoobject的Entity类</b></p>
 *<p> Geoobject的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Geoobject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private Double centerx;
	private Double centery;
	private String cngeoname;
	private String engeoname;
	private String geodata;
	private String geotype;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private String pid;
	private String relation;
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private Integer synchstatus;
	private String version;
	private String adcode;
	private String citycode;
	private String shortName;

/*	//bi-directional many-to-one association to TraitsetHasTraitontology
	@OneToMany(mappedBy="geoobject")
	private List<DistributiondataHasGeoobject> distributiondataHasGeoobjects;
*/
	@ManyToOne
	private Geogroup geogroup;

	public void setGeogroup(Geogroup geogroup) {
		this.geogroup = geogroup;
	}

	public Geogroup getGeogroup() {
		return geogroup;
	}

	public Geoobject() {

	}

}