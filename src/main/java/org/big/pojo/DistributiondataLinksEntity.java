package org.big.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p><b>DistributiondataLinksEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:10
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "distributiondata_links", schema = "dot", catalog = "")
public class DistributiondataLinksEntity {
    private String id;
    private String geoobjectId;
    private String distributiondataId;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "geoobject_id")
    public String getGeoobjectId() {
        return geoobjectId;
    }

    public void setGeoobjectId(String geoobjectId) {
        this.geoobjectId = geoobjectId;
    }

    @Basic
    @Column(name = "distributiondata_id")
    public String getDistributiondataId() {
        return distributiondataId;
    }

    public void setDistributiondataId(String distributiondataId) {
        this.distributiondataId = distributiondataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistributiondataLinksEntity that = (DistributiondataLinksEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(geoobjectId, that.geoobjectId) &&
                Objects.equals(distributiondataId, that.distributiondataId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, geoobjectId, distributiondataId);
    }
}
