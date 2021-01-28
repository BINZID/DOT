package org.big.controller;

import java.util.List;

import org.big.service.CommonnameService;
import org.big.service.DistributiondataService;
import org.big.service.ExpertService;
import org.big.service.RefService;
import org.big.service.TaxonService;
import org.big.sp2000.entity.CommonName;
import org.big.sp2000.entity.Distribution;
import org.big.sp2000.entity.Family;
import org.big.sp2000.entity.Reference;
import org.big.sp2000.entity.ReferenceLink;
import org.big.sp2000.entity.ScientificName;
import org.big.sp2000.entity.Specialist;
import org.big.entityColChina.ResultMsg;
import org.big.entityColChina.ResultStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private RefService refService;
	@Autowired
	private DistributiondataService distributiondataService;
	@Autowired
	private CommonnameService commonnameService;
	@Autowired
	private ExpertService expertService;
	/**
	 * @Description 某用户的所有参考文献
	 * @param userId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getRefsByUserId/{inputer}")
	public Object getRefsByUserId(@PathVariable String inputer) {
		ResultMsg resultMsg = null;
		try {
			List<Reference> list = refService.findRefByInputerTurnToReference(inputer);
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg() + "," + e.getMessage(), null);
		}
		return resultMsg;
	}
	
	/**
	 * 
	 * @Description 某分类单元集下的所有高阶元taxon
	 * @param taxasetId
	 * @param taxtreeId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getFamilyDataByTaxaset/{taxasetId}/{taxtreeId}")
	public Object getFamilyDataByTaxaset(@PathVariable String taxasetId,@PathVariable String taxtreeId) {
		ResultMsg resultMsg = null;
		try {
			List<Family> list = taxonService.getFamilyDataByTaxaset(taxasetId,taxtreeId);
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
			System.out.println("空指针异常，无待拷贝数据，查询结果为空");
		}
		return resultMsg;
	}
	
	/**
	 * 
	 * @Description 某分类单元集下的所有低阶元taxon
	 * @param taxasetId
	 * @param taxtreeId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getScientificNamesByTaxaset/{taxasetId}/{taxtreeId}")
	public Object getScientificNamesByTaxaset(@PathVariable String taxasetId,@PathVariable String taxtreeId) {
		ResultMsg resultMsg = null;
		try {
			List<ScientificName> list = taxonService.getScientificNamesByTaxaset(taxasetId,taxtreeId);
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
			System.out.println("空指针异常，无待拷贝数据，查询结果为空");
		}
		return resultMsg;
	}
	
	/**
	 * 
	 * @Description 某分类单元集下的所有分布数据
	 * @param taxasetId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getDistributionByTaxaset/{taxasetId}")
	public Object getDistributionByTaxaset(@PathVariable String taxasetId) {
		ResultMsg resultMsg = null;
		try {
			List<Distribution> list = distributiondataService.getDistributionByTaxaset(taxasetId);
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
			System.out.println("空指针异常，无待拷贝数据，查询结果为空");
		}
		
		return resultMsg;
	}
	
	/**
	 * 
	 * @Description 某分类单元集下的所有分布数据
	 * @param taxasetId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getDistributionForFishByTaxaset/{taxasetId}")
	public Object getDistributionForFishByTaxaset(@PathVariable String taxasetId) {
		ResultMsg resultMsg = null;
		try {
			List<Distribution> list = distributiondataService.getDistributionByTaxasetForFish(taxasetId);
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
			System.out.println("空指针异常，无待拷贝数据，查询结果为空");
		}
		return resultMsg;
	}
	
	/**
	 * 
	 * @Description 某分类单元集下的所有俗名数据
	 * @param taxasetId
	 * @return
	 * @author BINZI
	 */
	@GetMapping("/getCommonNameByTaxaset/{taxasetId}")
	public Object getCommonnameByTaxaset(@PathVariable String taxasetId) {
		ResultMsg resultMsg = null;
		try {
			List<CommonName> list = this.commonnameService.getCommonNameByTaxaset(taxasetId);
			System.out.println("数据量：" + list.size());
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
			System.out.println("空指针异常，无待拷贝数据，查询结果为空");
		}
		return resultMsg;
	}
	/**
	 * 
	 * @Description 某分类单元集下的所有专家数据
	 * @param taxasetId
	 * @return
	 * @author BINZI
	 */
	@GetMapping("/getExpert")
	public Object getExpert() {
		ResultMsg resultMsg = null;
		try {
			List<Specialist> list = this.expertService.getExpertInFo();
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
			System.out.println("空指针异常，无待拷贝数据，查询结果为空");
		}
		return resultMsg;
	}	
	/**
	 * 
	 * @Description 某分类单元集下的所有taxon及相关实体的参考文献
	 * @param refLinks
	 * @return
	 * @author BINZI
	 */
	@GetMapping("/getRefLinksByTaxaset/{taxasetId}")
	public Object getRefLinksByTaxaset(@PathVariable String taxasetId) {
		ResultMsg resultMsg = null;
		try {
			List<ReferenceLink> list = this.refService.getReferenceLinkByTaxaset(taxasetId);
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
			System.out.println("空指针异常，无待拷贝数据，查询结果为空");
		}
		return resultMsg;
	}
}
