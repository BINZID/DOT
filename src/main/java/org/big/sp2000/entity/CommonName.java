package org.big.sp2000.entity;

import java.io.Serializable;

/**
 * The persistent class for the common_names database table.
 * 
 */
public class CommonName implements Serializable {
	private static final long serialVersionUID = 1L;

	private String recordId;

	private String commonName;

	private String commonNamePy;

	private String country;

	private String databaseId;

	private int isInfraspecies;

	private String language;

	private String nameCode;

	private String referenceCode;

	public CommonName() {
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getCommonName() {
		return this.commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getCommonNamePy() {
		return this.commonNamePy;
	}

	public void setCommonNamePy(String commonNamePy) {
		this.commonNamePy = commonNamePy;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public int getIsInfraspecies() {
		return this.isInfraspecies;
	}

	public void setIsInfraspecies(int isInfraspecies) {
		this.isInfraspecies = isInfraspecies;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNameCode() {
		return this.nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	public String getReferenceCode() {
		return this.referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

}