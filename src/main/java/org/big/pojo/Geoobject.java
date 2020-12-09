package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Geoobject</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Geoobject {
    private String id;
    private String cngeoname;
    private String engeoname;
    private String shortName;
    private String pid;
    private String geotype;
    private String version;
    private Double centerx;
    private Double centery;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Integer synchstatus;
    private Timestamp synchdate;
    private String adcode;
    private String citycode;
    private String geogroupId;
    private String remark;
    private String geodata;
    private String relation;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "cngeoname")
    public String getCngeoname() {
        return cngeoname;
    }

    public void setCngeoname(String cngeoname) {
        this.cngeoname = cngeoname;
    }

    @Basic
    @Column(name = "engeoname")
    public String getEngeoname() {
        return engeoname;
    }

    public void setEngeoname(String engeoname) {
        this.engeoname = engeoname;
    }

    @Basic
    @Column(name = "short_name")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Basic
    @Column(name = "pid")
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "geotype")
    public String getGeotype() {
        return geotype;
    }

    public void setGeotype(String geotype) {
        this.geotype = geotype;
    }

    @Basic
    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Basic
    @Column(name = "centerx")
    public Double getCenterx() {
        return centerx;
    }

    public void setCenterx(Double centerx) {
        this.centerx = centerx;
    }

    @Basic
    @Column(name = "centery")
    public Double getCentery() {
        return centery;
    }

    public void setCentery(Double centery) {
        this.centery = centery;
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

    @Basic
    @Column(name = "adcode")
    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    @Basic
    @Column(name = "citycode")
    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    @Basic
    @Column(name = "geogroup_id")
    public String getGeogroupId() {
        return geogroupId;
    }

    public void setGeogroupId(String geogroupId) {
        this.geogroupId = geogroupId;
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
    @Column(name = "geodata")
    public String getGeodata() {
        return geodata;
    }

    public void setGeodata(String geodata) {
        this.geodata = geodata;
    }

    @Basic
    @Column(name = "relation")
    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geoobject geoobject = (Geoobject) o;
        return Objects.equals(id, geoobject.id) &&
                Objects.equals(cngeoname, geoobject.cngeoname) &&
                Objects.equals(engeoname, geoobject.engeoname) &&
                Objects.equals(shortName, geoobject.shortName) &&
                Objects.equals(pid, geoobject.pid) &&
                Objects.equals(geotype, geoobject.geotype) &&
                Objects.equals(version, geoobject.version) &&
                Objects.equals(centerx, geoobject.centerx) &&
                Objects.equals(centery, geoobject.centery) &&
                Objects.equals(status, geoobject.status) &&
                Objects.equals(inputer, geoobject.inputer) &&
                Objects.equals(inputtime, geoobject.inputtime) &&
                Objects.equals(synchstatus, geoobject.synchstatus) &&
                Objects.equals(synchdate, geoobject.synchdate) &&
                Objects.equals(adcode, geoobject.adcode) &&
                Objects.equals(citycode, geoobject.citycode) &&
                Objects.equals(geogroupId, geoobject.geogroupId) &&
                Objects.equals(remark, geoobject.remark) &&
                Objects.equals(geodata, geoobject.geodata) &&
                Objects.equals(relation, geoobject.relation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cngeoname, engeoname, shortName, pid, geotype, version, centerx, centery, status, inputer, inputtime, synchstatus, synchdate, adcode, citycode, geogroupId, remark, geodata, relation);
    }
}
