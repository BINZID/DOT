package org.big.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *<p><b>Commonname的Entity类</b></p>
 *<p> Commonname的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:20</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Commonname implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    private String id;
    private String commonname;
    private String language;
    private String sourcesid;
    private String specialist;
    private String remark;
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Integer synchstatus;
    private Timestamp synchdate;

	@ManyToOne
	private Taxon taxon;

	public Commonname() {

	}
}
