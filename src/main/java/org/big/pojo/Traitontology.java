package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Traitontology</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Traitontology {
    private String id;
    private String enterm;
    private String cnterm;
    private String definition;
    private String synonymys;
    private String sourcesId;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Integer synchstatus;
    private Timestamp synchdate;
    private String catalog1;
    private String catalog2;
    private String catalog3;
    private String groups;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "enterm")
    public String getEnterm() {
        return enterm;
    }

    public void setEnterm(String enterm) {
        this.enterm = enterm;
    }

    @Basic
    @Column(name = "cnterm")
    public String getCnterm() {
        return cnterm;
    }

    public void setCnterm(String cnterm) {
        this.cnterm = cnterm;
    }

    @Basic
    @Column(name = "definition")
    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Basic
    @Column(name = "synonymys")
    public String getSynonymys() {
        return synonymys;
    }

    public void setSynonymys(String synonymys) {
        this.synonymys = synonymys;
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
    @Column(name = "synchdate")
    public Timestamp getSynchdate() {
        return synchdate;
    }

    public void setSynchdate(Timestamp synchdate) {
        this.synchdate = synchdate;
    }

    @Basic
    @Column(name = "catalog1")
    public String getCatalog1() {
        return catalog1;
    }

    public void setCatalog1(String catalog1) {
        this.catalog1 = catalog1;
    }

    @Basic
    @Column(name = "catalog2")
    public String getCatalog2() {
        return catalog2;
    }

    public void setCatalog2(String catalog2) {
        this.catalog2 = catalog2;
    }

    @Basic
    @Column(name = "catalog3")
    public String getCatalog3() {
        return catalog3;
    }

    public void setCatalog3(String catalog3) {
        this.catalog3 = catalog3;
    }

    @Basic
    @Column(name = "groups")
    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Traitontology that = (Traitontology) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(enterm, that.enterm) &&
                Objects.equals(cnterm, that.cnterm) &&
                Objects.equals(definition, that.definition) &&
                Objects.equals(synonymys, that.synonymys) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(synchstatus, that.synchstatus) &&
                Objects.equals(synchdate, that.synchdate) &&
                Objects.equals(catalog1, that.catalog1) &&
                Objects.equals(catalog2, that.catalog2) &&
                Objects.equals(catalog3, that.catalog3) &&
                Objects.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterm, cnterm, definition, synonymys, sourcesId, status, inputer, inputtime, synchstatus, synchdate, catalog1, catalog2, catalog3, groups);
    }
}
