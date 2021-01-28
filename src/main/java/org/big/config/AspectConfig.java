package org.big.config;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.big.common.IdentityVote;
import org.big.entity.Taxon;
import org.big.entity.Taxtree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *<p><b>切面配置类</b></p>
 *<p> 通过该类对方法进行拦截、记录等操作</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/28 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Aspect // 声明这是一个切面。必须的！
@Component // 让此切面成为Spring容器管理的Bean 
public class AspectConfig {
    @Autowired
    private HttpServletRequest request;
    
    @Pointcut("execution( * org.big.controller.rest.TeamRestController.Remove*(..)) " )
    public void RemoveTeam(){} 
    
    @Pointcut("execution( * org.big.controller.TeamController.Edit(..)) " )
    public void EditTeam(){} 
    
    @Pointcut("execution( * org.big.controller.TeamController.Add(..)) " )
    public void Invite(){} 
    
    @Pointcut("execution( * org.big.service.*.addTaxonBaseInfo*(..)) " )
    public void addTaxonBaseInfo(){} 
    
    @Pointcut("execution( * org.big.service.TaxtreeServiceImpl.updateOneById(..)) " )
    public void updateOneByTree(){} 
    
    /**
     *<b>删除拦截</b>
     *<p> 对service下的所有删除方法进行权限判断验证，符合权限则放行、不符合权限则终止此方法并将错误信息写入Session</p>
     * @author WangTianshan (王天山)
     * @param joinPoint
     * @param objectId 被删除的实体的id
     * @return java.lang.Object
     */
    @Around("execution(* org.big.service.*.logicRemove001(..)) && args(objectId)")	// 环绕通知
    public Object canReove(JoinPoint joinPoint,String objectId){
        ProceedingJoinPoint pjp = (ProceedingJoinPoint) joinPoint;
        IdentityVote thisIdentityVote=new IdentityVote();
        String targetName = pjp.getSignature().getDeclaringTypeName();
        //System.out.println(pjp.getTarget().getClass().getName());
        //targetName=targetName.substring(16);
        /*targetName=targetName.replace("org.big.service.","");*/
        if(thisIdentityVote.hasAuthority(targetName, objectId)){
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                request.getSession().setAttribute("operationError","authority");
                throwable.printStackTrace();
            }
            return false;
        }else{
            request.getSession().setAttribute("operationError","authority");
            return false;
        }
    }
    
    /**
     *<b>团队删除拦截</b>
     *<p>团队删除拦截</p>
     * @author MengMeng (王孟豪)
     * @param ProceedingJoinPoint
     * @return java.lang.Object
     */
    @Around("RemoveTeam()")
    public Object RemoveByTeam(ProceedingJoinPoint pjp){
        Object result = null; 
        Object[] method_args = pjp.getArgs();
        IdentityVote thisIdentityVote=new IdentityVote();
        
        String id = null;
        id= String.valueOf(method_args[0]);
        
        String [] ids = id.split("￥");
        Boolean flag = false;
        if(ids.length == 1){
        	flag = thisIdentityVote.isTeamLeaderByTeamId(id);
        }else{
        	for (int i = 0; i < ids.length; i++) {
        		flag = thisIdentityVote.isTeamLeaderByTeamId(ids[i]);
        		if(!flag)
        			break;
			}
        }
        
		if(!flag){
			result = false;
		}else{
			try {
	            result =pjp.proceed();
	        } catch (Throwable e) {
	            e.printStackTrace();
	        } 
		}
        return result;
    }
    
    /**
     *<b>团队修改拦截</b>
     *<p>团队修改拦截</p>
     * @author MengMeng (王孟豪)
     * @param ProceedingJoinPoint
     * @return java.lang.Object
     */
    @Around("EditTeam()")
    public Object EditByTeam(ProceedingJoinPoint pjp){
        Object result = null; 
        Object[] method_args = pjp.getArgs();
        IdentityVote thisIdentityVote=new IdentityVote();
        
        String id = null;
        id= String.valueOf(method_args[1]);
        
		if(!thisIdentityVote.isTeamLeaderByTeamId(id)){
			result = "redirect:/console/team/permission";
		}else{
			try {
	            result =pjp.proceed();
	        } catch (Throwable e) {
	            e.printStackTrace();
	        } 
		}
        return result;
    }
    
    /**
     *<b>邀请拦截</b>
     *<p>邀请拦截</p>
     * @author MengMeng (王孟豪)
     * @param ProceedingJoinPoint
     * @return java.lang.Object
     */
    @Around("Invite()")
    public Object InviteByTeam(ProceedingJoinPoint pjp){
        Object result = null; 
        Object[] method_args = pjp.getArgs();
        IdentityVote thisIdentityVote=new IdentityVote();
        
        String id = null;
        id= String.valueOf(method_args[0]);
        
		if(!thisIdentityVote.isTeamLeaderByTeamId(id)){
			result = "redirect:/console/team/permission";
		}else{
			try {
	            result =pjp.proceed();
	        } catch (Throwable e) {
	            e.printStackTrace();
	        } 
		}
        return result;
    }
    
    /**
     *<b>统一数据集和分类单元集更新时间</b>
     *<p> 对service下的所有更新方法进行控制,统一数据集和分类单元集更新时间</p>
     * @author MengMeng (王孟豪)
     * @param joinPoint
     * @return java.lang.Object
     */
    @After("addTaxonBaseInfo()")
    public Object AddTaxonBaseInfo(JoinPoint joinPoint){
        ProceedingJoinPoint pjp = (ProceedingJoinPoint) joinPoint;
        IdentityVote thisIdentityVote=new IdentityVote();
        String targetName = pjp.getSignature().getDeclaringTypeName();
        Object result = null;
        
        Object[] args = pjp.getArgs();  
        Taxon thisTaxon = (Taxon) args[0];
       
        if(thisIdentityVote.isTaxonTime(targetName, thisTaxon.getTaxaset().getId())){
            try {
            	result =  pjp.proceed();
            } catch (Throwable throwable) {
                request.getSession().setAttribute("UpdateError","更新时间错误");
                throwable.printStackTrace();
            }
            return result;
        }
        return result;
    }
    
    /**
     *<b>统一数据集和分类树集更新时间</b>
     *<p> 对service下的所有更新方法进行控制,统一数据集和分类树集更新时间</p>
     * @author MengMeng (王孟豪)
     * @param joinPoint
     * @return java.lang.Object
     */
    @After("updateOneByTree()")
    public Object UpdateBytaxtree(JoinPoint joinPoint){
        ProceedingJoinPoint pjp = (ProceedingJoinPoint) joinPoint;
        IdentityVote thisIdentityVote=new IdentityVote();
        String targetName = pjp.getSignature().getDeclaringTypeName();
        Object result = null;
        
        Object[] args = pjp.getArgs();  
        Taxtree thisTaxtree = (Taxtree) args[0];
       
        if(thisIdentityVote.isTaxonTime(targetName,thisTaxtree.getDataset().getId())){
            try {
            	result =  pjp.proceed();
            } catch (Throwable throwable) {
                request.getSession().setAttribute("UpdateError","更新时间错误");
                throwable.printStackTrace();
            }
            return result;
        }
        return result;
    }
}