package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Traitdata</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Traitdata {
    private String id;
    private String trainsetId;
    private String sourcesId;
    private String specialistId;
    private String taxonId;
    private String descriptionId;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Integer synchstatus;
    private Timestamp synchdate;
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
    @Column(name = "trainset_id")
    public String getTrainsetId() {
        return trainsetId;
    }

    public void setTrainsetId(String trainsetId) {
        this.trainsetId = trainsetId;
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
    @Column(name = "taxon_id")
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
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
        Traitdata traitdata = (Traitdata) o;
        return Objects.equals(id, traitdata.id) &&
                Objects.equals(trainsetId, traitdata.trainsetId) &&
                Objects.equals(sourcesId, traitdata.sourcesId) &&
                Objects.equals(specialistId, traitdata.specialistId) &&
                Objects.equals(taxonId, traitdata.taxonId) &&
                Objects.equals(descriptionId, traitdata.descriptionId) &&
                Objects.equals(status, traitdata.status) &&
                Objects.equals(inputer, traitdata.inputer) &&
                Objects.equals(inputtime, traitdata.inputtime) &&
                Objects.equals(synchstatus, traitdata.synchstatus) &&
                Objects.equals(synchdate, traitdata.synchdate) &&
                Objects.equals(remark, traitdata.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainsetId, sourcesId, specialistId, taxonId, descriptionId, status, inputer, inputtime, synchstatus, synchdate, remark);
    }
}
