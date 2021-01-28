package org.big.service;

import java.util.List;

import org.big.entity.Baseinfotmp;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Datasource;
import org.big.entity.Description;
import org.big.entity.Distributiondata;
import org.big.entity.Expert;
import org.big.entity.Multimedia;
import org.big.entity.Protection;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entity.Traitdata;
/**
 * 
 * @Description 数据库连接参数需要增加rewriteBatchedStatements=true
 * @author ZXY
 */
public interface BatchInsertService {
	/**
	 * @Description 批量存储Baseinfotmp基础信息
	 * @author BINZI
	 * @param baseinfotmps
	 * @throws Exception
	 */
	public void batchInsertBaseinfotmps(List<Baseinfotmp> baseinfotmps) throws Exception;
	
	/**
	 * @Description 批量存储Expert专家信息
	 * @author BINZI
	 * @param experts
	 * @throws Exception
	 */
	public void batchInsertExpert(List<Expert> experts) throws Exception;
	
	/**
	 * @Description 批量存储Ref参考文献
	 * @author BINZI
	 * @param refs
	 * @throws Exception
	 */
	public void batchInsertRef(List<Ref> refs) throws Exception;
	
	/**
	 * @Description 批量存储Datasource数据源信息
	 * @author BINZI
	 * @param datasources
	 * @throws Exception
	 */
	public void batchInsertDatasource(List<Datasource> datasources) throws Exception;
	
	/**
	 * @Description 批量存储Taxon基础信息
	 * @author BINZI
	 * @param taxons
	 * @throws Exception
	 */
	public void batchInsertTaxon(List<Taxon> taxons) throws Exception;
	
	/**
	 * @Description 批量存储引证
	 * @author BINZI
	 * @param citations
	 * @throws Exception
	 */
	public void batchInsertCitation(List<Citation> citations) throws Exception ;
	
	/**
	 * @Description 批量存储描述
	 * @author BINZI
	 * @param descriptions
	 * @throws Exception
	 */
	public void batchInsertDescription(List<Description> descriptions) throws Exception ;
	
	/**
	 * @Description 批量存储分布
	 * @author BINZI
	 * @param distributions
	 * @throws Exception
	 */
	public void batchInsertDistributiondata(List<Distributiondata> distributions) throws Exception ;
	
	/**
	 * @Description 批量存储俗名
	 * @author BINZI
	 * @param commonname
	 * @throws Exception
	 */
	public void batchInsertCommonname (List<Commonname> commonname) throws Exception;
	
	/**
	 * @Description 批量存储
	 * @author BINZI
	 * @param protections
	 * @throws Exception
	 */
	public void batchInsertProtection(List<Protection> protections) throws Exception;
	
	/**
	 * @Description 批量存储
	 * @author BINZI
	 * @param traitdatas
	 * @throws Exception
	 */
	public void batchInsertTraitdata(List<Traitdata> traitdatas) throws Exception;
	
	/**
	 * @Description 批量存储
	 * @author BINZI
	 * @param multimedias
	 * @throws Exception
	 */
	public void batchInsertMultimedia(List<Multimedia> multimedias);
	
	/**
	 * @Description 跟新文件上传中重复的Taxon数据
	 * @author BINZI
	 * @param taxon
	 */
	public void updateTaxon(Taxon taxon);

	public void updateTaxonHasTaxtree(List<TaxonHasTaxtree> updateTaxonHasTaxtree);
	
	/**
	 * @Desciption 批量更新Taxon
	 * @param taxons
	 * @throws Exception
	 */
	public void batchUpdateTaxon(List<Taxon> taxons) throws Exception ;
	
	/**
	 * @Desciption 批量更新Citation
	 * @param citations
	 * @throws Exception
	 */
	public void batchUpdateCitation(List<Citation> citations) throws Exception ;
}
