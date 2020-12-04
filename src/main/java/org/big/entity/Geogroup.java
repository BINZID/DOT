package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

/**
 *<p><b>Geogroup的Entity类</b></p>
 *<p> Geogroup的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:54</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Geogroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String name;
	private String remark;
	private String version;

	@OneToMany(mappedBy="geogroup")
	private List<Geoobject> geoobjects;

	public List<Geoobject> getGeoobjects() {
		return this.geoobjects;
	}

	public void setGeoobjects(List<Geoobject> geoobjects) {
		this.geoobjects = geoobjects;
	}

	public Geoobject addGeoobject(Geoobject geoobject) {
		getGeoobjects().add(geoobject);
		geoobject.setGeogroup(this);

		return geoobject;
	}

	public Geoobject removeGeoobject(Geoobject geoobject) {
		getGeoobjects().remove(geoobject);
		geoobject.setGeogroup(null);

		return geoobject;
	}

	public Geogroup() {

	}
}