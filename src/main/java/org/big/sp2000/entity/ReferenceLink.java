package org.big.sp2000.entity;

import java.io.Serializable;

/**
 * The persistent class for the reference_links database table.
 * 
 */
public class ReferenceLink implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private String recordId;

	private String referenceCode;

	private String referenceId;

	private String referenceType;

	public ReferenceLink() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getReferenceCode() {
		return this.referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceType() {
		return this.referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

}