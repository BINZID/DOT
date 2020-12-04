package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

/**
 *<p><b>Rank的Entity类</b></p>
 *<p> Rank的Entity类</p>
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
public class Rank implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private Integer sort;
	private String chname;
	private String enname;
	@OneToMany(mappedBy="rank")
	private List<Taxon> taxons;

	public Rank() {
	}

	public List<Taxon> getTaxons() {
		return taxons;
	}

	public Taxon addTaxon(Taxon taxon) {
		getTaxons().add(taxon);
		taxon.setRank(this);

		return taxon;
	}

	public Taxon removeTaxon(Taxon taxon) {
		getTaxons().remove(taxon);
		taxon.setRank(null);

		return taxon;
	}
}