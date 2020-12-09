package org.big.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p><b>KeyitemEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "keyitem", schema = "dot", catalog = "")
public class KeyitemEntity {
    private String id;
    private String item;
    private Integer innerorder;
    private Integer orderid;
    private Integer branchid;
    private String taxonId;
    private String pid;
    private String taxkeyId;
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
    @Column(name = "item")
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Basic
    @Column(name = "innerorder")
    public Integer getInnerorder() {
        return innerorder;
    }

    public void setInnerorder(Integer innerorder) {
        this.innerorder = innerorder;
    }

    @Basic
    @Column(name = "orderid")
    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    @Basic
    @Column(name = "branchid")
    public Integer getBranchid() {
        return branchid;
    }

    public void setBranchid(Integer branchid) {
        this.branchid = branchid;
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
    @Column(name = "pid")
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "taxkey_id")
    public String getTaxkeyId() {
        return taxkeyId;
    }

    public void setTaxkeyId(String taxkeyId) {
        this.taxkeyId = taxkeyId;
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
        KeyitemEntity that = (KeyitemEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(item, that.item) &&
                Objects.equals(innerorder, that.innerorder) &&
                Objects.equals(orderid, that.orderid) &&
                Objects.equals(branchid, that.branchid) &&
                Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(taxkeyId, that.taxkeyId) &&
                Objects.equals(keytype, that.keytype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, innerorder, orderid, branchid, taxonId, pid, taxkeyId, keytype);
    }
}
