package com.ningmeng.manage_course.service;

import com.ningmeng.framework.domain.system.SysDictionary;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.manage_course.dao.SysDicthinaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class SysdictionaryService {

    @Resource
    private SysDicthinaryRepository sysDicthinaryRepository;

    //根据字典分类type 查询字典信息
    public SysDictionary getByType(String type) {
        if(StringUtils.isEmpty(type)){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return sysDicthinaryRepository.findByDType(type);
    }


}
