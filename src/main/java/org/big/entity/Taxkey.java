package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 *<p><b>Taxkey的Entity类</b></p>
 *<p> Taxkey的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:15</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Taxkey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String abstraction;
	private String keytitle;
	private String keytype;

	@OneToMany(mappedBy="taxkey")
	private List<Keyitem> keyitems;

	@ManyToOne
	private Taxon taxon;

	public Taxkey() {
	}

	public void setKeyitems(List<Keyitem> keyitems) {
		this.keyitems = keyitems;
	}

	public List<Keyitem> getKeyitems() {
		return keyitems;
	}

	public Keyitem addKeyitem(Keyitem keyitem) {
		getKeyitems().add(keyitem);
		keyitem.setTaxkey(this);

		return keyitem;
	}

	public Keyitem removeKeyitem(Keyitem keyitem) {
		getKeyitems().remove(keyitem);
		keyitem.setTaxkey(null);

		return keyitem;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}
}