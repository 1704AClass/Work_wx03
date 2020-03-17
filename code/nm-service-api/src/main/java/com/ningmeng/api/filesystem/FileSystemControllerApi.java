package com.ningmeng.api.filesystem;

import com.ningmeng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件管理系统",description = "管理文件的上传下载服务")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件")
    public UploadFileResult update(MultipartFile file,String filetag,
                                   String businesskey,String metadata);


}
