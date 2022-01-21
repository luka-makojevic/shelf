package com.htec.account.filter;

import com.htec.account.security.SecurityConstants;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class JwtStorageFilter implements Filter {

    public static ThreadLocal<String> jwtThreadLocal = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        storeJwt(req, res);

        chain.doFilter(req, res);
    }

    public void storeJwt(ServletRequest req,
                         ServletResponse res) {

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String jwtToken = httpRequest.getHeader(SecurityConstants.AUTHORIZATION_HEADER_STRING);

        jwtThreadLocal.set(jwtToken);
    }
}
