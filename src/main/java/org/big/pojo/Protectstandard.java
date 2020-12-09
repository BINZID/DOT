package org.big.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>Protectstandard</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 16:07
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
public class Protectstandard {
    private String id;
    private String standardname;
    private String standardinfo;
    private String protlevel;
    private String version;
    private Timestamp releasedate;
    private String source;
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
    @Column(name = "standardname")
    public String getStandardname() {
        return standardname;
    }

    public void setStandardname(String standardname) {
        this.standardname = standardname;
    }

    @Basic
    @Column(name = "standardinfo")
    public String getStandardinfo() {
        return standardinfo;
    }

    public void setStandardinfo(String standardinfo) {
        this.standardinfo = standardinfo;
    }

    @Basic
    @Column(name = "protlevel")
    public String getProtlevel() {
        return protlevel;
    }

    public void setProtlevel(String protlevel) {
        this.protlevel = protlevel;
    }

    @Basic
    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Basic
    @Column(name = "releasedate")
    public Timestamp getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Timestamp releasedate) {
        this.releasedate = releasedate;
    }

    @Basic
    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
        Protectstandard that = (Protectstandard) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(standardname, that.standardname) &&
                Objects.equals(standardinfo, that.standardinfo) &&
                Objects.equals(protlevel, that.protlevel) &&
                Objects.equals(version, that.version) &&
                Objects.equals(releasedate, that.releasedate) &&
                Objects.equals(source, that.source) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(synchdate, that.synchdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, standardname, standardinfo, protlevel, version, releasedate, source, status, inputer, inputtime, synchdate);
    }
}
