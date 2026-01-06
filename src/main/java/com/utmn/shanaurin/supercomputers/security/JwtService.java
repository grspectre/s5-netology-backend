package com.utmn.shanaurin.supercomputers.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final JwtProperties props;
    private final JWSSigner signer;

    public JwtService(JwtProperties props) throws KeyLengthException {
        this.props = props;
        byte[] secretBytes = props.secret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 bytes");
        }
        this.signer = new MACSigner(secretBytes);
    }

    public String generateToken(String subject, List<? extends GrantedAuthority> authorities) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.ttlSeconds());

        // Сохраняем роли без префикса ROLE_
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a)
                .toList();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(props.issuer())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(exp))
                .claim("roles", roles)
                .build();

        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);

        try {
            jwt.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to sign JWT", e);
        }

        return jwt.serialize();
    }
}