package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * <p><b>Descriptiontype</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Descriptiontype {
    private String id;
    private String descterm;
    private String meaning;
    private String pid;
    private int dtorder;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "descterm")
    public String getDescterm() {
        return descterm;
    }

    public void setDescterm(String descterm) {
        this.descterm = descterm;
    }

    @Basic
    @Column(name = "meaning")
    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
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
    @Column(name = "dtorder")
    public int getDtorder() {
        return dtorder;
    }

    public void setDtorder(int dtorder) {
        this.dtorder = dtorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Descriptiontype that = (Descriptiontype) o;
        return dtorder == that.dtorder &&
                Objects.equals(id, that.id) &&
                Objects.equals(descterm, that.descterm) &&
                Objects.equals(meaning, that.meaning) &&
                Objects.equals(pid, that.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descterm, meaning, pid, dtorder);
    }
}
