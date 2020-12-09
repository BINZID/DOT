package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>DescriptionEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:10
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "description", schema = "dot", catalog = "")
public class DescriptionEntity {
    private String id;
    private String destitle;
    private String descontent;
    private String language;
    private String licenseId;
    private String sourcesId;
    private String specialistId;
    private String describer;
    private Timestamp desdate;
    private String copyright;
    private String rightsholder;
    private String descriptiontypeId;
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
    @Column(name = "destitle")
    public String getDestitle() {
        return destitle;
    }

    public void setDestitle(String destitle) {
        this.destitle = destitle;
    }

    @Basic
    @Column(name = "descontent")
    public String getDescontent() {
        return descontent;
    }

    public void setDescontent(String descontent) {
        this.descontent = descontent;
    }

    @Basic
    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    @Basic
    @Column(name = "specialist_id")
    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    @Basic
    @Column(name = "describer")
    public String getDescriber() {
        return describer;
    }

    public void setDescriber(String describer) {
        this.describer = describer;
    }

    @Basic
    @Column(name = "desdate")
    public Timestamp getDesdate() {
        return desdate;
    }

    public void setDesdate(Timestamp desdate) {
        this.desdate = desdate;
    }

    @Basic
    @Column(name = "copyright")
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Basic
    @Column(name = "rightsholder")
    public String getRightsholder() {
        return rightsholder;
    }

    public void setRightsholder(String rightsholder) {
        this.rightsholder = rightsholder;
    }

    @Basic
    @Column(name = "descriptiontype_id")
    public String getDescriptiontypeId() {
        return descriptiontypeId;
    }

    public void setDescriptiontypeId(String descriptiontypeId) {
        this.descriptiontypeId = descriptiontypeId;
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
        DescriptionEntity that = (DescriptionEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(destitle, that.destitle) &&
                Objects.equals(descontent, that.descontent) &&
                Objects.equals(language, that.language) &&
                Objects.equals(licenseId, that.licenseId) &&
                Objects.equals(sourcesId, that.sourcesId) &&
                Objects.equals(specialistId, that.specialistId) &&
                Objects.equals(describer, that.describer) &&
                Objects.equals(desdate, that.desdate) &&
                Objects.equals(copyright, that.copyright) &&
                Objects.equals(rightsholder, that.rightsholder) &&
                Objects.equals(descriptiontypeId, that.descriptiontypeId) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(dbaseId, that.dbaseId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(synchdate, that.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, destitle, descontent, language, licenseId, sourcesId, specialistId, describer, desdate, copyright, rightsholder, descriptiontypeId, taxonId, remark, dbaseId, status, inputer, inputtime, synchdate);
    }
}
