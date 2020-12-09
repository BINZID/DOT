package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>MultimediaEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "multimedia", schema = "dot", catalog = "")
public class MultimediaEntity {
    private String id;
    private String title;
    private String mediatype;
    private String medialabel;
    private String mediainfo;
    private String path;
    private String originurl;
    private String copyright;
    private String rightsholder;
    private String country;
    private String province;
    private String city;
    private String county;
    private String locality;
    private Double lng;
    private Double lat;
    private String location;
    private String announcer;
    private String contributor;
    private String desid;
    private String lisenceId;
    private String sourcesId;
    private String specialistId;
    private String remark;
    private String dbaseId;
    private String taxonId;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Timestamp synchdate;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "mediatype")
    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    @Basic
    @Column(name = "medialabel")
    public String getMedialabel() {
        return medialabel;
    }

    public void setMedialabel(String medialabel) {
        this.medialabel = medialabel;
    }

    @Basic
    @Column(name = "mediainfo")
    public String getMediainfo() {
        return mediainfo;
    }

    public void setMediainfo(String mediainfo) {
        this.mediainfo = mediainfo;
    }

    @Basic
    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic
    @Column(name = "originurl")
    public String getOriginurl() {
        return originurl;
    }

    public void setOriginurl(String originurl) {
        this.originurl = originurl;
    }

    @Basic
    @Column(name = "copyright")
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Basic
    @Column(name = "rightsholder")
    public String getRightsholder() {
        return rightsholder;
    }

    public void setRightsholder(String rightsholder) {
        this.rightsholder = rightsholder;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "county")
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Basic
    @Column(name = "locality")
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Basic
    @Column(name = "lng")
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Basic
    @Column(name = "lat")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "announcer")
    public String getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(String announcer) {
        this.announcer = announcer;
    }

    @Basic
    @Column(name = "contributor")
    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    @Basic
    @Column(name = "desid")
    public String getDesid() {
        return desid;
    }

    public void setDesid(String desid) {
        this.desid = desid;
    }

    @Basic
    @Column(name = "lisence_id")
    public String getLisenceId() {
        return lisenceId;
    }

    public void setLisenceId(String lisenceId) {
        this.lisenceId = lisenceId;
    }

    @Basic
    @Column(name = "sources_id")
    public String getSourcesId() {
        return sourcesId;
    }

    public void setSourcesId(String sourcesId) {
        this.sourcesId = sourcesId;
    }

    @Basic
    @Column(name = "specialist_id")
    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "dbase_id")
    public String getDbaseId() {
        return dbaseId;
    }

    public void setDbaseId(String dbaseId) {
        this.dbaseId = dbaseId;
    }

    @Basic
    @Column(name = "taxon_id")
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "inputer")
    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer;
    }

    @Basic
    @Column(name = "inputtime")
    public Timestamp getInputtime() {
        return inputtime;
    }

    public void setInputtime(Timestamp inputtime) {
        this.inputtime = inputtime;
    }

    @Basic
    @Column(name = "synchdate")
    public Timestamp getSynchdate() {
        return synchdate;
    }

    public void setSynchdate(Timestamp synchdate) {
        this.synchdate = synchdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultimediaEntity that = (MultimediaEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(mediatype, that.mediatype) &&
                Objects.equals(medialabel, that.medialabel) &&
                Objects.equals(mediainfo, that.mediainfo) &&
                Objects.equals(path, that.path) &&
                Objects.equals(originurl, that.originurl) &&
                Objects.equals(copyright, that.copyright) &&
                Objects.equals(rightsholder, that.rightsholder) &&
                Objects.equals(country, that.country) &&
                Objects.equals(province, that.province) &&
                Objects.equals(city, that.city) &&
                Objects.equals(county, that.county) &&
                Objects.equals(locality, that.locality) &&
                Objects.equals(lng, that.lng) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(location, that.location) &&
                Objects.equals(announcer, that.announcer) &&
                Objects.equals(contributor, that.contributor) &&
                Objects.equals(desid, that.desid) &&
                Objects.equals(lisenceId, that.lisenceId) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(specialistId, that.specialistId) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(dbaseId, that.dbaseId) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(synchdate, that.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, mediatype, medialabel, mediainfo, path, originurl, copyright, rightsholder, country, province, city, county, locality, lng, lat, location, announcer, contributor, desid, lisenceId, sourcesId, specialistId, remark, dbaseId, taxonId, status, inputer, inputtime, synchdate);
    }
}
