package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>RefsEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "refs", schema = "dot", catalog = "")
public class RefsEntity {
    private String id;
    private String refstr;
    private String author;
    private String pyear;
    private String title;
    private String reftype;
    private String journal;
    private String volume;
    private String issue;
    private String language;
    private String orignlang;
    private String keywords;
    private String translator;
    private String press;
    private String place;
    private String pagenumber;
    private String wordnum;
    private String version;
    private String isbn;
    private String startpage;
    private String endpage;
    private String refurl;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Timestamp synchdate;
    private String remark;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "refstr")
    public String getRefstr() {
        return refstr;
    }

    public void setRefstr(String refstr) {
        this.refstr = refstr;
    }

    @Basic
    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Basic
    @Column(name = "pyear")
    public String getPyear() {
        return pyear;
    }

    public void setPyear(String pyear) {
        this.pyear = pyear;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "reftype")
    public String getReftype() {
        return reftype;
    }

    public void setReftype(String reftype) {
        this.reftype = reftype;
    }

    @Basic
    @Column(name = "journal")
    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    @Basic
    @Column(name = "volume")
    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Basic
    @Column(name = "issue")
    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    @Basic
    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "orignlang")
    public String getOrignlang() {
        return orignlang;
    }

    public void setOrignlang(String orignlang) {
        this.orignlang = orignlang;
    }

    @Basic
    @Column(name = "keywords")
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Basic
    @Column(name = "translator")
    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    @Basic
    @Column(name = "press")
    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    @Basic
    @Column(name = "place")
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Basic
    @Column(name = "pagenumber")
    public String getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
    }

    @Basic
    @Column(name = "wordnum")
    public String getWordnum() {
        return wordnum;
    }

    public void setWordnum(String wordnum) {
        this.wordnum = wordnum;
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
    @Column(name = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "startpage")
    public String getStartpage() {
        return startpage;
    }

    public void setStartpage(String startpage) {
        this.startpage = startpage;
    }

    @Basic
    @Column(name = "endpage")
    public String getEndpage() {
        return endpage;
    }

    public void setEndpage(String endpage) {
        this.endpage = endpage;
    }

    @Basic
    @Column(name = "refurl")
    public String getRefurl() {
        return refurl;
    }

    public void setRefurl(String refurl) {
        this.refurl = refurl;
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

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefsEntity that = (RefsEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(refstr, that.refstr) &&
                Objects.equals(author, that.author) &&
                Objects.equals(pyear, that.pyear) &&
                Objects.equals(title, that.title) &&
                Objects.equals(reftype, that.reftype) &&
                Objects.equals(journal, that.journal) &&
                Objects.equals(volume, that.volume) &&
                Objects.equals(issue, that.issue) &&
                Objects.equals(language, that.language) &&
                Objects.equals(orignlang, that.orignlang) &&
                Objects.equals(keywords, that.keywords) &&
                Objects.equals(translator, that.translator) &&
                Objects.equals(press, that.press) &&
                Objects.equals(place, that.place) &&
                Objects.equals(pagenumber, that.pagenumber) &&
                Objects.equals(wordnum, that.wordnum) &&
                Objects.equals(version, that.version) &&
                Objects.equals(isbn, that.isbn) &&
                Objects.equals(startpage, that.startpage) &&
                Objects.equals(endpage, that.endpage) &&
                Objects.equals(refurl, that.refurl) &&
                Objects.equals(status, that.status) &&
                Objects.equals(inputer, that.inputer) &&
                Objects.equals(inputtime, that.inputtime) &&
                Objects.equals(synchdate, that.synchdate) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, refstr, author, pyear, title, reftype, journal, volume, issue, language, orignlang, keywords, translator, press, place, pagenumber, wordnum, version, isbn, startpage, endpage, refurl, status, inputer, inputtime, synchdate, remark);
    }
}
