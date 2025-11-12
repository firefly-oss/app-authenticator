package com.firefly.app.orchestrator.web.controllers;

import com.firefly.common.application.config.DomainPassthrough;
import com.firefly.common.application.config.DomainPassthroughs;
import org.springframework.stereotype.Component;

@Component
@DomainPassthroughs({
        @DomainPassthrough(path = "/api/v1/auth/login", target = "http://localhost:8085"),
        @DomainPassthrough(path = "/api/v1/auth/logout", target = "http://localhost:8085"),
        @DomainPassthrough(path = "/api/v1/users", target = "http://localhost:8085")
})
public class AuthenticatorController {

}
