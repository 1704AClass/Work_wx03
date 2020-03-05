package com.ningmeng.framework.domain.cms.response;

import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class CmsPostPageResult  extends ResponseResult {
    String pageUrl;

    public  CmsPostPageResult (ResultCode resultCode,String pageUrl){
        super(resultCode);
        this.pageUrl=pageUrl;
    }
}
