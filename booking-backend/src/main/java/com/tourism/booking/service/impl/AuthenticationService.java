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
import java.util.List;
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

        if(acc == null || !passwordEncoder.matches(authenticationRequest.getPassword(), acc.getPassword())) {
            throw new ApiException(ErrorCode.UNAUTHENTICATION);
        }
        return AuthenticationResponse.builder()
                .token(generateToken(acc))
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest)  {
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
                .subject(acc.getUsername()) // Đặt chủ thể (subject) của JWT là tên đăng nhập của người dùng
                .issuer("sqc.com") // Đặt người phát hành JWT là "sqc.com"
                .issueTime(new Date()) // Đặt thời gian phát hành JWT là thời điểm hiện tại
                .expirationTime(new Date( // Đặt thời gian hết hạn cho JWT là 1 giờ kể từ lúc phát hành
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                // Thêm một custom claim (thông tin tùy chỉnh) vào JWT, chứa thông tin về đối tượng Student
                .claim("scope", getRoles(acc))
                .jwtID(UUID.randomUUID().toString())
                .build(); // Xây dựng đối tượng JWTClaimsSet

        // Tạo payload từ claims đã tạo, chuyển đối tượng claims thành định dạng JSON
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Tạo JWSObject từ header và payload, kết hợp chúng lại thành đối tượng JWS
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // Ký JWT bằng thuật toán HMAC SHA-512, sử dụng khóa bí mật (SIGNER_KEY)
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            // Chuyển đối tượng JWS thành chuỗi JWT hoàn chỉnh (header.payload.signature) và trả về
            return jwsObject.serialize();
        } catch (JOSEException e) {
            // Nếu có lỗi xảy ra trong quá trình ký JWT, ném ra ngoại lệ RuntimeException
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
        // Tạo một đối tượng JWSVerifier với thuật toán HMAC SHA-512 để xác minh chữ ký của JWT
        try {
            // Tạo một đối tượng JWSVerifier với thuật toán HMAC SHA-512 để xác minh chữ ký của JWT
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            // Phân tích cú pháp (parse) chuỗi JWT thành đối tượng SignedJWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Lấy thời gian hết hạn của JWT từ phần claims (payload)
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);

            // Xác minh chữ ký của JWT, kiểm tra xem chữ ký có hợp lệ không
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
