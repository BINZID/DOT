package org.big.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 *<p><b>Quarantineprodb的Entity类</b></p>
 *<p> Quarantineprodb的Entity类</p>
 * @author 观察君（monitor@yunhoutech.com）
 * <p>Created date: 2020/01/01</p>
 * <p>Copyright: 云后科技 (北京) 有限公司[ http://www.yunhoutech.com ]</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "quarantineprodb")
public class Quarantineprodb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", columnDefinition="varchar(50) COMMENT 'UUID唯一标识'")
	private String id;

	@Column(name="type", columnDefinition="varchar(200) COMMENT '分类，例：水果、干果、蔬菜；繁殖材料；粮豆、油籽类；饲草、饲料类；棉麻、烟草类；竹藤柳草及其制品；原木及其制品；运输工具；土壤及其他，关联类别表'" )
	private String type;
	
	@Column(name="host", columnDefinition="varchar(200) COMMENT '寄主'" )
	private String host;

	@Column(name="pest", columnDefinition="varchar(200) COMMENT '有害生物'" )
	private String pest;
	
	@Column(name="measures", columnDefinition="varchar(200) COMMENT '处理方法'" )
	private String measures;
	
	@Column(name="htmlcode", columnDefinition="TEXT COMMENT '处理方法表格数据'" )
	private String htmlcode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;//录入时间
	
	@Column(columnDefinition="int(11) DEFAULT 1 COMMENT '状态（默认1、可用；0、不可用）'" )
	private Integer status;
	
	@Column(name="remark", columnDefinition="varchar(500) COMMENT '备注'" )
	private String remark;

	public Quarantineprodb() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPest() {
		return pest;
	}

	public void setPest(String pest) {
		this.pest = pest;
	}

	public String getMeasures() {
		return measures;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	public String getHtmlcode() {
		return htmlcode;
	}

	public void setHtmlcode(String htmlcode) {
		this.htmlcode = htmlcode;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Quarantineprodb [id=" + id + ", type=" + type + ", host=" + host + ", pest=" + pest + ", measures="
				+ measures + ", htmlcode=" + htmlcode + ", inputtime=" + inputtime + ", status=" + status + ", remark="
				+ remark + "]";
	}

}