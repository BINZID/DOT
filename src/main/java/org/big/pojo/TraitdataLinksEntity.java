package org.big.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p><b>TraitdataLinksEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "traitdata_links", schema = "dot", catalog = "")
public class TraitdataLinksEntity {
    private String id;
    private String traitUnit;
    private String traitValue;
    private String traitRef;
    private String traitdataId;
    private String traitPropertyId;
    private String traitOntologyId;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "trait_unit")
    public String getTraitUnit() {
        return traitUnit;
    }

    public void setTraitUnit(String traitUnit) {
        this.traitUnit = traitUnit;
    }

    @Basic
    @Column(name = "trait_value")
    public String getTraitValue() {
        return traitValue;
    }

    public void setTraitValue(String traitValue) {
        this.traitValue = traitValue;
    }

    @Basic
    @Column(name = "trait_ref")
    public String getTraitRef() {
        return traitRef;
    }

    public void setTraitRef(String traitRef) {
        this.traitRef = traitRef;
    }

    @Basic
    @Column(name = "traitdata_id")
    public String getTraitdataId() {
        return traitdataId;
    }

    public void setTraitdataId(String traitdataId) {
        this.traitdataId = traitdataId;
    }

    @Basic
    @Column(name = "trait_property_id")
    public String getTraitPropertyId() {
        return traitPropertyId;
    }

    public void setTraitPropertyId(String traitPropertyId) {
        this.traitPropertyId = traitPropertyId;
    }

    @Basic
    @Column(name = "trait_ontology_id")
    public String getTraitOntologyId() {
        return traitOntologyId;
    }

    public void setTraitOntologyId(String traitOntologyId) {
        this.traitOntologyId = traitOntologyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraitdataLinksEntity that = (TraitdataLinksEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(traitUnit, that.traitUnit) &&
                Objects.equals(traitValue, that.traitValue) &&
                Objects.equals(traitRef, that.traitRef) &&
                Objects.equals(traitdataId, that.traitdataId) &&
                Objects.equals(traitPropertyId, that.traitPropertyId) &&
                Objects.equals(traitOntologyId, that.traitOntologyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, traitUnit, traitValue, traitRef, traitdataId, traitPropertyId, traitOntologyId);
    }
}
