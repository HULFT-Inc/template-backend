package com.example.template;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.management.endpoint.annotation.Endpoint;

@Controller("/health")
public class HealthController {
    
    @Get
    public String health() {
        return "OK";
    }
}
