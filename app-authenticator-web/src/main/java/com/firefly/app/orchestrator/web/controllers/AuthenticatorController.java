package com.firefly.app.orchestrator.web.controllers;

import com.firefly.common.application.config.DomainPassthrough;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatorController {

    @DomainPassthrough(path = "/api/v1/auth/login", target = "http://localhost:8085")
    public void login(){}

    @DomainPassthrough(path = "/api/v1/auth/logout", target = "http://localhost:8085")
    public void logout(){}
}
