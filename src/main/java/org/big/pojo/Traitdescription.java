package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * <p><b>Traitdescription</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Traitdescription {
    private int id;
    private String traitontologyid;
    private String propertyid;
    private String definition;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "traitontologyid")
    public String getTraitontologyid() {
        return traitontologyid;
    }

    public void setTraitontologyid(String traitontologyid) {
        this.traitontologyid = traitontologyid;
    }

    @Basic
    @Column(name = "propertyid")
    public String getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(String propertyid) {
        this.propertyid = propertyid;
    }

    @Basic
    @Column(name = "definition")
    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Traitdescription that = (Traitdescription) o;
        return id == that.id &&
                Objects.equals(traitontologyid, that.traitontologyid) &&
                Objects.equals(propertyid, that.propertyid) &&
                Objects.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, traitontologyid, propertyid, definition);
    }
}
