package org.big.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p><b>RanksEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "ranks", schema = "dot", catalog = "")
public class RanksEntity {
    private String id;
    private int sort;
    private String chname;
    private String enname;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "sort")
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
    @Column(name = "enname")
    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RanksEntity that = (RanksEntity) o;
        return sort == that.sort &&
                Objects.equals(id, that.id) &&
                Objects.equals(chname, that.chname) &&
                Objects.equals(enname, that.enname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sort, chname, enname);
    }
}
