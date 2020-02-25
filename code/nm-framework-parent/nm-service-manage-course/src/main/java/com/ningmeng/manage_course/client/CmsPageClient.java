package com.ningmeng.manage_course.client;

import com.ningmeng.framework.client.NmServiceList;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.model.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = NmServiceList.nm_SERVICE_MANAGE_CMS)
public interface CmsPageClient {

    @PostMapping("/cms/add")
    public ResponseResult add(@RequestBody CmsPage cmsPage);

}
