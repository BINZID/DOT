package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 *<p><b>Expert的Entity类</b></p>
 *<p> Expert的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:45</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Specialist implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String cnName;
	private String enName;
	private String cnCompany;
	private String enCompany;
	private String cnAddress;
	private String enAddress;
	private String expEmail;
	private String cnHomePage;
	private String enHomePage;
	private String expInfo;
	
	private Integer status;
	
	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private Integer synchstatus;
	
	public Specialist() {
		super();
	}
}
