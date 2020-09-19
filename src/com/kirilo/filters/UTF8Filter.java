package com.kirilo.filters;

import javax.servlet.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

//@WebFilter("*.xhtml")
public class UTF8Filter implements Filter {
    private String encoding = null;
    private boolean active = false;

    /*    private final Logger logger =
                Logger.getLogger(this.getClass().getName());*/
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        final Logger logger =
                Logger.getLogger(this.getClass().getName());
        this.encoding = filterConfig.getInitParameter("encoding");
        try {
            Charset testCS = Charset.forName(this.encoding);
            logger.warning(testCS.displayName());
            this.active = true;
        } catch (Exception e) {
            this.active = false;
            logger.warning(encoding + " character set not supported (" + e.getMessage() + "). UTF8Filter de-activated.");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
/*        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/xhtml+xml; charset=UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);*/
        if (active) {
            servletRequest.setCharacterEncoding(encoding);
            servletResponse.setContentType("application/xhtml+xml; charset=UTF-8");
            servletResponse.setCharacterEncoding(encoding);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        this.encoding = null;
    }
}
