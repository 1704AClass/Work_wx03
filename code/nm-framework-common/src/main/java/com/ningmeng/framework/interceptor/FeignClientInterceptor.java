package com.ningmeng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class FeignClientInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {

        try {
            //使用RequestContextHolder 工具获取request 相关变量
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null){
                //取出request
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if(headerNames != null){
                    while (headerNames.hasMoreElements()){
                        String name = headerNames.nextElement();
                        String value = request.getHeader(name);
                        if (name.equals("authorization")){
                            System.out.println("name = " + name + "values = " + value);
                            requestTemplate.header(name,value);
                        }
                    }
                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }




    }





}
