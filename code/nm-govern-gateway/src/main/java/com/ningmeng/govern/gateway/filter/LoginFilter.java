package com.ningmeng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.govern.gateway.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFilter extends ZuulFilter {


    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

    @Resource
    private AuthService authService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        //int值来定义过滤器的执行顺序，数值越小优先级越高
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //该过滤器需要执行
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //请求对象
        HttpServletRequest request = requestContext.getRequest();
        //查询身份令牌
        String access_token = authService.getTokenFromCookie(request);
        if (access_token == null){
            //拒绝访问
            this.access_denied();
        }
        //从redis效验身份令牌是否过期
        long expire = authService.getExpire(access_token);
        if (expire <= 0){
            //拒绝访问
            this.access_denied();
        }
        //查询jwt令牌
        String jwt = authService.getJwtFromHeader(request);
        if (jwt == null){
            //拒绝访问
            this.access_denied();
        }
        return null;
    }

    //拒绝访问
    private void access_denied(){
        //上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.setSendZuulResponse(false);//拒绝访问
        ResponseResult unauthenticated = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String jsonString = JSON.toJSONString(unauthenticated);
        requestContext.setResponseBody(jsonString);
        requestContext.setResponseStatusCode(200);//设置响应状态码

        HttpServletResponse response = requestContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
    }





}
