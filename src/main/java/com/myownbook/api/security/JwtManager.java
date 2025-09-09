package com.myownbook.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import static java.util.stream.Collectors.toList;

@Component
public class JwtManager {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtManager(@Lazy RSAPrivateKey privateKey, @Lazy RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String create(UserDetails principal) {
        final long now = System.currentTimeMillis();
        return JWT.create()
                .withIssuer("Own Book API")
                .withSubject(principal.getUsername())
                .withClaim(
                    "roles",
                        principal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority).collect(toList())
                ).withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + 900_000)) //15분
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }
}
