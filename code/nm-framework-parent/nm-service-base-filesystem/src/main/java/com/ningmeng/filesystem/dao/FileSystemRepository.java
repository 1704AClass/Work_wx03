package com.ningmeng.filesystem.dao;

import com.ningmeng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileSystemRepository extends MongoRepository<FileSystem,String> {

}
