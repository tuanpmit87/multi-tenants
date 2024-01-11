package com.multitenant.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler
    ) {
        String dbHeaderValue = request.getHeader("tenantName");
        if (dbHeaderValue != null) {
            RoutingDataSource.setDataSourceKey(dbHeaderValue);
        }
        return true;
    }

    @Override
    public void postHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler,
            ModelAndView modelAndView
    ) {
        RoutingDataSource.clearDataSourceKey();
    }

    @Override
    public void afterCompletion(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler,
            Exception ex
    ) {
        RoutingDataSource.clearDataSourceKey();
    }
}
