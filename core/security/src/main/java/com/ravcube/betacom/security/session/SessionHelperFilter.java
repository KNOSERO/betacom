package com.ravcube.betacom.security.session;

import com.ravcube.betacom.database.audit.AuditRevisionContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionHelperFilter extends OncePerRequestFilter {

    private final RequestSessionHelper sessionHelper;

    public SessionHelperFilter(RequestSessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication && authentication.isAuthenticated()) {
            UUID userId = UUID.fromString(jwtAuthentication.getToken().getSubject());
            sessionHelper.setUserId(userId);
            AuditRevisionContext.setCreatedBy(userId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            AuditRevisionContext.clear();
        }
    }
}
