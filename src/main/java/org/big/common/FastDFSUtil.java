package org.big.common;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Component
public class FastDFSUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FastDFSUtil.class);
    @Resource
    private FastFileStorageClient storageClient ;
    /**
     * 上传文件
     */
    public String upload(MultipartFile multipartFile) throws Exception{
        String originalFilename = multipartFile.getOriginalFilename().
                                  substring(multipartFile.getOriginalFilename().
                                  lastIndexOf(".") + 1);
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                              multipartFile.getInputStream(),
                              multipartFile.getSize(),originalFilename , null);
        return storePath.getFullPath() ;
    }
    /**
     * 删除文件
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            //logger.info - 最佳的logger 来源和相关信息。("fileUrl == >>文件路径为空...");
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}