package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>License</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class License {
    private String id;
    private String licensetitle;
    private String licenseinfo;
    private String imageurl;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Timestamp synchdate;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "licensetitle")
    public String getLicensetitle() {
        return licensetitle;
    }

    public void setLicensetitle(String licensetitle) {
        this.licensetitle = licensetitle;
    }

    @Basic
    @Column(name = "licenseinfo")
    public String getLicenseinfo() {
        return licenseinfo;
    }

    public void setLicenseinfo(String licenseinfo) {
        this.licenseinfo = licenseinfo;
    }

    @Basic
    @Column(name = "imageurl")
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "inputer")
    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer;
    }

    @Basic
    @Column(name = "inputtime")
    public Timestamp getInputtime() {
        return inputtime;
    }

    public void setInputtime(Timestamp inputtime) {
        this.inputtime = inputtime;
    }

    @Basic
    @Column(name = "synchdate")
    public Timestamp getSynchdate() {
        return synchdate;
    }

    public void setSynchdate(Timestamp synchdate) {
        this.synchdate = synchdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        License license = (License) o;
        return Objects.equals(id, license.id) &&
                Objects.equals(licensetitle, license.licensetitle) &&
                Objects.equals(licenseinfo, license.licenseinfo) &&
                Objects.equals(imageurl, license.imageurl) &&
                Objects.equals(status, license.status) &&
                Objects.equals(inputer, license.inputer) &&
                Objects.equals(inputtime, license.inputtime) &&
                Objects.equals(synchdate, license.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, licensetitle, licenseinfo, imageurl, status, inputer, inputtime, synchdate);
    }
}
