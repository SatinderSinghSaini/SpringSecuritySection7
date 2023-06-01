package com.learnspringsecurity.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//This filter will throw bad request if username contains "test".
//This will execute before BasicAuthentication as configured in ProjectSecurityConfig.
public class RequestValidationBeforeFilter implements Filter {
    public static final String AUTHENTICATION_SCHEME_BASIC = "Basic";
    private Charset credentialsCharset = StandardCharsets.UTF_8;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String header = request.getHeader(AUTHORIZATION);
        if(header !=null){
            header = header.trim();
            if(StringUtils.startsWithIgnoreCase(header,AUTHENTICATION_SCHEME_BASIC)){
                byte[] base64token = header.substring(6).getBytes(credentialsCharset);
                byte[] decode;
                try {
                    decode = Base64.getDecoder().decode(base64token);
                    String token = new String(decode,credentialsCharset);
                    int delim = token.indexOf(":");
                    if(delim == -1){
                        throw new BadCredentialsException("Invalid Basic Authentication Token");
                    }
                    String email = token.substring(0,delim);
                    if(email.toLowerCase().contains("test")){
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                }catch (IllegalArgumentException e){
                    throw new BadCredentialsException("Failed to decode basic authentication token");
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
