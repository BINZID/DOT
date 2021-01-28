package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

/**
 *<p><b>Rank的Entity类</b></p>
 *<p> Rank的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "entitys")
public class Entitys implements Serializable {

	@Id
	private String id;

	private String sciname;
	
	private String chname;

	private String ranks;
	
	private String important;
	
	private String type;
	
	private String count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSciname() {
		return sciname;
	}

	public void setSciname(String sciname) {
		this.sciname = sciname;
	}

	public String getChname() {
		return chname;
	}

	public void setChname(String chname) {
		this.chname = chname;
	}

	public String getRanks() {
		return ranks;
	}

	public void setRanks(String ranks) {
		this.ranks = ranks;
	}

	public String getImportant() {
		return important;
	}

	public void setImportant(String important) {
		this.important = important;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chname == null) ? 0 : chname.hashCode());
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((important == null) ? 0 : important.hashCode());
		result = prime * result + ((ranks == null) ? 0 : ranks.hashCode());
		result = prime * result + ((sciname == null) ? 0 : sciname.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entitys other = (Entitys) obj;
		if (chname == null) {
			if (other.chname != null)
				return false;
		} else if (!chname.equals(other.chname))
			return false;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (important == null) {
			if (other.important != null)
				return false;
		} else if (!important.equals(other.important))
			return false;
		if (ranks == null) {
			if (other.ranks != null)
				return false;
		} else if (!ranks.equals(other.ranks))
			return false;
		if (sciname == null) {
			if (other.sciname != null)
				return false;
		} else if (!sciname.equals(other.sciname))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	

}