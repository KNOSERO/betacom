package com.ravcube.betacom.security.session;

import com.ravcube.betacom.domain.SessionHelper;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestSessionHelper implements SessionHelper {

    private UUID userId;

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public UUID getUserId() {
        if (userId == null) {
            throw new IllegalStateException("An authenticated user is required");
        }
        return userId;
    }
}
