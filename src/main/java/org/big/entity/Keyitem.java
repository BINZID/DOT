package org.big.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *<p><b>Keyitem的Entity类</b></p>
 *<p> Keyitem的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 15:02</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Keyitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private Integer branchid;
	private Integer innerorder;
	private String item;
	private Integer orderid;
	private String pid;
	private String taxonid;
	private String keytype;
	
	@ManyToOne
	private Taxkey taxkey;

	public Keyitem() {

	}

	public void setTaxkey(Taxkey taxkey) {
		this.taxkey = taxkey;
	}

	public Taxkey getTaxkey() {
		return taxkey;
	}
}