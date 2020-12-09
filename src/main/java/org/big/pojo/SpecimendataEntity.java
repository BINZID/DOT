package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>SpecimendataEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "specimendata", schema = "dot", catalog = "")
public class SpecimendataEntity {
    private String id;
    private String city;
    private String collectdate;
    private String collector;
    private String country;
    private String county;
    private String idenby;
    private String idendate;
    private String inputer;
    private Timestamp inputtime;
    private Double lat;
    private Double lng;
    private String locality;
    private String location;
    private String mediajson;
    private String province;
    private String sex;
    private String sourcesId;
    private String specimenno;
    private String specimentype;
    private int state;
    private String storedin;
    private Timestamp synchdate;
    private Integer synchstatus;
    private String taxonId;
    private String altitude;
    private String conserveStatus;
    private String deep;
    private String descNote;
    private String host;
    private String specimenStatus;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "collectdate")
    public String getCollectdate() {
        return collectdate;
    }

    public void setCollectdate(String collectdate) {
        this.collectdate = collectdate;
    }

    @Basic
    @Column(name = "collector")
    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
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
    @Column(name = "county")
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Basic
    @Column(name = "idenby")
    public String getIdenby() {
        return idenby;
    }

    public void setIdenby(String idenby) {
        this.idenby = idenby;
    }

    @Basic
    @Column(name = "idendate")
    public String getIdendate() {
        return idendate;
    }

    public void setIdendate(String idendate) {
        this.idendate = idendate;
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
    @Column(name = "lat")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
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
    @Column(name = "locality")
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
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
    @Column(name = "mediajson")
    public String getMediajson() {
        return mediajson;
    }

    public void setMediajson(String mediajson) {
        this.mediajson = mediajson;
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
    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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
    @Column(name = "specimenno")
    public String getSpecimenno() {
        return specimenno;
    }

    public void setSpecimenno(String specimenno) {
        this.specimenno = specimenno;
    }

    @Basic
    @Column(name = "specimentype")
    public String getSpecimentype() {
        return specimentype;
    }

    public void setSpecimentype(String specimentype) {
        this.specimentype = specimentype;
    }

    @Basic
    @Column(name = "state")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "storedin")
    public String getStoredin() {
        return storedin;
    }

    public void setStoredin(String storedin) {
        this.storedin = storedin;
    }

    @Basic
    @Column(name = "synchdate")
    public Timestamp getSynchdate() {
        return synchdate;
    }

    public void setSynchdate(Timestamp synchdate) {
        this.synchdate = synchdate;
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
    @Column(name = "taxon_id")
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    @Basic
    @Column(name = "altitude")
    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    @Basic
    @Column(name = "conserve_status")
    public String getConserveStatus() {
        return conserveStatus;
    }

    public void setConserveStatus(String conserveStatus) {
        this.conserveStatus = conserveStatus;
    }

    @Basic
    @Column(name = "deep")
    public String getDeep() {
        return deep;
    }

    public void setDeep(String deep) {
        this.deep = deep;
    }

    @Basic
    @Column(name = "desc_note")
    public String getDescNote() {
        return descNote;
    }

    public void setDescNote(String descNote) {
        this.descNote = descNote;
    }

    @Basic
    @Column(name = "host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Basic
    @Column(name = "specimen_status")
    public String getSpecimenStatus() {
        return specimenStatus;
    }

    public void setSpecimenStatus(String specimenStatus) {
        this.specimenStatus = specimenStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecimendataEntity that = (SpecimendataEntity) o;
        return state == that.state &&
                Objects.equals(id, that.id) &&
                Objects.equals(city, that.city) &&
                Objects.equals(collectdate, that.collectdate) &&
                Objects.equals(collector, that.collector) &&
                Objects.equals(country, that.country) &&
                Objects.equals(county, that.county) &&
                Objects.equals(idenby, that.idenby) &&
                Objects.equals(idendate, that.idendate) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(lng, that.lng) &&
                Objects.equals(locality, that.locality) &&
                Objects.equals(location, that.location) &&
                Objects.equals(mediajson, that.mediajson) &&
                Objects.equals(province, that.province) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(specimenno, that.specimenno) &&
                Objects.equals(specimentype, that.specimentype) &&
                Objects.equals(storedin, that.storedin) &&
                Objects.equals(synchdate, that.synchdate) &&
                Objects.equals(synchstatus, that.synchstatus) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(altitude, that.altitude) &&
                Objects.equals(conserveStatus, that.conserveStatus) &&
                Objects.equals(deep, that.deep) &&
                Objects.equals(descNote, that.descNote) &&
                Objects.equals(host, that.host) &&
                Objects.equals(specimenStatus, that.specimenStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, collectdate, collector, country, county, idenby, idendate, inputer, inputtime, lat, lng, locality, location, mediajson, province, sex, sourcesId, specimenno, specimentype, state, storedin, synchdate, synchstatus, taxonId, altitude, conserveStatus, deep, descNote, host, specimenStatus);
    }
}
