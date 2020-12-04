package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;

/**
 * <p><b>Ref的Entity类</b></p>
 * <p>Ref的Entity类</p>
 * @author BIN
 * <p>Created date: 2020/11/30 09:35</p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfoGroup) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
public class Ref implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String ptype;		//文献类型
	private String refstr;		//完整题录
	private String author;		//作者
	private String pyear;		//发表年代
	private String title;		//题目
	private String journal;		//期刊|专著|论文集
	private String rVolume;		//卷*
	private String rPeriod;		//期*
	private String refs; 		//起始页
	private String refe;		//终止页
	private String isbn;		//文献标识*
	private String place;		//出版地
	private String press;		//出版社
	private String translator;	//编译者
	private String keywords;	//关键字
	private String tpage;		//总页数
	private String tchar;		//总字数
	private String languages;	//文献语言
	private String olang;		//原始语言
	private String version;		//版本*
	@Lob
	private String remark;		//备注
	private String leftstr;	 
	private String rLink;
	
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private int status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;
	private String taxasetId;	// 针对某个分类单元集上传的文献

	public Ref() {
	}
	
}