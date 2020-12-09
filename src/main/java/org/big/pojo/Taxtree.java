package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Taxtree</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Taxtree {
    private String id;
    private String treename;
    private String treeinfo;
    private String sourcesId;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Integer synchstatus;
    private Timestamp synchdate;
    private String taxtreecol;
    private String bgurl;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "treename")
    public String getTreename() {
        return treename;
    }

    public void setTreename(String treename) {
        this.treename = treename;
    }

    @Basic
    @Column(name = "treeinfo")
    public String getTreeinfo() {
        return treeinfo;
    }

    public void setTreeinfo(String treeinfo) {
        this.treeinfo = treeinfo;
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
    @Column(name = "taxtreecol")
    public String getTaxtreecol() {
        return taxtreecol;
    }

    public void setTaxtreecol(String taxtreecol) {
        this.taxtreecol = taxtreecol;
    }

    @Basic
    @Column(name = "bgurl")
    public String getBgurl() {
        return bgurl;
    }

    public void setBgurl(String bgurl) {
        this.bgurl = bgurl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Taxtree taxtree = (Taxtree) o;
        return Objects.equals(id, taxtree.id) &&
                Objects.equals(treename, taxtree.treename) &&
                Objects.equals(treeinfo, taxtree.treeinfo) &&
                Objects.equals(sourcesId, taxtree.sourcesId) &&
                Objects.equals(status, taxtree.status) &&
                Objects.equals(inputer, taxtree.inputer) &&
                Objects.equals(inputtime, taxtree.inputtime) &&
                Objects.equals(synchstatus, taxtree.synchstatus) &&
                Objects.equals(synchdate, taxtree.synchdate) &&
                Objects.equals(taxtreecol, taxtree.taxtreecol) &&
                Objects.equals(bgurl, taxtree.bgurl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, treename, treeinfo, sourcesId, status, inputer, inputtime, synchstatus, synchdate, taxtreecol, bgurl);
    }
}
