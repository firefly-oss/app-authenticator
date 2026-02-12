package com.firefly.app.orchestrator.web.controllers;

import org.fireflyframework.application.config.DomainPassthrough;
import org.fireflyframework.application.config.DomainPassthroughs;
import org.springframework.stereotype.Component;

@Component
@DomainPassthroughs({
        @DomainPassthrough(path = "/api/v1/auth/login", target = "${endpoints.domain.security-center}"),
        @DomainPassthrough(path = "/api/v1/auth/logout", target = "${endpoints.domain.security-center}"),
        @DomainPassthrough(path = "/api/v1/users", target = "${endpoints.domain.security-center}")
})
public class AuthenticatorController {

}
