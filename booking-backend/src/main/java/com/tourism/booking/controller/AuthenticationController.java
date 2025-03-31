package com.tourism.booking.controller;

import com.nimbusds.jose.JOSEException;
import com.tourism.booking.dto.authentication.AuthenticationRequest;
import com.tourism.booking.dto.authentication.AuthenticationResponse;
import com.tourism.booking.dto.authentication.IntrospectRequest;
import com.tourism.booking.dto.authentication.IntrospectResponse;
import com.tourism.booking.service.IAuthenticationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthenticationController {

    IAuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }

    @PostMapping("/introspect")
    public IntrospectResponse introspect(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        return authenticationService.introspect(introspectRequest);
    }
}
