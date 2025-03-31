package com.tourism.booking.config;


import com.nimbusds.jose.JOSEException;
import com.tourism.booking.dto.authentication.IntrospectRequest;
import com.tourism.booking.service.IAuthenticationService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class IntrospectFilter implements Filter {

    private final IAuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                boolean valid = authenticationService.introspect(new IntrospectRequest(token)).isValid();
                if (!valid) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }
            } catch (ParseException | JOSEException e) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token parse error");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}