package com.rakbow.blog.controller.interceptor;

import com.rakbow.blog.annotation.RootRequired;
import com.rakbow.blog.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-29 23:36
 * @Description:
 */
@Component
public class RootRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RootRequired rootRequired = method.getAnnotation(RootRequired.class);
            if (rootRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }

}
