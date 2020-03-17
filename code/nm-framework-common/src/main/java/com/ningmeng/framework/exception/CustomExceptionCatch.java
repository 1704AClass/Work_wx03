package com.ningmeng.framework.exception;

import com.ningmeng.framework.model.response.CommonCode;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }

}
