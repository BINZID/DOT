package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *<p><b>Tags的Entity类</b></p>
 *<p> Tags的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/10/28 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Tags implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String enname;

	private String chname;

	private String remark;

	public Tags() {
		super();
	}
}