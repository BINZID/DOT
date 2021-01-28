package org.big.controller.rest;

import org.apache.commons.lang.StringUtils;
import org.big.common.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@RestController
public class FileRestController {
	@Autowired
    private FastDFSUtil fastDFSUtil;
    /**
     * 文件上传
     */
    @ApiOperation(value="上传文件", notes="测试FastDFS文件上传")
    @RequestMapping(value = "/uploadFile",headers="content-type=multipart/form-data", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile (@RequestParam("file") MultipartFile file){
        String result ;
        try{
            String path = fastDFSUtil.upload(file) ;
            if (!StringUtils.isEmpty(path)){
                result = path ;
            } else {
                result = "上传失败" ;
            }
        } catch (Exception e){
            e.printStackTrace() ;
            result = "服务异常" ;
        }
        return ResponseEntity.ok(result);
    }
    /**
     * 文件删除
     */
    @RequestMapping(value = "/deleteByPath", method = RequestMethod.GET)
    public ResponseEntity<String> deleteByPath (){
        String filePathName = "group1/M00/00/00/wKhIgl0n4AKABxQEABhlMYw_3Lo825.png" ;
        fastDFSUtil.deleteFile(filePathName);
        return ResponseEntity.ok("SUCCESS") ;
    }
}