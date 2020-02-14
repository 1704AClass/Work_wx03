package com.ningmeng.manage_cms.dao;

import com.ningmeng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface cmsPageRepository extends MongoRepository<CmsPage,String> {
    CmsPage findByPageNameEquals(String pageName);
    CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId,String pageName,String pageWebPath);
}
