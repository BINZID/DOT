package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>ProtectionEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "protection", schema = "dot", catalog = "")
public class ProtectionEntity {
    private String id;
    private String proassessment;
    private String sourcesId;
    private String specialistId;
    private String remark;
    private String dbaseId;
    private String protectstandardId;
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
    @Column(name = "proassessment")
    public String getProassessment() {
        return proassessment;
    }

    public void setProassessment(String proassessment) {
        this.proassessment = proassessment;
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
    @Column(name = "protectstandard_id")
    public String getProtectstandardId() {
        return protectstandardId;
    }

    public void setProtectstandardId(String protectstandardId) {
        this.protectstandardId = protectstandardId;
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
        ProtectionEntity that = (ProtectionEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(proassessment, that.proassessment) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(specialistId, that.specialistId) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(dbaseId, that.dbaseId) &&
                Objects.equals(protectstandardId, that.protectstandardId) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(synchdate, that.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, proassessment, sourcesId, specialistId, remark, dbaseId, protectstandardId, taxonId, status, inputer, inputtime, synchdate);
    }
}
