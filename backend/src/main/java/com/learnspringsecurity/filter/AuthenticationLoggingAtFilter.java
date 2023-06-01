package com.learnspringsecurity.filter;

import jakarta.servlet.*;

import java.io.IOException;
import java.util.logging.Logger;

public class AuthenticationLoggingAtFilter implements Filter {
    private final Logger LOG = Logger.getLogger(AuthenticationLoggingAtFilter.class.getName());
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("Authentication is in progress");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
