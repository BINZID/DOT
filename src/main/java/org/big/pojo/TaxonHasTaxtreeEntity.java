package org.big.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p><b>TaxonHasTaxtreeEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "taxon_has_taxtree", schema = "dot", catalog = "")
@IdClass(TaxonHasTaxtreeEntityPK.class)
public class TaxonHasTaxtreeEntity {
    private String taxonId;
    private String taxtreeId;
    private String pid;
    private String prevTaxon;
    private String chname;
    private String scientificname;

    @Id
    @Column(name = "taxon_id")
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    @Id
    @Column(name = "taxtree_id")
    public String getTaxtreeId() {
        return taxtreeId;
    }

    public void setTaxtreeId(String taxtreeId) {
        this.taxtreeId = taxtreeId;
    }

    @Basic
    @Column(name = "pid")
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "prev_taxon")
    public String getPrevTaxon() {
        return prevTaxon;
    }

    public void setPrevTaxon(String prevTaxon) {
        this.prevTaxon = prevTaxon;
    }

    @Basic
    @Column(name = "chname")
    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }

    @Basic
    @Column(name = "scientificname")
    public String getScientificname() {
        return scientificname;
    }

    public void setScientificname(String scientificname) {
        this.scientificname = scientificname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxonHasTaxtreeEntity that = (TaxonHasTaxtreeEntity) o;
        return Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(taxtreeId, that.taxtreeId) &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(prevTaxon, that.prevTaxon) &&
                Objects.equals(chname, that.chname) &&
                Objects.equals(scientificname, that.scientificname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxonId, taxtreeId, pid, prevTaxon, chname, scientificname);
    }
}
