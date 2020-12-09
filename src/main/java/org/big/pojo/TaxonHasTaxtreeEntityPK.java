package org.big.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p><b>TaxonHasTaxtreeEntityPK</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
public class TaxonHasTaxtreeEntityPK implements Serializable {
    private String taxonId;
    private String taxtreeId;

    @Column(name = "taxon_id")
    @Id
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    @Column(name = "taxtree_id")
    @Id
    public String getTaxtreeId() {
        return taxtreeId;
    }

    public void setTaxtreeId(String taxtreeId) {
        this.taxtreeId = taxtreeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxonHasTaxtreeEntityPK that = (TaxonHasTaxtreeEntityPK) o;
        return Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(taxtreeId, that.taxtreeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxonId, taxtreeId);
    }
}
