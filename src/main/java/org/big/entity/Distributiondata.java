package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 *<p><b>Distributiondata的Entity类</b></p>
 *<p> Distributiondata的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "distributiondata")
public class Distributiondata implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String geojson;
	
	private String dismark;			// 地理标识
	private String dismapstandard;	// 地图类型
	private String distype ;		// 分布类型
	
	private String province;
	private String city;
	private String county;
	private String locality;
	
	private Double lat;
	private Double lng;
	
	private String sourcesid;
	private String refjson;
	private String expert;			// 审核专家
	
	private String discontent;
	private String taxonid;
	
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private Integer synchstatus;
	
	private String remark;
	
	private String descid;
	
	public String getDescid() {
		return descid;
	}

	public void setDescid(String descid) {
		this.descid = descid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getDiscontent() {
		return discontent;
	}

	public void setDiscontent(String discontent) {
		this.discontent = discontent;
	}
	
/*	//bi-directional many-to-one association to TraitsetHasTraitontology
		@OneToMany(mappedBy="distributiondata")
		private List<DistributiondataHasGeoobject> distributiondataHasGeoobjects;*/

	public String getDismark() {
		return dismark;
	}

	public void setDismark(String dismark) {
		this.dismark = dismark;
	}

	public String getDismapstandard() {
		return dismapstandard;
	}

	public void setDismapstandard(String dismapstandard) {
		this.dismapstandard = dismapstandard;
	}

	public String getDistype() {
		return distype;
	}

	public void setDistype(String distype) {
		this.distype = distype;
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public String getGeojson() {
		return geojson;
	}

	public void setGeojson(String geojson) {
		this.geojson = geojson;
	}

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;

	public Distributiondata() {
	}
	
	/*分布模板1*/
	public Distributiondata(String geojson, String dismark, String dismapstandard, String distype, String sourcesid,
			String refjson, String expert, Taxon taxon) {
		super();
		this.geojson = geojson;
		this.dismark = dismark;
		this.dismapstandard = dismapstandard;
		this.distype = distype;
		this.sourcesid = sourcesid;
		this.refjson = refjson;
		this.expert = expert;
		this.taxon = taxon;
	}
	
	/*分布模板2*/
	public Distributiondata(String province, String city, String county, String locality, Double lat,
			Double lng, String refjson, String sourcesid, String expert, Taxon taxon) {
		super();
		this.province = province;
		this.city = city;
		this.county = county;
		this.locality = locality;
		this.lat = lat;
		this.lng = lng;
		this.refjson = refjson;
		this.sourcesid = sourcesid;
		this.expert = expert;
		this.taxon = taxon;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}


	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getRefjson() {
		return refjson;
	}

	public void setSynchstatus(Integer synchstatus) {
		this.synchstatus = synchstatus;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
	}

	public String getSourcesid() {
		return sourcesid;
	}

	public void setSourcesid(String sourcesid) {
		this.sourcesid = sourcesid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getSynchdate() {
		return synchdate;
	}

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
	}

	public int getSynchstatus() {
		return synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	public String getTaxonid() {
		return taxonid;
	}

	public void setTaxonid(String taxonid) {
		this.taxonid = taxonid;
	}


	public Taxon getTaxon() {
		return taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((county == null) ? 0 : county.hashCode());
		result = prime * result + ((descid == null) ? 0 : descid.hashCode());
		result = prime * result + ((discontent == null) ? 0 : discontent.hashCode());
		result = prime * result + ((dismapstandard == null) ? 0 : dismapstandard.hashCode());
		result = prime * result + ((dismark == null) ? 0 : dismark.hashCode());
		result = prime * result + ((distype == null) ? 0 : distype.hashCode());
		result = prime * result + ((expert == null) ? 0 : expert.hashCode());
		result = prime * result + ((geojson == null) ? 0 : geojson.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((lat == null) ? 0 : lat.hashCode());
		result = prime * result + ((lng == null) ? 0 : lng.hashCode());
		result = prime * result + ((locality == null) ? 0 : locality.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + ((synchstatus == null) ? 0 : synchstatus.hashCode());
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
		result = prime * result + ((taxonid == null) ? 0 : taxonid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Distributiondata other = (Distributiondata) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (county == null) {
			if (other.county != null)
				return false;
		} else if (!county.equals(other.county))
			return false;
		if (descid == null) {
			if (other.descid != null)
				return false;
		} else if (!descid.equals(other.descid))
			return false;
		if (discontent == null) {
			if (other.discontent != null)
				return false;
		} else if (!discontent.equals(other.discontent))
			return false;
		if (dismapstandard == null) {
			if (other.dismapstandard != null)
				return false;
		} else if (!dismapstandard.equals(other.dismapstandard))
			return false;
		if (dismark == null) {
			if (other.dismark != null)
				return false;
		} else if (!dismark.equals(other.dismark))
			return false;
		if (distype == null) {
			if (other.distype != null)
				return false;
		} else if (!distype.equals(other.distype))
			return false;
		if (expert == null) {
			if (other.expert != null)
				return false;
		} else if (!expert.equals(other.expert))
			return false;
		if (geojson == null) {
			if (other.geojson != null)
				return false;
		} else if (!geojson.equals(other.geojson))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inputer == null) {
			if (other.inputer != null)
				return false;
		} else if (!inputer.equals(other.inputer))
			return false;
		if (inputtime == null) {
			if (other.inputtime != null)
				return false;
		} else if (!inputtime.equals(other.inputtime))
			return false;
		if (lat == null) {
			if (other.lat != null)
				return false;
		} else if (!lat.equals(other.lat))
			return false;
		if (lng == null) {
			if (other.lng != null)
				return false;
		} else if (!lng.equals(other.lng))
			return false;
		if (locality == null) {
			if (other.locality != null)
				return false;
		} else if (!locality.equals(other.locality))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (refjson == null) {
			if (other.refjson != null)
				return false;
		} else if (!refjson.equals(other.refjson))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (sourcesid == null) {
			if (other.sourcesid != null)
				return false;
		} else if (!sourcesid.equals(other.sourcesid))
			return false;
		if (status != other.status)
			return false;
		if (synchdate == null) {
			if (other.synchdate != null)
				return false;
		} else if (!synchdate.equals(other.synchdate))
			return false;
		if (synchstatus == null) {
			if (other.synchstatus != null)
				return false;
		} else if (!synchstatus.equals(other.synchstatus))
			return false;
		if (taxon == null) {
			if (other.taxon != null)
				return false;
		} else if (!taxon.equals(other.taxon))
			return false;
		if (taxonid == null) {
			if (other.taxonid != null)
				return false;
		} else if (!taxonid.equals(other.taxonid))
			return false;
		return true;
	}
	
}