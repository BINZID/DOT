package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *<p><b>Address的Entity类</b></p>
 *<p> Address的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "aref")
public class ARef implements Serializable {

	@Id
	private String id;
	
	private String refcontent;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRefcontent() {
		return refcontent;
	}

	public void setRefcontent(String refcontent) {
		this.refcontent = refcontent;
	}

	public ARef() {
		
	}
}