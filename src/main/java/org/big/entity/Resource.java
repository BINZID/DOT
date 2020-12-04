package org.big.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *<p><b>KeyitemResource的Entity类</b></p>
 *<p> KeyitemResource的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/30 09:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Entity
@Getter
@Setter
@ToString
public class Resource implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String path;
	private String type;
	private String recordId;
	public Resource() {

	}
}
