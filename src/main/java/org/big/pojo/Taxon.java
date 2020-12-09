package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Taxon</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Taxon {
    private String id;
    private String scientificname;
    private String chinesename;
    private String pinyin;
    private String type;
    private String assort;
    private String authorship;
    private String epithet;
    private String rankId;
    private String nomencode;
    private String tcid;
    private String refclassification;
    private String sourcesId;
    private String specialistId;
    private String remark;
    private String dbaseId;
    private Integer status;
    private String inputer;
    private Integer browse;
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
    @Column(name = "chinesename")
    public String getChinesename() {
        return chinesename;
    }

    public void setChinesename(String chinesename) {
        this.chinesename = chinesename;
    }

    @Basic
    @Column(name = "pinyin")
    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "assort")
    public String getAssort() {
        return assort;
    }

    public void setAssort(String assort) {
        this.assort = assort;
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
    @Column(name = "epithet")
    public String getEpithet() {
        return epithet;
    }

    public void setEpithet(String epithet) {
        this.epithet = epithet;
    }

    @Basic
    @Column(name = "rank_id")
    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    @Basic
    @Column(name = "nomencode")
    public String getNomencode() {
        return nomencode;
    }

    public void setNomencode(String nomencode) {
        this.nomencode = nomencode;
    }

    @Basic
    @Column(name = "tcid")
    public String getTcid() {
        return tcid;
    }

    public void setTcid(String tcid) {
        this.tcid = tcid;
    }

    @Basic
    @Column(name = "refclassification")
    public String getRefclassification() {
        return refclassification;
    }

    public void setRefclassification(String refclassification) {
        this.refclassification = refclassification;
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
    @Column(name = "browse")
    public Integer getBrowse() {
        return browse;
    }

    public void setBrowse(Integer browse) {
        this.browse = browse;
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
        Taxon taxon = (Taxon) o;
        return Objects.equals(id, taxon.id) &&
                Objects.equals(scientificname, taxon.scientificname) &&
                Objects.equals(chinesename, taxon.chinesename) &&
                Objects.equals(pinyin, taxon.pinyin) &&
                Objects.equals(type, taxon.type) &&
                Objects.equals(assort, taxon.assort) &&
                Objects.equals(authorship, taxon.authorship) &&
                Objects.equals(epithet, taxon.epithet) &&
                Objects.equals(rankId, taxon.rankId) &&
                Objects.equals(nomencode, taxon.nomencode) &&
                Objects.equals(tcid, taxon.tcid) &&
                Objects.equals(refclassification, taxon.refclassification) &&
                Objects.equals(sourcesId, taxon.sourcesId) &&
                Objects.equals(specialistId, taxon.specialistId) &&
                Objects.equals(remark, taxon.remark) &&
                Objects.equals(dbaseId, taxon.dbaseId) &&
                Objects.equals(status, taxon.status) &&
                Objects.equals(inputer, taxon.inputer) &&
                Objects.equals(browse, taxon.browse) &&
                Objects.equals(inputtime, taxon.inputtime) &&
                Objects.equals(synchdate, taxon.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scientificname, chinesename, pinyin, type, assort, authorship, epithet, rankId, nomencode, tcid, refclassification, sourcesId, specialistId, remark, dbaseId, status, inputer, browse, inputtime, synchdate);
    }
}
