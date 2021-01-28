package org.big.zoology.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author HM
 * @since 2019-10-18
 */
public class Refs implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String refstr;

	private String author;

	private String pyear;

	private String title;

	private String reftype;

	/**
	 * 期刊/专著/论文集
	 */
	private String journal;

	/**
	 * 卷
	 */
	private String volume;

	/**
	 * 期
	 */
	private String issue;

	/**
	 * 文献语言
	 */
	private String language;

	/**
	 * 原始语言
	 */
	private String orignlang;

	/**
	 * 关键字
	 */
	private String keywords;

	/**
	 * 编译者
	 */
	private String translator;

	/**
	 * 出版社
	 */
	private String press;

	/**
	 * 出版地
	 */
	private String place;

	/**
	 * 总页数
	 */
	private String pagenumber;

	/**
	 * 总字数
	 */
	private String wordnum;

	/**
	 * 版本
	 */
	private String version;

	/**
	 * 文献标识
	 */
	private String isbn;

	/**
	 * 起始页
	 */
	private String startpage;

	/**
	 * 终止页
	 */
	private String endpage;

	/**
	 * 链接地址
	 */
	private String refurl;

	/**
	 * 状态（默认1、可用；0、不可用）
	 */
	private Integer status;

	/**
	 * 录入人
	 */
	private String inputer;

	/**
	 * 录入时间
	 */
	private Date inputtime;

	/**
	 * 更新时间
	 */
	private Date synchdate;

	/**
	 * 备注
	 */
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRefstr() {
		return refstr;
	}

	public void setRefstr(String refstr) {
		this.refstr = refstr;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPyear() {
		return pyear;
	}

	public void setPyear(String pyear) {
		this.pyear = pyear;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReftype() {
		return reftype;
	}

	public void setReftype(String reftype) {
		this.reftype = reftype;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOrignlang() {
		return orignlang;
	}

	public void setOrignlang(String orignlang) {
		this.orignlang = orignlang;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPagenumber() {
		return pagenumber;
	}

	public void setPagenumber(String pagenumber) {
		this.pagenumber = pagenumber;
	}

	public String getWordnum() {
		return wordnum;
	}

	public void setWordnum(String wordnum) {
		this.wordnum = wordnum;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getStartpage() {
		return startpage;
	}

	public void setStartpage(String startpage) {
		this.startpage = startpage;
	}

	public String getEndpage() {
		return endpage;
	}

	public void setEndpage(String endpage) {
		this.endpage = endpage;
	}

	public String getRefurl() {
		return refurl;
	}

	public void setRefurl(String refurl) {
		this.refurl = refurl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public Date getSynchdate() {
		return synchdate;
	}

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
