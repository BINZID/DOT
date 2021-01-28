package org.big.zoology.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Errorinfodot
 * @Description: TODO
 * @Author NY
 * @Date: 2019/12/6 10:07
 * @return
 * @Version V1.0
 */
public class Errorinfodot implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String entityId;
	private String tableName;

	private String errorInfo;

	private String systemName;

	private Date inputtime;

	private String systemPort;

	private String systemIp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public String getSystemPort() {
		return systemPort;
	}

	public void setSystemPort(String systemPort) {
		this.systemPort = systemPort;
	}

	public String getSystemIp() {
		return systemIp;
	}

	public void setSystemIp(String systemIp) {
		this.systemIp = systemIp;
	}
}
