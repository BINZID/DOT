package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 *<p><b>Multimedia的Entity类</b></p>
 *<p> Multimedia的Entity类</p>
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
public class Multimedia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String mediatype;	//媒体类型
	private String context;		//图注
	private String title;		//标签
	private String oldPath;		//文件地址
	private String sourcesid;	//数据源	
	private String expert;		//审核专家
	private String rightsholder;//版权所有者
	private String copyright;	//版权声明
	private String lisenceid;	//共享协议
	private String path;		//原始链接
	private String createtime;	//创建时间
	private String creator;		//创建人
	private String publisher;	//发布者
	private String contributor;	//贡献者
	private String country;		//国家
	private String province;	//省
	private String city;		//市
	private String county;		//县
	private String locality;	//小地点
	private Double lng;			//经度
	private Double lat;			//维度

	private String location;
	private String desid;
	private String disId;
	private String info;
	private String suffix;
	private int status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;		//录入时间
	private String inputer;		//录入者
	@ManyToOne
	private License license;

	@ManyToOne
	private Taxon taxon;

	public Multimedia() {
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}
}