package com.alpaca.infrastructure.runtime.corssdomain;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:06 2019/3/13
 * @Description：
 * @Modified By：
 */
@Order(1)
@Configuration
@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
public class CorssDomainFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getHeader(RequestHeaderDefine.ACCESS_CONTROL_REQUEST_METHOD) != null && RequestHeaderDefine.REQUEST_OPTIONS.equals(request.getMethod())) {
            if (response.getHeader("Access-Control-Allow-Origin") == null) {

                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                response.addHeader("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization,SXAUTHID,Origin,X-Powered-By,Pragma,Cache-Control");
                //30 min
                response.addHeader("Access-Control-Max-Age", "1800");
            }
        }
        if(RequestHeaderDefine.REQUEST_OPTIONS.equals( request.getMethod())){
            response.setStatus(200);
            return;
        }
        filterChain.doFilter(request, response);
    }
}

