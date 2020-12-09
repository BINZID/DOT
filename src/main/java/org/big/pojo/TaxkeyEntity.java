package org.big.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p><b>TaxkeyEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "taxkey", schema = "dot", catalog = "")
public class TaxkeyEntity {
    private String id;
    private String keytitle;
    private String abstraction;
    private String taxonId;
    private String keytype;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "keytitle")
    public String getKeytitle() {
        return keytitle;
    }

    public void setKeytitle(String keytitle) {
        this.keytitle = keytitle;
    }

    @Basic
    @Column(name = "abstraction")
    public String getAbstraction() {
        return abstraction;
    }

    public void setAbstraction(String abstraction) {
        this.abstraction = abstraction;
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
    @Column(name = "keytype")
    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxkeyEntity that = (TaxkeyEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(keytitle, that.keytitle) &&
                Objects.equals(abstraction, that.abstraction) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(keytype, that.keytype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keytitle, abstraction, taxonId, keytype);
    }
}
