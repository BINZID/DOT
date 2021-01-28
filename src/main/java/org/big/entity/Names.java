package org.big.entity;

import java.util.List;

import javax.persistence.*;

/**
 *<p><b>Names的Entity类</b></p>
 *<p> Names的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "names")
public class Names {
	@Id
	private String id;
	
	private String scientificname;
	
	private String chname;
	
	private String authorstr;
	
	@OneToMany(mappedBy = "names")
	private List<Namesource> namesources;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScientificname() {
		return scientificname;
	}
	public void setScientificname(String scientificname) {
		this.scientificname = scientificname;
	}
	public String getChname() {
		return chname;
	}
	public void setChname(String chname) {
		this.chname = chname;
	}
	public String getAuthorstr() {
		return authorstr;
	}
	public void setAuthorstr(String authorstr) {
		this.authorstr = authorstr;
	}
	public List<Namesource> getNamesources() {
		return namesources;
	}
	public void setNamesources(List<Namesource> namesources) {
		this.namesources = namesources;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorstr == null) ? 0 : authorstr.hashCode());
		result = prime * result + ((chname == null) ? 0 : chname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((namesources == null) ? 0 : namesources.hashCode());
		result = prime * result + ((scientificname == null) ? 0 : scientificname.hashCode());
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
		Names other = (Names) obj;
		if (authorstr == null) {
			if (other.authorstr != null)
				return false;
		} else if (!authorstr.equals(other.authorstr))
			return false;
		if (chname == null) {
			if (other.chname != null)
				return false;
		} else if (!chname.equals(other.chname))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (namesources == null) {
			if (other.namesources != null)
				return false;
		} else if (!namesources.equals(other.namesources))
			return false;
		if (scientificname == null) {
			if (other.scientificname != null)
				return false;
		} else if (!scientificname.equals(other.scientificname))
			return false;
		return true;
	}
}
