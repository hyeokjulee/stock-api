package com.stock.api.apilog;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLogAspect {

    private final ApiLogService apiLogService;

    @Around("@annotation(com.stock.api.apilog.ApiLogging)")
    public Object logApiRequest(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        ApiLog apiLog = getApiLog((int) (endTime - startTime));

        apiLogService.saveApiLog(apiLog);

        return result;
    }

    private ApiLog getApiLog(int responseTime) {

        Long userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            userId = (Long) authentication.getPrincipal();
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String method = request.getMethod();
        String url = request.getRequestURL().toString();

        return new ApiLog(userId, method, url, responseTime);
    }
}
