package com.tourism.booking.service;

import com.nimbusds.jose.JOSEException;
import com.tourism.booking.dto.authentication.AuthenticationRequest;
import com.tourism.booking.dto.authentication.AuthenticationResponse;
import com.tourism.booking.dto.authentication.IntrospectRequest;
import com.tourism.booking.dto.authentication.IntrospectResponse;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException;
}
