package com.bookmarkmanager.bookmarkmanager.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookmarkmanager.annotation.Auth;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
        if(checkAuthAnnotation(handler, Auth.class)){
            System.out.println("# I'm AuthInterCeptor,,,, ");

        }

        String url = request.getRequestURI();

        return true;
    }

    private boolean checkAuthAnnotation(Object handler, Class clazz){
        //rss, js, html
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        //annotation check
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(handlerMethod.getMethodAnnotation(clazz)!=null ||
            handlerMethod.getBeanType().getAnnotation(clazz)!=null
        ){
            return true;
        }

        return false;



    }

}
