package org.big.controller.rest;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.big.service.MultimediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Multimedia相关的Controller的Rest风格类</b></p>
 *<p> Multimedia相关的Controller的Rest风格类</p>
 * @author  BINZI
 *<p>Created date: 2018/07/13 11:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping("/console/multimedia/rest")
public class MultimediaRestController {
	@Autowired
	private MultimediaService multimediaService;
	
	private String uploadPath = "upload/";
	
	/**
	 *<b>Multimedia的已上传listJSON</b>
	 *<p> Multimedia的已上传listJSON</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @param timestamp
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{timestamp}")
	public JSON list(HttpServletRequest request, @PathVariable Timestamp timestamp) {
		return this.multimediaService.findUploadedMultimediaList(timestamp, request);
	}
	
	/**
	 * <b>Multimedia添加</b>
	 * <p> Multimedia添加</p>
	 * @param request
	 * @param id
	 * @return com.alibaba.fastjson.JSON
	 */
	@PostMapping(value="/add/{id}")
	public JSON addMultimedia(HttpServletRequest request, @PathVariable String id) {
		return this.multimediaService.addMultimedia(id, request);
	}
	
	/**
	 * <b>Multimedia修改</b>
	 * <p> Multimedia修改</p>
	 * @param request
	 * @param id
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value="/edit/{id}")
	public JSON editMultimedia(@PathVariable String id) {
		return this.multimediaService.editMultimedia(id);
	}

	/**
	 * <b>根据Id批量逻辑删除指定Multimedia</b>
	 * <p> 根据Id批量逻辑删除指定Multimedia</p>
	 * @param request
	 * @return 
	 */
	@GetMapping(value = "/removeMany/{ids}")
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.multimediaService.logicRemove(id)) {
					isRemove = isRemove + 1;
				}
			}
			return isRemove;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * <b>根据Id单个逻辑删除指定Multimedia</b>
	 * <p> 根据Id单个逻辑删除指定Multimedia</p>
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/remove/{id}")
	public boolean remove(@PathVariable String id) {
		try {
			return this.multimediaService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     *<b>Multimedia信息添加后的删除</b>
     *<p> Multimedia信息添加后的删除</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@PostMapping(value = "/delete")
	public boolean delete(HttpServletRequest request){
		return this.multimediaService.deleteOne(request);
	}

	/**
     *<b>根据TaxonId查找对应Taxon下的所有Multimedia</b>
     *<p> 根据TaxonId查找对应Taxon下的所有Multimedia</p>
     * @author BINZI
	 * @param request
	 * @param id
     * @return 
     */
	@GetMapping("/multimediaList/{id}")
	public JSON multimediaList(HttpServletRequest request,@PathVariable String id) {
		return this.multimediaService.findMultimediaListByTaxonId(id, request);
	}
	
	/**
     *<b>文件上传（批量）</b>
     *<p> 文件上传</p>
     * @author BINZI
	 * @param id
	 * @param file
	 * @param request
	 * @param response
	 * @return com.alibaba.fastjson.JSON
	 * @throws IOException
	 */
	@PostMapping(value = "/batch/{id}")
	public JSON addImages(@PathVariable String id, @RequestParam("upload-fa[]") MultipartFile file, HttpServletRequest request)
            throws IOException {
        return multimediaService.batchImages(id, request, file, uploadPath + "images/");
    }
	
	/**
	 * <b>解析导入的Multimedia相关的Excel文件</b>
	 * <p> 解析导入的Multimedia相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @param taxasetId
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload/{taxasetId}")
    public JSON upload(@RequestParam("uploadMultimedia") MultipartFile file, @PathVariable String taxasetId, HttpServletRequest request) throws Exception{
		return this.multimediaService.uploadFile(file, taxasetId, request);
	}
	
	/**
	 * <b>图片上传之切换实体背景图片</b>
	 * <p> 图片上传之切换实体背景图片</p>
	 * @param file
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/changeBg")
	public JSON changeBg(@RequestParam("changeBg") MultipartFile file, HttpServletRequest request)
            throws IOException {
		JSON changeBg = multimediaService.changeBg(request, file, uploadPath + "entityBg/");
        return multimediaService.changeBg(request, file, uploadPath + "entityBg/");
    }
}
