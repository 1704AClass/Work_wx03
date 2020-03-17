package com.ningmeng.manage_media.controller;

import com.ningmeng.api.media.MediaFileControllerApi;
import com.ningmeng.framework.domain.course.TeachplanMedia;
import com.ningmeng.framework.domain.media.request.QueryMediaFileRequest;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_media.service.MediaFileService;
import com.ningmeng.manage_media.service.MediaUploadService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {

    @Resource
    private MediaFileService mediaFileService;

    @Resource
    private MediaUploadService mediaUploadService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int size,
                                        @RequestBody QueryMediaFileRequest queryMediaFileRequest) {
        //媒体文件查询
        return mediaFileService.findList(page,size,queryMediaFileRequest);
    }






}
