package com.tourism.booking.service;

import com.nimbusds.jose.JOSEException;
import com.tourism.booking.dto.authentication.AuthenticationRequest;
import com.tourism.booking.dto.authentication.AuthenticationResponse;
import com.tourism.booking.dto.authentication.IntrospectRequest;
import com.tourism.booking.dto.authentication.IntrospectResponse;
import com.tourism.booking.dto.logout.LogoutRequest;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException;

    void logout(LogoutRequest logoutRequest) throws ParseException;

}
