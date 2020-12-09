package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Distributiondata</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Distributiondata {
    private String id;
    private Double lng;
    private Double lat;
    private String discontent;
    private String dismark;
    private String dismapstandard;
    private String city;
    private String county;
    private String province;
    private String locality;
    private Integer synchstatus;
    private Timestamp synchdate;
    private String sourcesId;
    private String specialistId;
    private String descriptiontypeId;
    private String descriptionId;
    private String taxonId;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private String remark;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "discontent")
    public String getDiscontent() {
        return discontent;
    }

    public void setDiscontent(String discontent) {
        this.discontent = discontent;
    }

    @Basic
    @Column(name = "dismark")
    public String getDismark() {
        return dismark;
    }

    public void setDismark(String dismark) {
        this.dismark = dismark;
    }

    @Basic
    @Column(name = "dismapstandard")
    public String getDismapstandard() {
        return dismapstandard;
    }

    public void setDismapstandard(String dismapstandard) {
        this.dismapstandard = dismapstandard;
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
    @Column(name = "province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
    @Column(name = "descriptiontype_id")
    public String getDescriptiontypeId() {
        return descriptiontypeId;
    }

    public void setDescriptiontypeId(String descriptiontypeId) {
        this.descriptiontypeId = descriptiontypeId;
    }

    @Basic
    @Column(name = "description_id")
    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
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
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distributiondata that = (Distributiondata) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(lng, that.lng) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(discontent, that.discontent) &&
                Objects.equals(dismark, that.dismark) &&
                Objects.equals(dismapstandard, that.dismapstandard) &&
                Objects.equals(city, that.city) &&
                Objects.equals(county, that.county) &&
                Objects.equals(province, that.province) &&
                Objects.equals(locality, that.locality) &&
                Objects.equals(synchstatus, that.synchstatus) &&
                Objects.equals(synchdate, that.synchdate) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(specialistId, that.specialistId) &&
                Objects.equals(descriptiontypeId, that.descriptiontypeId) &&
                Objects.equals(descriptionId, that.descriptionId) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lng, lat, discontent, dismark, dismapstandard, city, county, province, locality, synchstatus, synchdate, sourcesId, specialistId, descriptiontypeId, descriptionId, taxonId, status, inputer, inputtime, remark);
    }
}
