package org.big.common;

import java.sql.Timestamp;

import org.big.entity.*;

/**
 *<p><b>构造实体类</b></p>
 *<p> 将传入的java对象对应至已有的Entity实体</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/12/04 21:58</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class BuildEntity {
	/**
     *<b>构造TeamEntity</b>
     *<p> 将传入的java对象对应至已有的TeamEntity实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisIdrecord（Idrecord）
     */
    public static String buildUserInfoString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[0]+"<"+(String)objs[1]+">";
    }
    public static String buildEmailString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[1];
    }
    public static String buildString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[1];
    }
    /**
     *<b>构造保护级别</b>
     *<p> 将传入的java对象对应至String字符串</p>
     * @author BINZI
     * @param obj 传入的Object对象
     * @return 
     */
    public static String buildProtlevel(Object obj){
    	Object[] objs = (Object[]) obj;
    	return (String)objs[0];
    }

    /**
     *<b>构造TeamEntity</b>
     *<p> 将传入的java对象对应至已有的TeamEntity实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisTeam（Team）
     */
    public static Team buildTeam(Object obj){
        Team thisTeam=new Team();
        Object[] objs = (Object[]) obj;
        thisTeam.setId((String)objs[0]);
        thisTeam.setName((String)objs[1]);
        thisTeam.setLeader((String)objs[3]+"("+(String)objs[2]+")");
        thisTeam.setNote((String)objs[4]);
        thisTeam.setAdddate((Timestamp) objs[5]);
        return thisTeam;
    }

    /**
     *<b>构造已上传的专家信息</b>
     *<p> 将传入的java对象对应至构造已上传的专家信息实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisUploadedExpert（Expert）
     */
    public static UploadedExpert buildUploadedExpert(Object obj){
        UploadedExpert thisUploadedExpert=new UploadedExpert();
        Object[] objs = (Object[]) obj;
        thisUploadedExpert.setNum((String)objs[0]);
        Expert thisExpert= (Expert) objs[1];
        thisUploadedExpert.setId(thisExpert.getId());
        thisUploadedExpert.setCnAddress(thisExpert.getCnAddress());
        thisUploadedExpert.setCnCompany(thisExpert.getCnCompany());
        thisUploadedExpert.setCnHomePage(thisExpert.getCnHomePage());
        thisUploadedExpert.setCnName(thisExpert.getCnName());
        thisUploadedExpert.setEnAddress(thisExpert.getEnAddress());
        thisUploadedExpert.setEnCompany(thisExpert.getEnCompany());
        thisUploadedExpert.setEnHomePage(thisExpert.getEnHomePage());
        thisUploadedExpert.setEnName(thisExpert.getEnName());
        thisUploadedExpert.setExpEmail(thisExpert.getExpEmail());
        thisUploadedExpert.setExpInfo(thisExpert.getExpInfo());
        thisUploadedExpert.setInputer(thisExpert.getInputer());
        thisUploadedExpert.setInputtime((Timestamp) thisExpert.getInputtime());
        thisUploadedExpert.setStatus(thisExpert.getStatus());
        thisUploadedExpert.setSynchdate((Timestamp) thisExpert.getSynchdate());
        thisUploadedExpert.setSynchstatus(thisExpert.getSynchstatus());
        return thisUploadedExpert;
    }

    /**
     *<b>构造已上传的专家信息</b>
     *<p> 将传入的java对象对应至构造已上传的专家信息实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisUploadedExpert（Expert）
     */
    public static UploadedRef buildUploadedRef(Object obj){
        UploadedRef thisUploadedRef=new UploadedRef();
        Object[] objs = (Object[]) obj;
        thisUploadedRef.setNum((String)objs[0]);
        Ref thisRef= (Ref) objs[1];
        thisUploadedRef.setId(thisRef.getId());
        thisUploadedRef.setPtype(thisRef.getPtype());
        thisUploadedRef.setRefstr(thisRef.getRefstr());
        thisUploadedRef.setAuthor(thisRef.getAuthor());
        thisUploadedRef.setPyear(thisRef.getPyear());
        thisUploadedRef.setTitle(thisRef.getTitle());
        thisUploadedRef.setJournal(thisRef.getJournal());
        thisUploadedRef.setrVolume(thisRef.getrVolume());
        thisUploadedRef.setrPeriod(thisRef.getrPeriod());
        thisUploadedRef.setRefs(thisRef.getRefs());
        thisUploadedRef.setRefe(thisRef.getRefe());
        thisUploadedRef.setIsbn(thisRef.getIsbn());
        thisUploadedRef.setPlace(thisRef.getPlace());
        thisUploadedRef.setPress(thisRef.getPress());
        thisUploadedRef.setTranslator(thisRef.getTranslator());
        thisUploadedRef.setKeywords(thisRef.getKeywords());
        thisUploadedRef.setTpage(thisRef.getTpage());
        thisUploadedRef.setTchar(thisRef.getTchar());
        thisUploadedRef.setLanguages(thisRef.getLanguages());
        thisUploadedRef.setOlang(thisRef.getOlang());
        thisUploadedRef.setVersion(thisRef.getVersion());
        thisUploadedRef.setRemark(thisRef.getRemark());
        thisUploadedRef.setLeftstr(thisRef.getLeftstr());
        thisUploadedRef.setrLink(thisRef.getrLink());
        thisUploadedRef.setInputer(thisRef.getInputer());
        thisUploadedRef.setInputtime((Timestamp) thisRef.getInputtime());
        thisUploadedRef.setStatus(thisRef.getStatus());
        thisUploadedRef.setSynchdate((Timestamp) thisRef.getSynchdate());
        thisUploadedRef.setSynchstatus(thisRef.getSynchstatus());
        return thisUploadedRef;
    }
    
    /**
     *<b>构造已上传的数据源信息</b>
     *<p> 将传入的java对象对应至构造已上传的数据源信息实体</p>
     * @author BINZI
     * @param obj 传入的Object对象
     * @return thisUploadedDatasource（Datasource）
     */
    public static UploadedDatasource buildUploadedDatasource(Object obj){
        UploadedDatasource thisUploadedDatasource=new UploadedDatasource();
        Object[] objs = (Object[]) obj;
        thisUploadedDatasource.setNum((String)objs[0]);
        Datasource thisDatasource= (Datasource) objs[1];
        thisUploadedDatasource.setId(thisDatasource.getId());
        thisUploadedDatasource.setTitle(thisDatasource.getTitle());
        thisUploadedDatasource.setdType(thisDatasource.getdType());
        thisUploadedDatasource.setVersions(thisDatasource.getVersions());
        thisUploadedDatasource.setCreater(thisDatasource.getCreater());
        thisUploadedDatasource.setCreatetime(thisDatasource.getCreatetime());
        thisUploadedDatasource.setdAbstract(thisDatasource.getdAbstract());
        thisUploadedDatasource.setdKeyword(thisDatasource.getdKeyword());
        thisUploadedDatasource.setdLink(thisDatasource.getdLink());
        thisUploadedDatasource.setdVerifier(thisDatasource.getdVerifier());
        thisUploadedDatasource.setdRightsholder(thisDatasource.getdRightsholder());
        thisUploadedDatasource.setdCopyright(thisDatasource.getdCopyright());
        thisUploadedDatasource.setInfo(thisDatasource.getInfo());
        thisUploadedDatasource.setInputer(thisDatasource.getInputer());
        thisUploadedDatasource.setInputtime((Timestamp) thisDatasource.getInputtime());
        thisUploadedDatasource.setStatus(thisDatasource.getStatus());
        thisUploadedDatasource.setSynchdate((Timestamp) thisDatasource.getSynchdate());
        thisUploadedDatasource.setSynchstatus(thisDatasource.getSynchstatus());
        return thisUploadedDatasource;
    }
    /**
     *<b>构造MessageEntity</b>
     *<p> 将传入的java对象对应至已有的MessageEntity实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisMessage（Message）
     */
    public static Message buildMessage(Object obj){
        Message thisMessage=new Message();
        Object[] objs = (Object[]) obj;
        thisMessage.setId((String)objs[0]);
        thisMessage.setSender((String)objs[1]);
        thisMessage.setAddressee((String)objs[2]);
        thisMessage.setSendtime((Timestamp)objs[3]);
        thisMessage.setTitle((String) objs[4]);
        thisMessage.setText((String) objs[5]);
        thisMessage.setStatus((int) objs[6]);
        thisMessage.setType((String) objs[7]);
        thisMessage.setMark((String) objs[8]);
        return thisMessage;
    }
    /**
     *<b>构造TaxonEntity</b>
     *<p> 将传入的java对象对应至已有的TaxonEntity实体</p>
     * @author BINZI
     * @param obj 传入的Object对象
     * @return thisTaxon（Taxon）
     */
    public static Taxon buildTaxon(Object obj){
    	Taxon thisTaxon = new Taxon();
    	Object[] objs = (Object[]) obj;
    	thisTaxon.setId((String)objs[0]);
    	thisTaxon.setScientificname((String)objs[1]);
    	thisTaxon.setChname((String)objs[2]);
    	return thisTaxon;
    }
}
