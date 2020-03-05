package com.ningmeng.manage_media.dao;

import com.ningmeng.framework.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MediaFileRepository extends MongoRepository<MediaFile,String> {
}
