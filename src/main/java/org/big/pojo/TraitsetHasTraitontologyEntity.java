package org.big.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p><b>TraitsetHasTraitontologyEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "traitset_has_traitontology", schema = "dot", catalog = "")
public class TraitsetHasTraitontologyEntity {
    private String id;
    private String traitsetId;
    private String traitontologyId;
    private String pTraitontologyId;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "traitset_id")
    public String getTraitsetId() {
        return traitsetId;
    }

    public void setTraitsetId(String traitsetId) {
        this.traitsetId = traitsetId;
    }

    @Basic
    @Column(name = "traitontology_id")
    public String getTraitontologyId() {
        return traitontologyId;
    }

    public void setTraitontologyId(String traitontologyId) {
        this.traitontologyId = traitontologyId;
    }

    @Basic
    @Column(name = "p_traitontology_id")
    public String getpTraitontologyId() {
        return pTraitontologyId;
    }

    public void setpTraitontologyId(String pTraitontologyId) {
        this.pTraitontologyId = pTraitontologyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraitsetHasTraitontologyEntity that = (TraitsetHasTraitontologyEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(traitsetId, that.traitsetId) &&
                Objects.equals(traitontologyId, that.traitontologyId) &&
                Objects.equals(pTraitontologyId, that.pTraitontologyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, traitsetId, traitontologyId, pTraitontologyId);
    }
}
