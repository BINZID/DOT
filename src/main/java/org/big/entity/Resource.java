package org.big.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *<p><b>KeyitemResource的Entity类</b></p>
 *<p> KeyitemResource的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/8/23 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Entity
@Table(name = "resource")
public class Resource implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String resourceName;
	private String type;
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Keyitem keyitem;
	public Resource(String id, String resourceName, String type, Keyitem keyitem) {
		this.id = id;
		this.resourceName = resourceName;
		this.type = type;
		this.keyitem = keyitem;
	}
	public Resource() {
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Keyitem getKeyitem() {
		return keyitem;
	}
	public void setKeyitem(Keyitem keyitem) {
		this.keyitem = keyitem;
	}
}
