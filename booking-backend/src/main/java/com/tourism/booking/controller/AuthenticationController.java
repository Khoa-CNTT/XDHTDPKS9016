package com.tourism.booking.controller;

import com.nimbusds.jose.JOSEException;
import com.tourism.booking.dto.ApiResponse;
import com.tourism.booking.dto.account.AccountRequest;
import com.tourism.booking.dto.account.AccountResponse;
import com.tourism.booking.dto.authentication.AuthenticationRequest;
import com.tourism.booking.dto.authentication.AuthenticationResponse;
import com.tourism.booking.dto.authentication.IntrospectRequest;
import com.tourism.booking.dto.authentication.IntrospectResponse;
import com.tourism.booking.dto.logout.LogoutRequest;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.mapper.IAccountMapper;
import com.tourism.booking.model.Account;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IAuthenticationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@PreAuthorize("permitAll()")
@RestController
@RequestMapping("${api.prefix}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class AuthenticationController {

    IAuthenticationService authenticationService;
    IAccountMapper accountMapper;
    IAccountService accountService;

    @PostMapping("/login")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.login(authenticationRequest);
    }

    @PostMapping("/introspect")
    public IntrospectResponse introspect(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        return authenticationService.introspect(introspectRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody AccountRequest accountRequest) {
        try {
            Account savedAccount = accountService.register(accountRequest);
            AccountResponse response = accountMapper.AccountToAccountResponse(savedAccount);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .message("Register Success")
                            .data(response)
                            .build());
        } catch (RuntimeException ex) {
            ErrorCode errorCode;
            if (ex.getMessage().contains("Username is exists")) {
                errorCode = ErrorCode.USERNAME_EXISTS;
            } else if (ex.getMessage().contains("Email is exists")) {
                errorCode = ErrorCode.EMAIL_EXISTS;
            } else {
                errorCode = ErrorCode.UNAUTHENTICATION;
            }
            return ResponseEntity.status(errorCode.getStatus())
                    .body(ApiResponse.builder()
                            .message(errorCode.getMessage())
                            .build());
        }
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest logoutRequest) throws ParseException {
        authenticationService.logout(logoutRequest);
    }
}
