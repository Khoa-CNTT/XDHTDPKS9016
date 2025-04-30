package com.tourism.booking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Autowired
    CustomJwtDecoder customJwtDecoder;

    @Value("${api.prefix}")
    private String apiPrefix;

    // Định nghĩa một bean của SecurityFilterChain để cấu hình bảo mật cho ứng dụng
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults()) // Cần có dòng này
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(apiPrefix + "/hotels/**").permitAll()
                            .requestMatchers(apiPrefix + "/auth/**").permitAll()
                            .requestMatchers(apiPrefix + "/forgotPassword/**").permitAll()
                            .requestMatchers(apiPrefix + "/management-user/**").authenticated()
                            .requestMatchers(apiPrefix + "/images/**").permitAll()
                            .requestMatchers(apiPrefix + "/chat/ai").permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .anyRequest().authenticated(); // các request còn lại phải xác thực
                });

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
        );

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of("http://localhost:5173"));
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        cors.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Tạo khóa bí mật với thuật toán HS512
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");

        // Cấu hình và tạo JwtDecoder với khóa bí mật và thuật toán mã hóa
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)  // Đặt khóa bí mật
                .macAlgorithm(MacAlgorithm.HS512)  // Chọn thuật toán HS512
                .build();  // Xây dựng JwtDecoder
    }

    //401: chưa login
    //403: không có quyền truy cập

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Tạo JwtGrantedAuthoritiesConverter và đặt tiền tố quyền hạn
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        // Tạo JwtAuthenticationConverter và thiết lập JwtGrantedAuthoritiesConverter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        jwtAuthenticationConverter.setPrincipalClaimName("accountId");

        // Trả về JwtAuthenticationConverter cấu hình sẵn
        return jwtAuthenticationConverter;
    }




}
