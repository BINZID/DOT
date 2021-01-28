package org.big.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.big.common.UUIDUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class BatchInsertServiceImpl implements BatchInsertService {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	private JdbcTemplate jdbctemp;
	@PersistenceContext
	EntityManager em;
	
	@Override
	public void batchInsertBaseinfotmps(List<Baseinfotmp> baseinfotmps) throws Exception {
        String sql = "INSERT INTO baseinfotmp "
        		+ "(id, ref_ds_id, serial_num, filemark, file_type, inputer, inputtime) "
        		+ "VALUES (:id, :refDsId, :serialNum, :filemark, :fileType, :inputer, :inputtime)";
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(baseinfotmps.toArray());
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batch);
        System.out.println("批量上传条数：" + updateCounts.length);
	}

	@Override
	public void batchInsertExpert(List<Expert> experts) throws Exception {
        String sql = "INSERT INTO expert "
        		+ "(id, cn_name, en_name, cn_company, en_company, cn_address, en_address, exp_email, cn_home_page, en_home_page, exp_info, status, inputer, inputtime, synchdate, synchstatus) "
        		+ "VALUES (:id, :cnName, :enName, :cnCompany, :enCompany, :cnAddress, :enAddress, :expEmail, :cnHomePage, :enHomePage, :expInfo, :status, :inputer, :inputtime, :synchdate, :synchstatus)";
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(experts.toArray());
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batch);
        System.out.println("批量上传条数：" + updateCounts.length);
	}

	@Override
	public void batchInsertRef(List<Ref> refs) throws Exception {
		String remark = UUIDUtils.getUUID32();
		String sql = "INSERT INTO refs "
        		+ "(id, ptype, refstr, author, pyear, title, journal, r_volume, r_period, refs, refe, isbn, place, press, translator, keywords, tpage, tchar, languages, olang, version, remark, r_link, inputer, inputtime, status, synchdate, synchstatus) "
        		+ "VALUES (:id, :ptype, :refstr, :author, :pyear, :title, :journal, :rVolume, :rPeriod, :refs, :refe, :isbn, :place, :press, :translator, :keywords, :tpage, :tchar, :languages, :olang, :version, :remark, :rLink, :inputer, :inputtime, :status, :synchdate, :synchstatus)";
		List<Map<String, Object>> batchValues = new ArrayList<>(refs.size());
        for (Ref ref : refs) {
            batchValues.add(
                    new MapSqlParameterSource("id", ref.getId())
                            .addValue("ptype", ref.getPtype())
                            .addValue("refstr", ref.getRefstr())
                            .addValue("author", ref.getAuthor())
                            .addValue("pyear", ref.getPyear())
                            .addValue("title", ref.getTitle())
                            .addValue("journal", ref.getJournal())
                            .addValue("rVolume", ref.getrVolume())
                            .addValue("rPeriod", ref.getrPeriod())
                            .addValue("refs", ref.getRefs())
                            .addValue("refe", ref.getRefe())
                            .addValue("isbn", ref.getIsbn())
                            .addValue("place", ref.getPlace())
                            .addValue("press", ref.getPress())
                            .addValue("translator", ref.getTranslator())
                            .addValue("keywords", ref.getKeywords())
                            .addValue("tpage", ref.getTpage())
                            .addValue("tchar", ref.getTchar())
                            .addValue("languages", ref.getLanguages())
                            .addValue("olang", ref.getOlang()) 
                            .addValue("version", ref.getVersion())
                            .addValue("remark", remark)
                            .addValue("rLink", ref.getrLink())
                            .addValue("inputer", ref.getInputer())
                            .addValue("inputtime", ref.getInputtime())
                            .addValue("status", ref.getStatus())
                            .addValue("synchdate", ref.getSynchdate())
                            .addValue("synchstatus", ref.getSynchstatus())
                            .getValues());
        }
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[refs.size()]));
        System.out.println("批量上传条数：" + updateCounts.length);
    }

	@Override
	public void batchInsertDatasource(List<Datasource> datasources) throws Exception {
		String sql = "INSERT INTO datasources "
				+ "(id, title, d_type, versions, creater, createtime, d_abstract, d_keyword, d_link, d_verifier, d_rightsholder, d_copyright, info, inputer, inputtime, status, synchdate, synchstatus) "
				+ "VALUES (:id, :title, :dType, :versions, :creater, :createtime, :dAbstract, :dKeyword, :dLink, :dVerifier, :dRightsholder, :dCopyright, :info, :inputer, :inputtime, :status, :synchdate, :synchstatus)";
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(datasources.toArray());
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		System.out.println("批量上传条数：" + updateCounts.length);
	}
	
	@Override
	public void batchInsertTaxon(List<Taxon> taxons) throws SQLException {
		String sql = "INSERT INTO taxon "
				+ "(id, scientificname, chname, authorstr, epithet, nomencode, rankid, ref_class_sys, refjson, sourcesid, expert, remark, comment, inputer, inputtime, status, synchdate, synchstatus, taxon_condition, order_num, rank_id, taxaset_id) "
				+ "VALUES (:id, :scientificname, :chname, :authorstr, :epithet, :nomencode, :rankid, :refClassSys, :refjson, :sourcesid, :expert, :remark, :comment, :inputer, :inputtime, :status, :synchdate, :synchstatus, :taxonCondition, :orderNum, :rank, :taxaset)";
		List<Map<String, Object>> batchValues = new ArrayList<>(taxons.size());
        for (Taxon taxon : taxons) {
            batchValues.add(
                    new MapSqlParameterSource("id", taxon.getId())
                            .addValue("scientificname", taxon.getScientificname())
                            .addValue("chname", taxon.getChname())
                            .addValue("authorstr", taxon.getAuthorstr())
                            .addValue("epithet", taxon.getEpithet())
                            .addValue("nomencode", taxon.getNomencode())
                            .addValue("rankid", taxon.getRank().getId())
                            .addValue("refClassSys", taxon.getRefClassSys())
                            .addValue("refjson", JSON.toJSONString(taxon.getRefjson()))
                            .addValue("sourcesid", taxon.getSourcesid())
                            .addValue("expert", taxon.getExpert())
                            .addValue("remark", taxon.getRemark())
                            .addValue("comment", taxon.getComment())
                            .addValue("inputer", taxon.getInputer())
                            .addValue("inputtime", taxon.getInputtime())
                            .addValue("status", taxon.getStatus())
                            .addValue("synchdate", taxon.getSynchdate())
                            .addValue("synchstatus", taxon.getSynchstatus())
                            .addValue("taxonCondition", 1)
                            .addValue("orderNum", taxon.getOrderNum())
                            .addValue("rank", taxon.getRank().getId())
                            .addValue("taxaset", taxon.getTaxaset().getId())
                            .getValues());
        }
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[taxons.size()]));
        System.out.println("批量上传条数：" + updateCounts.length);
    }

	@Override
	public void batchInsertCitation(List<Citation> citations) {
		String sql = "INSERT INTO citation "
				+ "(id, sciname, authorship, nametype, citationstr, sourcesid, refjson, expert, shortrefs, inputer, inputtime, status, synchdate, synchstatus, remark, taxon_id) "
				+ "VALUES (:id, :sciname, :authorship, :nametype, :citationstr, :sourcesid, :refjson, :expert, :shortrefs, :inputer, :inputtime, :status, :synchdate, :synchstatus, :remark, :taxon)";
		List<Map<String, Object>> batchValues = new ArrayList<>(citations.size());
        for (Citation citation : citations) {
            batchValues.add(
                    new MapSqlParameterSource("id", citation.getId())
                            .addValue("sciname", citation.getSciname())
                            .addValue("authorship", citation.getAuthorship())
                            .addValue("nametype", citation.getNametype())
                            .addValue("citationstr", citation.getCitationstr())
                            .addValue("sourcesid", citation.getSourcesid())
                            .addValue("refjson", JSON.toJSONString(citation.getRefjson()))
                            .addValue("expert", citation.getExpert())
                            .addValue("shortrefs", citation.getShortrefs())
                            .addValue("remark", citation.getRemark())
                            .addValue("inputer", citation.getInputer())
                            .addValue("inputtime", citation.getInputtime())
                            .addValue("status", citation.getStatus())
                            .addValue("synchdate", citation.getSynchdate())
                            .addValue("synchstatus", citation.getSynchstatus())
                            .addValue("taxon", citation.getTaxon().getId())
                            .getValues());
        }
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[citations.size()]));
		System.out.println("批量上传条数：" + updateCounts.length);
	}

	/**
	 * Description 描述
	 * 
	 * @param records
	 */
	public void batchInsertDescription(List<Description> descriptions) {
		String sql = "INSERT INTO description "
				+ "(id, destitle, desdate, describer, language, destypeid, descontent, rightsholder, d_copyright, licenseid, sourcesid, refjson, expert, inputer, inputtime, status, synchdate, synchstatus, remark, descriptiontype_id, taxon_id) "
				+ "VALUES (:id, :destitle, :desdate, :describer, :language, :destypeid, :descontent, :rightsholder, :dCopyright, :licenseid, :sourcesid, :refjson, :expert, :inputer, :inputtime, :status, :synchdate, :synchstatus, :remark, :descriptiontype, :taxon)";
		List<Map<String, Object>> batchValues = new ArrayList<>(descriptions.size());
        for (Description description : descriptions) {
            batchValues.add(
                    new MapSqlParameterSource("id", description.getId())
                            .addValue("destitle", description.getDestitle())
                            .addValue("desdate", description.getDesdate())
                            .addValue("describer", description.getDescriber())
                            .addValue("language", description.getLanguage())
                            .addValue("destypeid", description.getDestypeid())
                            .addValue("descontent", description.getDescontent())
                            .addValue("rightsholder", description.getRightsholder())
                            .addValue("dCopyright", description.getdCopyright())
                            .addValue("licenseid", description.getLicenseid())
                            .addValue("sourcesid", description.getSourcesid())
                            .addValue("refjson", JSON.toJSONString(description.getRefjson()))
                            .addValue("expert", description.getExpert())
                            .addValue("remark", description.getRemark())
                            .addValue("inputer", description.getInputer())
                            .addValue("inputtime", description.getInputtime())
                            .addValue("status", description.getStatus())
                            .addValue("synchdate", description.getSynchdate())
                            .addValue("synchstatus", description.getSynchstatus())
                            .addValue("descriptiontype", description.getDescriptiontype().getId())
                            .addValue("taxon", description.getTaxon().getId())
                            .getValues());
        }
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[descriptions.size()]));
		System.out.println("批量上传条数：" + updateCounts.length);
	}

	/**
	 * Distributiondata 分布
	 * 
	 * @param records
	 */
	public void batchInsertDistributiondata(List<Distributiondata> distributions) throws Exception {
		String sql = "INSERT INTO distributiondata "
				+ "(id, geojson, dismark, dismapstandard, distype, province, city, county, locality, lat, lng, sourcesid, refjson, expert, inputer, inputtime, status, synchdate, synchstatus, remark, taxon_id) "
				+ "VALUES (:id, :geojson, :dismark, :dismapstandard, :distype, :province, :city, :county, :locality, :lat, :lng, :sourcesid, :refjson, :expert, :inputer, :inputtime, :status, :synchdate, :synchstatus, :remark, :taxon)";
		List<Map<String, Object>> batchValues = new ArrayList<>(distributions.size());
        for (Distributiondata distribution : distributions) {
            batchValues.add(
                    new MapSqlParameterSource("id", distribution.getId())
                            .addValue("geojson", distribution.getGeojson())
                            .addValue("dismark", distribution.getDismark())
                            .addValue("dismapstandard", distribution.getDismapstandard())
                            .addValue("distype", distribution.getDistype())
                            .addValue("province", distribution.getProvince())
                            .addValue("city", distribution.getCity())
                            .addValue("county", distribution.getCounty())
                            .addValue("locality", distribution.getLocality())
                            .addValue("lat", distribution.getLat())
                            .addValue("lng", distribution.getLng())
                            .addValue("sourcesid", distribution.getSourcesid())
                            .addValue("refjson", distribution.getRefjson())
                            .addValue("expert", distribution.getExpert())
                            .addValue("remark", distribution.getRemark())
                            .addValue("inputer", distribution.getInputer())
                            .addValue("inputtime", distribution.getInputtime())
                            .addValue("status", distribution.getStatus())
                            .addValue("synchdate", distribution.getSynchdate())
                            .addValue("synchstatus", distribution.getSynchstatus())
                            .addValue("taxon", distribution.getTaxon().getId())
                            .getValues());
        }
		int[] updateCounts= namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[distributions.size()]));
		System.out.println("批量上传条数：" + updateCounts.length);
	}

	/**
	 * Commonname 俗名
	 * 
	 * @param records
	 */
	public void batchInsertCommonname(List<Commonname> commonnames) {
		String sql = "INSERT INTO commonname "
				+ "(id, commonname, language, refjson, sourcesid, expert, remark, status, inputer, inputtime, synchstatus, synchdate, taxon_id) "
				+ "VALUES (:id, :commonname, :language, :refjson, :sourcesid, :expert, :remark, :status, :inputer, :inputtime, :synchstatus, :synchdate, :taxon)";
		List<Map<String, Object>> batchValues = new ArrayList<>(commonnames.size());
        for (Commonname commonname : commonnames) {
            batchValues.add(
                    new MapSqlParameterSource("id", commonname.getId())
                            .addValue("commonname", commonname.getCommonname())
                            .addValue("language", commonname.getLanguage())
                            .addValue("refjson", JSON.toJSONString(commonname.getRefjson()))
                            .addValue("sourcesid", commonname.getSourcesid())
                            .addValue("expert", commonname.getExpert())
                            .addValue("remark", commonname.getRemark())
                            .addValue("inputer", commonname.getInputer())
                            .addValue("inputtime", commonname.getInputtime())
                            .addValue("status", commonname.getStatus())
                            .addValue("synchdate", commonname.getSynchdate())
                            .addValue("synchstatus", commonname.getSynchstatus())
                            .addValue("taxon", commonname.getTaxon().getId())
                            .getValues());
        }
		int[] updateCounts =  namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[commonnames.size()]));
		System.out.println("批量上传条数：" + updateCounts.length);
	}

	@Override
	public void batchInsertProtection(List<Protection> protections) throws Exception {
		String sql = "INSERT INTO protection "
				+ "(id, protlevel, proassessment, refjson, sourcesid, expert, remark, status, inputer, inputtime, synchstatus, synchdate, protectstandard_id, taxon_id) "
				+ "VALUES (:id, :protlevel, :proassessment, :refjson, :sourcesid, :expert, :remark, :status, :inputer, :inputtime, :synchstatus, :synchdate, :protectstandard, :taxon)";
		List<Map<String, Object>> batchValues = new ArrayList<>(protections.size());
        for (Protection protection : protections) {
            batchValues.add(
                    new MapSqlParameterSource("id", protection.getId())
                    		.addValue("protlevel", protection.getProtlevel())
                            .addValue("proassessment", protection.getProassessment())
                            .addValue("refjson", JSON.toJSONString(protection.getRefjson()))
                            .addValue("sourcesid", protection.getSourcesid())
                            .addValue("expert", protection.getExpert())
                            .addValue("remark", protection.getRemark())
                            .addValue("inputer", protection.getInputer())
                            .addValue("inputtime", protection.getInputtime())
                            .addValue("status", protection.getStatus())
                            .addValue("synchdate", protection.getSynchdate())
                            .addValue("synchstatus", protection.getSynchstatus())
                            .addValue("protectstandard", protection.getProtectstandard().getId())
                            .addValue("taxon", protection.getTaxon().getId())
                            .getValues());
        }
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[protections.size()]));
		System.out.println("批量上传条数：" + updateCounts.length);
	}
	
	@Override
	public void batchInsertTraitdata(List<Traitdata> traitdatas) throws Exception {
		String sql = "INSERT INTO traitdata "
				+ "(id, trainsetid, traitjson, refjson, sources_id, expert, remark, status, inputer, inputtime, synchstatus, synchdate, taxon_id) "
				+ "VALUES (:id, :trainsetid, :traitjson, :refjson, :sourcesid, :expert, :remark, :status, :inputer, :inputtime, :synchstatus, :synchdate, :taxon)";
		List<Map<String, Object>> batchValues = new ArrayList<>(traitdatas.size());
        for (Traitdata traitdata : traitdatas) {
            batchValues.add(
                    new MapSqlParameterSource("id", traitdata.getId())
                            .addValue("trainsetid", traitdata.getTrainsetid())
                            .addValue("traitjson", JSON.toJSONString(traitdata.getTraitjson()))
                            .addValue("refjson", traitdata.getRefjson())
                            .addValue("sourcesid", traitdata.getSourcesId())
                            .addValue("expert", traitdata.getExpert())
                            .addValue("remark", traitdata.getRemark())
                            .addValue("inputer", traitdata.getInputer())
                            .addValue("inputtime", traitdata.getInputtime())
                            .addValue("status", traitdata.getStatus())
                            .addValue("synchdate", traitdata.getSynchdate())
                            .addValue("synchstatus", traitdata.getSynchstatus())
                            .addValue("taxon", traitdata.getTaxon().getId())
                            .getValues());
        }
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[traitdatas.size()]));
		System.out.println("批量上传条数：" + updateCounts.length);
	}
	
	@Override
	public void batchInsertMultimedia(List<Multimedia> multimedias) {
		String sql = "INSERT INTO multimedia "
				+ "(id, mediatype, context, title, old_path, sourcesid, expert, rightsholder, copyright, lisenceid, path, createtime, creator, publisher, contributor, country, province, city, county, locality, lng, lat, refjson, status, inputer, inputtime, synchstatus, synchdate, license_id, taxon_id) "
				+ "VALUES (:id, :mediatype, :context, :title, :oldPath, :sourcesid, :expert, :rightsholder, :copyright, :lisenceid, :path, :createtime, :creator, :publisher, :contributor, :country, :province, :city, :county, :locality, :lng, :lat, :refjson, :status, :inputer, :inputtime, :synchstatus, :synchdate, :license, :taxon)";
		List<Map<String, Object>> batchValues = new ArrayList<>(multimedias.size());
        for (Multimedia multimedia : multimedias) {
            batchValues.add(
                    new MapSqlParameterSource("id", multimedia.getId())
                            .addValue("mediatype", multimedia.getMediatype())
                            .addValue("context", multimedia.getContext())
                            .addValue("title", multimedia.getTitle())
                            .addValue("oldPath", multimedia.getOldPath())
                            .addValue("rightsholder", multimedia.getRightsholder())
                            .addValue("copyright", multimedia.getCopyright())
                            .addValue("lisenceid", multimedia.getLisenceid())
                            .addValue("path", multimedia.getPath())
                            .addValue("createtime", multimedia.getCreatetime())
                            .addValue("creator", multimedia.getCreator())
                            .addValue("publisher", multimedia.getPublisher())
                            .addValue("contributor", multimedia.getContributor())
                            .addValue("country", multimedia.getCountry())
                            .addValue("province", multimedia.getProvince())
                            .addValue("city", multimedia.getCity())
                            .addValue("county", multimedia.getCounty())
                            .addValue("locality", multimedia.getLocality())
                            .addValue("lng", multimedia.getLng())
                            .addValue("lat", multimedia.getLat())
                            .addValue("refjson", JSON.toJSONString(multimedia.getRefjson()))
                            .addValue("sourcesid", multimedia.getSourcesid())
                            .addValue("expert", multimedia.getExpert())
                            .addValue("inputer", multimedia.getInputer())
                            .addValue("inputtime", multimedia.getInputtime())
                            .addValue("status", multimedia.getStatus())
                            .addValue("synchdate", multimedia.getSynchdate())
                            .addValue("synchstatus", multimedia.getSynchstatus())
                            .addValue("license", multimedia.getLicense())
                            .addValue("taxon", multimedia.getTaxon())
                            .getValues());
        }
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[multimedias.size()]));
		System.out.println("批量上传条数：" + updateCounts.length);
	}

	@Override
	public void updateTaxon(Taxon taxon) {
		String sql = "UPDATE taxon SET sourcesid = :sourcesid, refjson = :refjson, expert = :expert, synchdate = :synchdate WHERE id = :id;";
	    SqlParameterSource ps = new BeanPropertySqlParameterSource(taxon);
	    namedParameterJdbcTemplate.update(sql, ps);
	}

	@Override
	public void updateTaxonHasTaxtree(List<TaxonHasTaxtree> records) {
		/*System.out.println("批量：" + JSON.toJSONString(records));
		String sql = "update taxon_has_taxtree set prev_taxon=? where taxon_id=? and taxtree_id=?";
		BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TaxonHasTaxtree record = records.get(i);
                ps.setString(1, record.getPrevTaxon());
                ps.setString(2, record.getTaxonId());
                ps.setString(3, record.getTaxtreeId());
			}
			
			public int getBatchSize() {
                return records.size();
            }
		};
        jdbcTemplate.batchUpdate(sql, bpss);*/
		
		String sql = "UPDATE taxon_has_taxtree SET prev_taxon = :prevTaxon, refjson = :refjson, expert = :expert, synchdate = :synchdate WHERE id = :id;";
	    SqlParameterSource ps = new BeanPropertySqlParameterSource(records);
	    namedParameterJdbcTemplate.update(sql, ps);
	}

	@Override
	public void batchUpdateTaxon(List<Taxon> taxons) throws Exception {
		String sql = "update taxon set refjson = ? where id = ?";
        jdbctemp.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				for (Taxon taxon : taxons) {
					ps.setString(1, taxon.getRefjson());
					ps.setString(2, taxon.getId());
				}
			}

			@Override
			public int getBatchSize() {
				return taxons.size();
			}
        });
        System.out.println("批量更新条数：" + taxons.size());
	}
	
	@Override
	public void batchUpdateCitation(List<Citation> citations) throws Exception {
		String sql = "update citation set refjson = ? where id = ?";
        jdbctemp.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				for (Citation citation : citations) {
					ps.setString(1, citation.getRefjson());
					ps.setString(2, citation.getId());
				}
			}

			@Override
			public int getBatchSize() {
				return citations.size();
			}
        });
        System.out.println("批量更新条数：" + citations.size());
	}

}
