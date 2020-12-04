package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 *<p><b>Descriptiontype的Entity类</b></p>
 *<p> Descriptiontype的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Descriptiontype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@Column(columnDefinition="varchar(100) COMMENT '名称'")
	private String name;
	@Column(columnDefinition="varchar(100) COMMENT '类型'")
	private String style;
	@Column(columnDefinition="varchar(100) COMMENT '父级ID'")
	private String pid;
	@Column(columnDefinition="varchar(100) COMMENT '排序字段'")
	private String dtorder;

	@OneToMany(mappedBy="descriptiontype")
	private List<Description> descriptions;
	
	public Descriptiontype() {

	}

	public List<Description> getDescriptions() {
		return this.descriptions;
	}

	public void setDescriptions(List<Description> descriptions) {
		this.descriptions = descriptions;
	}

	public Description addDescription(Description description) {
		getDescriptions().add(description);
		description.setDescriptiontype(this);

		return description;
	}

	public Description removeDescription(Description description) {
		getDescriptions().remove(description);
		description.setDescriptiontype(null);

		return description;
	}

}