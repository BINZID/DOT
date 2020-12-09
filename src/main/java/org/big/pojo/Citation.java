package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Citation</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Citation {
    private String id;
    private String scientificname;
    private String namestatus;
    private String authorship;
    private String citationstr;
    private String shortrefs;
    private String sourcesId;
    private String specialistId;
    private String taxonId;
    private String remark;
    private String dbaseId;
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
    @Column(name = "scientificname")
    public String getScientificname() {
        return scientificname;
    }

    public void setScientificname(String scientificname) {
        this.scientificname = scientificname;
    }

    @Basic
    @Column(name = "namestatus")
    public String getNamestatus() {
        return namestatus;
    }

    public void setNamestatus(String namestatus) {
        this.namestatus = namestatus;
    }

    @Basic
    @Column(name = "authorship")
    public String getAuthorship() {
        return authorship;
    }

    public void setAuthorship(String authorship) {
        this.authorship = authorship;
    }

    @Basic
    @Column(name = "citationstr")
    public String getCitationstr() {
        return citationstr;
    }

    public void setCitationstr(String citationstr) {
        this.citationstr = citationstr;
    }

    @Basic
    @Column(name = "shortrefs")
    public String getShortrefs() {
        return shortrefs;
    }

    public void setShortrefs(String shortrefs) {
        this.shortrefs = shortrefs;
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
        Citation citation = (Citation) o;
        return Objects.equals(id, citation.id) &&
                Objects.equals(scientificname, citation.scientificname) &&
                Objects.equals(namestatus, citation.namestatus) &&
                Objects.equals(authorship, citation.authorship) &&
                Objects.equals(citationstr, citation.citationstr) &&
                Objects.equals(shortrefs, citation.shortrefs) &&
                Objects.equals(sourcesId, citation.sourcesId) &&
                Objects.equals(specialistId, citation.specialistId) &&
                Objects.equals(taxonId, citation.taxonId) &&
                Objects.equals(remark, citation.remark) &&
                Objects.equals(dbaseId, citation.dbaseId) &&
                Objects.equals(status, citation.status) &&
                Objects.equals(inputer, citation.inputer) &&
                Objects.equals(inputtime, citation.inputtime) &&
                Objects.equals(synchdate, citation.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scientificname, namestatus, authorship, citationstr, shortrefs, sourcesId, specialistId, taxonId, remark, dbaseId, status, inputer, inputtime, synchdate);
    }
}
