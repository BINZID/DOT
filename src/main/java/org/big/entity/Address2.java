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
@Table(name = "address2")
public class Address2 implements Serializable {

	@Id
	private String id;
	
	private String adcode;

	private String address;
	
	private String pid;

	private String year;
	
	private String ppid;
	
	private String note;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdcode() {
		return adcode;
	}

	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Address2(String id, String adcode, String address, String pid, String year, String ppid, String note) {
		super();
		this.id = id;
		this.adcode = adcode;
		this.address = address;
		this.pid = pid;
		this.year = year;
		this.ppid = ppid;
		this.note = note;
	}

	public Address2() {

	}

}