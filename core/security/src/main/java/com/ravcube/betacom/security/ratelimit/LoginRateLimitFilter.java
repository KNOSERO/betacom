package com.ravcube.betacom.security.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

public class LoginRateLimitFilter extends OncePerRequestFilter {

    private final LoginRateLimitProperties properties;
    private final ConcurrentMap<String, Bucket> bucketsByIp = new ConcurrentHashMap<>();

    public LoginRateLimitFilter(LoginRateLimitProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.POST.matches(request.getMethod()) && "/login".equals(request.getServletPath())) {
            String ipAddress = Objects.requireNonNullElse(request.getRemoteAddr(), "unknown");
            Bucket bucket = bucketsByIp.computeIfAbsent(ipAddress, ignored -> newBucket());
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

            if (!probe.isConsumed()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setHeader(HttpHeaders.RETRY_AFTER, Long.toString(retryAfterSeconds(probe)));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Bucket newBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(properties.capacity())
                        .refillIntervally(properties.capacity(), properties.window()))
                .build();
    }

    private long retryAfterSeconds(ConsumptionProbe probe) {
        long nanos = probe.getNanosToWaitForRefill();
        long seconds = nanos / 1_000_000_000L;
        return Math.max(1, seconds + (nanos % 1_000_000_000L == 0 ? 0 : 1));
    }
}
