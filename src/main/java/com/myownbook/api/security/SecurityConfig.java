package com.myownbook.api.security;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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
    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resStream = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(keyStorePath);
            keyStore.load(resStream, keyStorePassword.toCharArray());
        }catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("키저장소를 로드할수 없습니다 : {}", keyStorePath, e);
        }
        throw new IllegalArgumentException("키저장소를 로드할수 없습니다");
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
