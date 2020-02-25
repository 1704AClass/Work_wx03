package com.ningmeng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.ningmeng.filesystem.dao.FileSystemRepository;
import com.ningmeng.framework.domain.filesystem.FileSystem;
import com.ningmeng.framework.domain.filesystem.response.FileSystemCode;
import com.ningmeng.framework.domain.filesystem.response.UploadFileResult;
import com.ningmeng.framework.exception.CustomExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class FileSystemService {

    @Value("${ningmeng.fastdfs.connect_timeout_in_seconds}")
    private int  connect_timeout_in_seconds;
    @Value("${ningmeng.fastdfs.network_timeout_in_seconds}")
    private int network_timeout_in_seconds;
    @Value("${ningmeng.fastdfs.charset}")
    private String  charset;
    @Value("${ningmeng.fastdfs.tracker_servers}")
    private String  tracker_servers;

    @Autowired
    private FileSystemRepository repository;

    private void initfdfsconfig(){
       try {
           ClientGlobal.initByTrackers(tracker_servers);
           ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
           ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
           ClientGlobal.setG_charset(charset);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    /**
     * 上传文件
     * @param file
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    @Transactional
    public UploadFileResult upload(MultipartFile file, String filetag, String businesskey, String metadata) {

        //1.上传到fastdfs上，上传之后返回一个文件路径（file_id）
        String file_id = fdfs_upload(file);
        if(file_id==null){
            CustomExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        // 2.将file_id保存到文件系统数据库中
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(file_id);
        fileSystem.setFilePath(file_id);
        fileSystem.setFileSize(file.getSize());
        fileSystem.setFileName(file.getOriginalFilename());
        fileSystem.setFileType(file.getContentType());
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        if (StringUtils.isNotEmpty(metadata)){
            try {
                Map map= JSON.parseObject(metadata, Map.class);
                fileSystem.setMetadata(map);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        repository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }

    /**
     * 上传方法
     */
    private String fdfs_upload(MultipartFile file){
       try {
           initfdfsconfig();
           TrackerClient trackerClient=new TrackerClient();
           TrackerServer trackerServer = trackerClient.getConnection();
           StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
           StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
           byte[] bytes = file.getBytes();
           String originalFilename = file.getOriginalFilename();
           String extname = originalFilename.substring(originalFilename.lastIndexOf("."));
           String file_id = storageClient1.upload_file1(bytes, extname, null);
            return file_id;
       }catch (Exception e){
           e.printStackTrace();
       }
        return null;


    }
}
