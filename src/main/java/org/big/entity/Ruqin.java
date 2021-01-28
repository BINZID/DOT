package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

/**
 *<p><b>Rank的Entity类</b></p>
 *<p> Rank的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "ruqin")
public class Ruqin implements Serializable {

	@Id
	private String id;

	private String xiandao;		// 先导
	private String ruqin;		// 外来入侵
	private String rank_harm;	// 入侵等级
	private String gnfb;		// 国内分布
	private String chinese;		// 中文名
	private String family;		// 科
	private String common;		// 俗名
	private String familycn;	// 科中文名
	private String genus;		// 属
	private String genusc;		// 属中文名
	private String epithet;		// 种加词	
	private String latename;	// 拉丁名
	private String latin;		// 拉丁名 + 命名信息
	private String xingzhuangdesc;	// 形状描述
	private String dilifenbutujian;	// 地理分布——口岸图鉴
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getXiandao() {
		return xiandao;
	}


	public void setXiandao(String xiandao) {
		this.xiandao = xiandao;
	}


	public String getRuqin() {
		return ruqin;
	}


	public void setRuqin(String ruqin) {
		this.ruqin = ruqin;
	}


	public String getRank_harm() {
		return rank_harm;
	}


	public void setRank_harm(String rank_harm) {
		this.rank_harm = rank_harm;
	}


	public String getGnfb() {
		return gnfb;
	}


	public void setGnfb(String gnfb) {
		this.gnfb = gnfb;
	}


	public String getChinese() {
		return chinese;
	}


	public void setChinese(String chinese) {
		this.chinese = chinese;
	}


	public String getFamily() {
		return family;
	}


	public void setFamily(String family) {
		this.family = family;
	}


	public String getCommon() {
		return common;
	}


	public void setCommon(String common) {
		this.common = common;
	}


	public String getFamilycn() {
		return familycn;
	}


	public void setFamilycn(String familycn) {
		this.familycn = familycn;
	}


	public String getGenus() {
		return genus;
	}


	public void setGenus(String genus) {
		this.genus = genus;
	}


	public String getGenusc() {
		return genusc;
	}


	public void setGenusc(String genusc) {
		this.genusc = genusc;
	}


	public String getEpithet() {
		return epithet;
	}


	public void setEpithet(String epithet) {
		this.epithet = epithet;
	}


	public String getLatename() {
		return latename;
	}


	public void setLatename(String latename) {
		this.latename = latename;
	}


	public String getLatin() {
		return latin;
	}


	public void setLatin(String latin) {
		this.latin = latin;
	}


	public String getXingzhuangdesc() {
		return xingzhuangdesc;
	}


	public void setXingzhuangdesc(String xingzhuangdesc) {
		this.xingzhuangdesc = xingzhuangdesc;
	}


	public String getDilifenbutujian() {
		return dilifenbutujian;
	}


	public void setDilifenbutujian(String dilifenbutujian) {
		this.dilifenbutujian = dilifenbutujian;
	}


	public Ruqin() {
	}
}