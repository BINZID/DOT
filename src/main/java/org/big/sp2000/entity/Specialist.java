package org.big.sp2000.entity;

import java.io.Serializable;

/**
 * The persistent class for the specialists database table.
 * 
 */
public class Specialist implements Serializable {
	private static final long serialVersionUID = 1L;

	private String recordId;

	private String address;

	private String addressChinese;

	private String databaseId;

	private String email;

	private String homepage;

	private String homepageChinese;

	private String institute;

	private String instituteChinese;

	private String specialistCode;

	private String specialistName;

	private String specialistNameChinese;

	public Specialist() {
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressChinese() {
		return this.addressChinese;
	}

	public void setAddressChinese(String addressChinese) {
		this.addressChinese = addressChinese;
	}

	public String getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomepage() {
		return this.homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getHomepageChinese() {
		return this.homepageChinese;
	}

	public void setHomepageChinese(String homepageChinese) {
		this.homepageChinese = homepageChinese;
	}

	public String getInstitute() {
		return this.institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public String getInstituteChinese() {
		return this.instituteChinese;
	}

	public void setInstituteChinese(String instituteChinese) {
		this.instituteChinese = instituteChinese;
	}

	public String getSpecialistCode() {
		return this.specialistCode;
	}

	public void setSpecialistCode(String specialistCode) {
		this.specialistCode = specialistCode;
	}

	public String getSpecialistName() {
		return this.specialistName;
	}

	public void setSpecialistName(String specialistName) {
		this.specialistName = specialistName;
	}

	public String getSpecialistNameChinese() {
		return this.specialistNameChinese;
	}

	public void setSpecialistNameChinese(String specialistNameChinese) {
		this.specialistNameChinese = specialistNameChinese;
	}

}