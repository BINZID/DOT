package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *<p><b>TeamHasRef的Entity类</b></p>
 *<p> TeamHasRef的Entity类</p>
 * @author BINZI
 *<p>Created date: 2019/01/16 09:45</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name="team_has_ref")
public class TeamHasRef implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@JoinColumn(nullable=false, columnDefinition="varchar(50) COMMENT 'Id'")
	private String id;

	@ManyToOne
	@JoinColumn(nullable=false, columnDefinition="varchar(50) COMMENT '所属团队'")
	@JSONField(serialize=false)
	@JsonIgnore
	private Team team;

	@ManyToOne
	@JoinColumn(nullable=false, columnDefinition="varchar(50) COMMENT '参考文献'")
	private Ref ref;

	public TeamHasRef() {
	}

	public TeamHasRef(String id, Team team, Ref ref) {
		super();
		this.id = id;
		this.team = team;
		this.ref = ref;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Ref getRef() {
		return ref;
	}

	public void setRef(Ref ref) {
		this.ref = ref;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ref == null) ? 0 : ref.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
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
		TeamHasRef other = (TeamHasRef) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ref == null) {
			if (other.ref != null)
				return false;
		} else if (!ref.equals(other.ref))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}
	
}