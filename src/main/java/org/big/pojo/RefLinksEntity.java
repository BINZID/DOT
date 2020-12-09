package org.big.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p><b>RefLinksEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "ref_links", schema = "dot", catalog = "")
public class RefLinksEntity {
    private String id;
    private String recordId;
    private String referenceId;
    private String reftype;
    private String refCode;
    private String taxonId;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "record_id")
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    @Basic
    @Column(name = "reference_id")
    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Basic
    @Column(name = "reftype")
    public String getReftype() {
        return reftype;
    }

    public void setReftype(String reftype) {
        this.reftype = reftype;
    }

    @Basic
    @Column(name = "ref_code")
    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    @Basic
    @Column(name = "taxon_id")
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefLinksEntity that = (RefLinksEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(recordId, that.recordId) &&
                Objects.equals(referenceId, that.referenceId) &&
                Objects.equals(reftype, that.reftype) &&
                Objects.equals(refCode, that.refCode) &&
                Objects.equals(taxonId, that.taxonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recordId, referenceId, reftype, refCode, taxonId);
    }
}
