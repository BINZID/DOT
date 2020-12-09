package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Traitproperty</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Traitproperty {
    private String id;
    private String cnterm;
    private String definition;
    private String enterm;
    private String inputer;
    private Timestamp inputtime;
    private String sourcesId;
    private int status;
    private Timestamp synchdate;
    private Integer synchstatus;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "enterm")
    public String getEnterm() {
        return enterm;
    }

    public void setEnterm(String enterm) {
        this.enterm = enterm;
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
    @Column(name = "sources_id")
    public String getSourcesId() {
        return sourcesId;
    }

    public void setSourcesId(String sourcesId) {
        this.sourcesId = sourcesId;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Traitproperty that = (Traitproperty) o;
        return status == that.status &&
                Objects.equals(id, that.id) &&
                Objects.equals(cnterm, that.cnterm) &&
                Objects.equals(definition, that.definition) &&
                Objects.equals(enterm, that.enterm) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(synchdate, that.synchdate) &&
                Objects.equals(synchstatus, that.synchstatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cnterm, definition, enterm, inputer, inputtime, sourcesId, status, synchdate, synchstatus);
    }
}
