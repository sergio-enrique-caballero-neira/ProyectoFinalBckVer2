package co.edu.unbosque.proyectoFinal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import co.edu.unbosque.proyectoFinal.entity.Persona;

/**
 * Utilidad para la generacion, validacion y extraccion de claims de tokens JWT.
 * Usa HMAC-SHA256 para la firma de tokens con una validez de 24 horas.
 */
@Component
public class JwtUtil {

    /** Duracion del token JWT en milisegundos (24 horas). */
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    /** Clave secreta para firmar los tokens JWT. */
    @Value("${jwt.secret:defaultSecretKeyWhichShouldBeAtLeast32CharactersLong}")
    private String secret;

    /**
     * Obtiene la clave de firma HMAC a partir del secreto configurado.
     * @return clave de firma
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el nombre de usuario (subject) del token.
     * @param token token JWT
     * @return nombre de usuario
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiracion del token.
     * @param token token JWT
     * @return fecha de expiracion
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae el rol del usuario del token.
     * @param token token JWT
     * @return rol del usuario
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extrae un claim especifico del token usando una funcion de resolucion.
     * @param token token JWT
     * @param claimsResolver funcion para extraer el claim
     * @return valor del claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token validando la firma.
     * @param token token JWT
     * @return claims del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica si el token ha expirado.
     * @param token token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Genera un token JWT para un usuario, incluyendo su rol y autoridades.
     * @param userDetails detalles del usuario
     * @return token JWT generado
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities());

        if (userDetails instanceof Persona) {
            Persona persona = (Persona) userDetails;
            claims.put("role", persona.getRole().name());
        }

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Crea un token JWT con los claims y sujeto especificados.
     * @param claims claims a incluir en el token
     * @param subject sujeto del token (nombre de usuario)
     * @return token JWT compacto
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida un token JWT verificando que el nombre de usuario coincida y no haya expirado.
     * @param token token JWT
     * @param userDetails detalles del usuario
     * @return true si el token es valido, false en caso contrario
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
