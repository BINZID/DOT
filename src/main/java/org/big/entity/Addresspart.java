package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *<p><b>Address的Entity类</b></p>
 *<p> Address的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "addresspart")
public class Addresspart implements Serializable {

	@Id
	private String id;
	
	private String sciname;
	private String cnname;
	private String province;
	private String city;
	private String county;
	private String orignal;
	private String remark;
	private String refcode;
	private String refs;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSciname() {
		return sciname;
	}
	public void setSciname(String sciname) {
		this.sciname = sciname;
	}
	public String getCnname() {
		return cnname;
	}
	public void setCnname(String cnname) {
		this.cnname = cnname;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getOrignal() {
		return orignal;
	}
	public void setOrignal(String orignal) {
		this.orignal = orignal;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRefcode() {
		return refcode;
	}
	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}
	public String getRefs() {
		return refs;
	}
	public void setRefs(String refs) {
		this.refs = refs;
	}
	public Addresspart() {
		
	}
}