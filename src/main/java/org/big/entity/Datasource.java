package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
/**
 *<p><b>Datasource的Entity类</b></p>
 *<p> Datasource的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:29</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Entity
@Getter
@Setter
@ToString
public class Datasource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String title;
	private String type;
	private String versions;
	private String creater;
	private String createtime;
	private String introduce;
	private String keyword;
	private String url;
	private String specialist;
	private String rightsholder;
	private String copyright;
	private String info;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private Integer synchstatus;
	
	public Datasource() {

	}
	
}