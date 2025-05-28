package com.tourism.booking.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tourism.booking.dto.authentication.*;
import com.tourism.booking.dto.logout.LogoutRequest;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.InvalidToken;
import com.tourism.booking.repository.IAccountRepository;
import com.tourism.booking.repository.IInvalidTokenRepository;
import com.tourism.booking.service.IAuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {
    IAccountRepository accountRepository;
    IInvalidTokenRepository invalidTokenRepository;

    @Value("${jwt.signerKey}")
    @NonFinal
    private String SIGNER_KEY;


    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Account acc = accountRepository.findByUsername(authenticationRequest.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        UserAuthResponse user = accountRepository.getUserAuthResponse(authenticationRequest.getUsername());
        if (acc == null || !passwordEncoder.matches(authenticationRequest.getPassword(), acc.getPassword())) {
            throw new ApiException(ErrorCode.UNAUTHENTICATION);
        }
        return AuthenticationResponse.builder()
                .token(generateToken(acc))
                .user(
                        UserAuthResponse.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .role(user.getRole())
                                .build()
                )
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        try {
            verifyToken(introspectRequest.getToken());
            return IntrospectResponse.builder()
                    .isValid(true)
                    .build();
        } catch (JwtException e) {
            return IntrospectResponse.builder()
                    .isValid(false)
                    .build();
        }
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException {
        SignedJWT signedJWT = verifyToken(logoutRequest.getToken());
        invalidTokenRepository.save(InvalidToken.builder()
                .id(signedJWT.getJWTClaimsSet().getJWTID())
                .expiryTime(LocalDateTime.now())
                .build());
    }


    private String generateToken(Account acc) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(acc.getUsername())
                .issuer("sqc.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))

                .claim("scope", getRoles(acc))
                .claim("accountId", acc.getAccount_id())
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRoles(Account acc) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        acc.getRoles().forEach(role ->
                stringJoiner.add(role.getRole_name()));
        return stringJoiner.toString();
    }

    private SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);

            if (!verified && expiryTime.after(new Date())) {
                throw new JwtException("Invalid token");
            }

            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

            if (invalidTokenRepository.findById(jwtId).isPresent()) {
                throw new JwtException("Invalid token");
            }

            return signedJWT;
        } catch (JOSEException | ParseException e) {
            e.printStackTrace();
            throw new JwtException("Invalid token");
        }
    }

}
