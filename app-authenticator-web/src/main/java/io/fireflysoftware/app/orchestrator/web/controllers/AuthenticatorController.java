/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.app.orchestrator.web.controllers;

import org.fireflyframework.common.application.config.DomainPassthrough;
import org.fireflyframework.common.application.config.DomainPassthroughs;
import org.springframework.stereotype.Component;

@Component
@DomainPassthroughs({
        @DomainPassthrough(path = "/api/v1/auth/login", target = "${endpoints.domain.security-center}"),
        @DomainPassthrough(path = "/api/v1/auth/logout", target = "${endpoints.domain.security-center}"),
        @DomainPassthrough(path = "/api/v1/users", target = "${endpoints.domain.security-center}")
})
public class AuthenticatorController {

}
