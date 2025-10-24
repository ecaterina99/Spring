package com.server.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * JWT (JSON Web Token)
 * Generates an RSA key pair (private + public) used to sign and verify JWTs.
 * NimbusJwtEncoder expects a JWK set (a collection of keys). ImmutableJWKSet wraps it.
 * Creates a JwtDecoder using only the public key.
 * The decoder verifies JWT signatures (it does NOT need the private key).
 */

@Configuration
public class JwtKeyConfig {

    @Bean
    public KeyPair keyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }

    @Bean
    public JwtEncoder jwtEncoder(KeyPair kp) {
        RSAKey jwk = new RSAKey.Builder((RSAPublicKey) kp.getPublic())
                .privateKey((RSAPrivateKey) kp.getPrivate())
                .build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    @Bean
    public JwtDecoder jwtDecoder(KeyPair kp) {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) kp.getPublic()).build();
    }
}