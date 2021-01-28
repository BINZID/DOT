package org.big.zoology.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author HM
 * @since 2019-10-18
 */
public class Multimedia implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 标题
	 */
	private String title;

	private String mediatype;

	/**
	 * 图注
	 */
	private String medialabel;

	/**
	 * 简介
	 */
	private String mediainfo;

	/**
	 * 存储路径
	 */
	private String path;

	/**
	 * 原始路径
	 */
	private String originurl;

	/**
	 * 版权声明
	 */
	private String copyright;

	/**
	 * 版权人
	 */
	private String rightsholder;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 市
	 */
	private String city;

	/**
	 * 县
	 */
	private String county;

	/**
	 * 小地点
	 */
	private String locality;

	/**
	 * 经度
	 */
	private Double lng;

	/**
	 * 纬度
	 */
	private Double lat;

	/**
	 * 详细地点
	 */
	private String location;

	/**
	 * 发布人
	 */
	private String announcer;

	/**
	 * 贡献人
	 */
	private String contributor;

	/**
	 * 描述的ID，及该记录来源于哪个描述，可以为空
	 */
	private String desid;

	private String lisenceId;

	/**
	 * 数据源ID
	 */
	private String sourcesId;

	/**
	 * 审核专家
	 */
	private String specialistId;

	/**
	 * 参考文献
	 */
	private String referencejson;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 数据库
	 */
	private String dbaseId;

	private String taxonId;

	/**
	 * 状态（默认1、可用；0、不可用）
	 */
	private Integer status;

	/**
	 * 录入人
	 */
	private String inputer;

	/**
	 * 录入时间
	 */
	private Date inputtime;

	/**
	 * 更新时间
	 */
	private Date synchdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMediatype() {
		return mediatype;
	}

	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}

	public String getMedialabel() {
		return medialabel;
	}

	public void setMedialabel(String medialabel) {
		this.medialabel = medialabel;
	}

	public String getMediainfo() {
		return mediainfo;
	}

	public void setMediainfo(String mediainfo) {
		this.mediainfo = mediainfo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOriginurl() {
		return originurl;
	}

	public void setOriginurl(String originurl) {
		this.originurl = originurl;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getRightsholder() {
		return rightsholder;
	}

	public void setRightsholder(String rightsholder) {
		this.rightsholder = rightsholder;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAnnouncer() {
		return announcer;
	}

	public void setAnnouncer(String announcer) {
		this.announcer = announcer;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public String getDesid() {
		return desid;
	}

	public void setDesid(String desid) {
		this.desid = desid;
	}

	public String getLisenceId() {
		return lisenceId;
	}

	public void setLisenceId(String lisenceId) {
		this.lisenceId = lisenceId;
	}

	public String getSourcesId() {
		return sourcesId;
	}

	public void setSourcesId(String sourcesId) {
		this.sourcesId = sourcesId;
	}

	public String getSpecialistId() {
		return specialistId;
	}

	public void setSpecialistId(String specialistId) {
		this.specialistId = specialistId;
	}

	public String getReferencejson() {
		return referencejson;
	}

	public void setReferencejson(String referencejson) {
		this.referencejson = referencejson;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDbaseId() {
		return dbaseId;
	}

	public void setDbaseId(String dbaseId) {
		this.dbaseId = dbaseId;
	}

	public String getTaxonId() {
		return taxonId;
	}

	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Date getSynchdate() {
		return synchdate;
	}

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
	}

}
