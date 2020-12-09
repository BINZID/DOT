package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Molecular</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Molecular {
    private String id;
    private String speId;
    private String authorname;
    private String authorinstitution;
    private String fastaurl;
    private String title;
    private String sequence;
    private String type;
    private String location;
    private String speLocation;
    private String country;
    private String province;
    private String city;
    private String county;
    private String locality;
    private Double lng;
    private Double lat;
    private String otherinfo;
    private String ncbiid;
    private String sourcesId;
    private String taxonId;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Integer synchstatus;
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
    @Column(name = "spe_id")
    public String getSpeId() {
        return speId;
    }

    public void setSpeId(String speId) {
        this.speId = speId;
    }

    @Basic
    @Column(name = "authorname")
    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    @Basic
    @Column(name = "authorinstitution")
    public String getAuthorinstitution() {
        return authorinstitution;
    }

    public void setAuthorinstitution(String authorinstitution) {
        this.authorinstitution = authorinstitution;
    }

    @Basic
    @Column(name = "fastaurl")
    public String getFastaurl() {
        return fastaurl;
    }

    public void setFastaurl(String fastaurl) {
        this.fastaurl = fastaurl;
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
    @Column(name = "sequence")
    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    @Column(name = "spe_location")
    public String getSpeLocation() {
        return speLocation;
    }

    public void setSpeLocation(String speLocation) {
        this.speLocation = speLocation;
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
    @Column(name = "otherinfo")
    public String getOtherinfo() {
        return otherinfo;
    }

    public void setOtherinfo(String otherinfo) {
        this.otherinfo = otherinfo;
    }

    @Basic
    @Column(name = "ncbiid")
    public String getNcbiid() {
        return ncbiid;
    }

    public void setNcbiid(String ncbiid) {
        this.ncbiid = ncbiid;
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
    @Column(name = "synchstatus")
    public Integer getSynchstatus() {
        return synchstatus;
    }

    public void setSynchstatus(Integer synchstatus) {
        this.synchstatus = synchstatus;
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
        Molecular molecular = (Molecular) o;
        return Objects.equals(id, molecular.id) &&
                Objects.equals(speId, molecular.speId) &&
                Objects.equals(authorname, molecular.authorname) &&
                Objects.equals(authorinstitution, molecular.authorinstitution) &&
                Objects.equals(fastaurl, molecular.fastaurl) &&
                Objects.equals(title, molecular.title) &&
                Objects.equals(sequence, molecular.sequence) &&
                Objects.equals(type, molecular.type) &&
                Objects.equals(location, molecular.location) &&
                Objects.equals(speLocation, molecular.speLocation) &&
                Objects.equals(country, molecular.country) &&
                Objects.equals(province, molecular.province) &&
                Objects.equals(city, molecular.city) &&
                Objects.equals(county, molecular.county) &&
                Objects.equals(locality, molecular.locality) &&
                Objects.equals(lng, molecular.lng) &&
                Objects.equals(lat, molecular.lat) &&
                Objects.equals(otherinfo, molecular.otherinfo) &&
                Objects.equals(ncbiid, molecular.ncbiid) &&
                Objects.equals(sourcesId, molecular.sourcesId) &&
                Objects.equals(taxonId, molecular.taxonId) &&
                Objects.equals(status, molecular.status) &&
                Objects.equals(inputer, molecular.inputer) &&
                Objects.equals(inputtime, molecular.inputtime) &&
                Objects.equals(synchstatus, molecular.synchstatus) &&
                Objects.equals(synchdate, molecular.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, speId, authorname, authorinstitution, fastaurl, title, sequence, type, location, speLocation, country, province, city, county, locality, lng, lat, otherinfo, ncbiid, sourcesId, taxonId, status, inputer, inputtime, synchstatus, synchdate);
    }
}
