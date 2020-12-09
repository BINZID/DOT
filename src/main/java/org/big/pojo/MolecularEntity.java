package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>MolecularEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "molecular", schema = "dot", catalog = "")
public class MolecularEntity {
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
        MolecularEntity that = (MolecularEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(speId, that.speId) &&
                Objects.equals(authorname, that.authorname) &&
                Objects.equals(authorinstitution, that.authorinstitution) &&
                Objects.equals(fastaurl, that.fastaurl) &&
                Objects.equals(title, that.title) &&
                Objects.equals(sequence, that.sequence) &&
                Objects.equals(type, that.type) &&
                Objects.equals(location, that.location) &&
                Objects.equals(speLocation, that.speLocation) &&
                Objects.equals(country, that.country) &&
                Objects.equals(province, that.province) &&
                Objects.equals(city, that.city) &&
                Objects.equals(county, that.county) &&
                Objects.equals(locality, that.locality) &&
                Objects.equals(lng, that.lng) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(otherinfo, that.otherinfo) &&
                Objects.equals(ncbiid, that.ncbiid) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(synchstatus, that.synchstatus) &&
                Objects.equals(synchdate, that.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, speId, authorname, authorinstitution, fastaurl, title, sequence, type, location, speLocation, country, province, city, county, locality, lng, lat, otherinfo, ncbiid, sourcesId, taxonId, status, inputer, inputtime, synchstatus, synchdate);
    }
}
