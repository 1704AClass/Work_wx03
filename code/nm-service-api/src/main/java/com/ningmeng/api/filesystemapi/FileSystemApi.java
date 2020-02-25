package com.ningmeng.api.filesystemapi;


import com.ningmeng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件系统服务接口",description = "提供文件系统服务接口常规操作")
public interface FileSystemApi {

    @ApiOperation(value = "文件上传接口")
    UploadFileResult upload(MultipartFile file,String filetag,String businesskey,String metadata);
}
