package com.bot.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    public static class Config { }
    @Autowired
    private RouteValidator routeValidator;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                // check Authorizatrion header
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Unauthorization access. Token is missing.");
                }

                String authorizationHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                    authorizationHeader = authorizationHeader.substring(7);
                }

                try {
                    String secret = "SchoolInMind_secret_key_is__bottomhalf@mi9_01";
                    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
                    SecretKey key = Keys.hmacShaKeyFor(keyBytes);

                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(authorizationHeader)
                            .getBody();

                    String email = claims.get("email", String.class);
                    String sid = claims.get("sid", String.class);

                } catch (ExpiredJwtException e) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your session got expired");
                } catch (Exception ex) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unauthorized access. Please try with valid token.");
                }
            }
            return chain.filter(exchange);
        });
    }
}
