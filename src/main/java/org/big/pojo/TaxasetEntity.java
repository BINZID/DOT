package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>TaxasetEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "taxaset", schema = "dot", catalog = "")
public class TaxasetEntity {
    private String id;
    private String tsname;
    private String tsinfo;
    private Integer status;
    private Integer synchstatus;
    private Timestamp synchdate;
    private Timestamp inputtime;
    private String inputer;
    private String bgurl;
    private String specialistId;
    private String licenseId;
    private String sourcesId;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tsname")
    public String getTsname() {
        return tsname;
    }

    public void setTsname(String tsname) {
        this.tsname = tsname;
    }

    @Basic
    @Column(name = "tsinfo")
    public String getTsinfo() {
        return tsinfo;
    }

    public void setTsinfo(String tsinfo) {
        this.tsinfo = tsinfo;
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
    @Column(name = "inputtime")
    public Timestamp getInputtime() {
        return inputtime;
    }

    public void setInputtime(Timestamp inputtime) {
        this.inputtime = inputtime;
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
    @Column(name = "bgurl")
    public String getBgurl() {
        return bgurl;
    }

    public void setBgurl(String bgurl) {
        this.bgurl = bgurl;
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
    @Column(name = "license_id")
    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    @Basic
    @Column(name = "sources_id")
    public String getSourcesId() {
        return sourcesId;
    }

    public void setSourcesId(String sourcesId) {
        this.sourcesId = sourcesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxasetEntity that = (TaxasetEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tsname, that.tsname) &&
                Objects.equals(tsinfo, that.tsinfo) &&
                Objects.equals(status, that.status) &&
                Objects.equals(synchstatus, that.synchstatus) &&
                Objects.equals(synchdate, that.synchdate) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(bgurl, that.bgurl) &&
                Objects.equals(specialistId, that.specialistId) &&
                Objects.equals(licenseId, that.licenseId) &&
                Objects.equals(sourcesId, that.sourcesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tsname, tsinfo, status, synchstatus, synchdate, inputtime, inputer, bgurl, specialistId, licenseId, sourcesId);
    }
}
