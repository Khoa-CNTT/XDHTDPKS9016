package com.tourism.booking.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tourism.booking.dto.authentication.AuthenticationRequest;
import com.tourism.booking.dto.authentication.AuthenticationResponse;
import com.tourism.booking.dto.authentication.IntrospectRequest;
import com.tourism.booking.dto.authentication.IntrospectResponse;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.model.Account;
import com.tourism.booking.repository.IAccountRepository;
import com.tourism.booking.service.IAuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {
    IAccountRepository accountRepository;

    String SIGNER_KEY = "bhVrIdtLrU5B8OGws9VweF71syQsCXKku0ul2Nf6/KDEAuvbCFbM+ZSinSJLBrRY";

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Account acc = accountRepository.findByUsername(authenticationRequest.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if(acc == null || !passwordEncoder.matches(authenticationRequest.getPassword(), acc.getPassword())) {
            throw new ApiException(ErrorCode.UNAUTHENTICATION);
        }
        return AuthenticationResponse.builder()
                .token(generateToken(acc.getUsername()))
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        return IntrospectResponse.builder()
                .isValid(verifyJWT(introspectRequest.getToken()))
                .build();
    }

    private String generateToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("booking.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope",getScope(username))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            throw new RuntimeException(e);
        }
    }

    private String getScope(String username) {
        List<String> roles = accountRepository.findRolesByUsername(username);
        return String.join(" ", roles);
    }

    public boolean verifyJWT(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiry = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return verified && expiry.after(new Date());
    }


}
