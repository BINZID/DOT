package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *<p><b>Customs的Entity类</b></p>
 *<p> Customs的Entity类</p>
 * @author 观察君（monitor@yunhoutech.com）
 * <p>Created date: 2019/10/4 15:31</p>
 * <p>Copyright: 云后科技 (北京) 有限公司[ http://www.yunhoutech.com ]</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "customs")
public class Customs implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", columnDefinition="varchar(50) COMMENT 'UUID唯一标识,海关代码'")
	private String id;

	@Column(name="chname", columnDefinition="varchar(200) COMMENT '海关中文名'")
	private String chname;
	
	@Column(name="pid", columnDefinition="varchar(30) COMMENT '父级id'")
	private String pid;
	
	@Column(name="sort", columnDefinition="int COMMENT '排序'")
	private Integer sort;

	@Column(name="remark", columnDefinition="varchar(500) COMMENT '备注'")
	private String remark;

	public Customs(String id, String chname, String pid, Integer sort) {
		super();
		this.id = id;
		this.chname = chname;
		this.pid = pid;
		this.sort = sort;
	}

	public Customs() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChname() {
		return chname;
	}

	public void setChname(String chname) {
		this.chname = chname;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}