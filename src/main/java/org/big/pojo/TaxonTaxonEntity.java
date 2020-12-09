package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>TaxonTaxonEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "taxon_taxon", schema = "dot", catalog = "")
public class TaxonTaxonEntity {
    private String id;
    private String taxonId;
    private String relationship;
    private String sourcesId;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Integer synchstatus;
    private Timestamp synchtime;
    private String tremark;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "relationship")
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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
    @Column(name = "synchtime")
    public Timestamp getSynchtime() {
        return synchtime;
    }

    public void setSynchtime(Timestamp synchtime) {
        this.synchtime = synchtime;
    }

    @Basic
    @Column(name = "tremark")
    public String getTremark() {
        return tremark;
    }

    public void setTremark(String tremark) {
        this.tremark = tremark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxonTaxonEntity that = (TaxonTaxonEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(relationship, that.relationship) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(synchstatus, that.synchstatus) &&
                Objects.equals(synchtime, that.synchtime) &&
                Objects.equals(tremark, that.tremark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taxonId, relationship, sourcesId, status, inputer, inputtime, synchstatus, synchtime, tremark);
    }
}
