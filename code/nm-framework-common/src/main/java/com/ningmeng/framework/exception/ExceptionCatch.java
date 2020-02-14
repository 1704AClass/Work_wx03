package com.ningmeng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class ExceptionCatch {
    private static final Logger LOGGER =LoggerFactory.getLogger(ExceptionCatch.class);

    //key 异常类 value 错误码以及信息;
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> immutableMap;

    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    static {
        //以后在这维护错误类型
        builder.put(HttpMessageNotReadableException.class, CommonCode.HTTP_ERROR);
    }

    //捕获customException异常
    @ExceptionHandler
    @ResponseBody
    public ResponseResult customException(CustomException e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(),e);
        return new ResponseResult(e.getResultCode());
    }
    //非自定义异常
    @ExceptionHandler
    @ResponseBody
    public ResponseResult exception(Exception e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(),e);
        if (immutableMap == null){
            //构建map集合；
            immutableMap = builder.build();

        }
        ResultCode  resultCode = immutableMap.get(e.getClass());
        if (resultCode!=null){
            return new ResponseResult(resultCode);
        }

        return new ResponseResult(CommonCode.SERVER_ERROR);
    }
}