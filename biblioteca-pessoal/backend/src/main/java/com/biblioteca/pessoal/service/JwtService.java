package com.biblioteca.pessoal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "Bl9VANeYpQTLofpNp1BGIz1ROl6yq/WVhoyXQKBrA/rcioxVXwQeacypvQIjvSB57vBhQVXipieBFTVVLacZcw==";

    /**
     * Extrai o nome de usuário (email) do token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai um atributo específico do token JWT.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
 * Gera um token JWT com informações adicionais no payload.
 */
public String generateToken(String subject, Long userId) {
    // Adiciona claims customizados no token
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", userId); // Adiciona o ID do usuário como um claim personalizado

    return Jwts.builder()
            .setClaims(claims) // Define os claims personalizados
            .setSubject(subject) // Define o "sub" como o identificador principal (email, por exemplo)
            .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expira em 10 horas
            .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina com a chave secreta
            .compact();
}


    /**
     * Verifica se o token JWT é válido.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Verifica se o token JWT expirou.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração do token JWT.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai todas as claims do token JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Configura a chave secreta
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retorna a chave de assinatura usada para criar e validar o token.
     */
    private Key getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
