package com.myownbook.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${app.security.jwt.keystore-location}")
    private String keyStorePath;

    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;

    @Value("${app.security.jwt.key-alias}")
    private String keyAlias;

    @Value("${app.security.jwt.private-key-passphrase}")
    private String privateKeyPassphrase;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req.requestMatchers(toH2Console()).permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-resources/**","/v3/api-docs/**", HttpMethod.GET.name()).permitAll()
                .requestMatchers(HttpMethod.POST, "/users/signup").permitAll()
                .requestMatchers(HttpMethod.POST,"/users/login").permitAll()
                //.requestMatchers(HttpMethod.POST, "/books/add").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/books").permitAll()
                .requestMatchers(HttpMethod.GET, "/books/isbn/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/books/id/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resStream = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(keyStorePath);
            keyStore.load(resStream, keyStorePassword.toCharArray());
            return keyStore;
        }catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("키 저장소를 로드할수 없습니다 : {}", keyStorePath, e);
        }
        throw new IllegalArgumentException("키 저장소를 로드할수 없습니다");
    }

    @Bean
    public RSAPrivateKey jwtSignKey(KeyStore keyStore) {
        try{
            Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
            if(key instanceof  RSAPrivateKey)  return (RSAPrivateKey) key;
        }catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("키 저장소에서 개인 키를 로드할 수 없습니다 : {}", keyStorePath, e);
        }
        throw new IllegalArgumentException("개인 키를 로드할 수 없습니다");
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();
            if(publicKey instanceof RSAPublicKey) return (RSAPublicKey) publicKey;

        } catch (KeyStoreException e) {
            log.error("키 저장소에서 개인 데이터를 로드할 수 없습니다 : {}", keyStorePath, e);
        }
        throw new IllegalArgumentException("RSA 공개 키를 로드할 수 없습니다.");
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }
}

