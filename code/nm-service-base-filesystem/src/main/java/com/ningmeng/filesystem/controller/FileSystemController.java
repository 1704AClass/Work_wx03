package com.ningmeng.filesystem.controller;

import com.ningmeng.api.filesystem.FileSystemControllerApi;
import com.ningmeng.filesystem.service.FileSystemService;
import com.ningmeng.framework.domain.filesystem.response.UploadFileResult;
import com.ningmeng.framework.model.response.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {

    @Resource
    private FileSystemService fileSystemService;

    @Override
    @PostMapping("/upload")
    public UploadFileResult update(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "filetag",required = true) String filetag,
                                   @RequestParam(value = "businesskey",required = false) String businesskey,
                                   @RequestParam(value = "metadata",required = false) String metadata) {
        return fileSystemService.upload(file,filetag,businesskey,metadata);
    }

    @Override
    public ResponseResult addCoursePic(String courseId, String pic) {
        return null;
    }
}
