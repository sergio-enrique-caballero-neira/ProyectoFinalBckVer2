package co.edu.unbosque.proyectoFinal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Componente utilitario para la gestión de tokens JWT (JSON Web Token).
 *
 * <p>Proporciona los tres pilares de la autenticación basada en tokens:</p>
 * <ol>
 *   <li><strong>Generación</strong>: crea un token firmado con HMAC-SHA256 que incluye
 *       el nombre de usuario ({@code subject}), la fecha de emisión y la fecha de
 *       expiración (24 horas desde la emisión).</li>
 *   <li><strong>Validación</strong>: verifica que el token no haya expirado y que el
 *       {@code subject} del token coincida con el nombre del usuario cargado.</li>
 *   <li><strong>Extracción</strong>: parsea el token para extraer cualquier claim
 *       (sujeto, fechas, datos personalizados).</li>
 * </ol>
 *
 * <p>La clave secreta se inyecta desde la propiedad {@code jwt.secret} del archivo
 * {@code application.properties}. Debe tener al menos 32 caracteres para cumplir
 * con el requisito mínimo de HMAC-SHA256.</p>
 *
 * <p><strong>Formato del token generado:</strong></p>
 * <pre>
 * Header:  { "alg": "HS256" }
 * Payload: { "sub": "nombre_usuario", "iat": timestamp, "exp": timestamp+86400s }
 * </pre>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see JwtAuthenticationFilter
 * @see SecurityConfig
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta usada para firmar y verificar los tokens JWT.
     * Se inyecta desde la propiedad {@code jwt.secret} en {@code application.properties}.
     */
    @Value("${jwt.secret}")
    private String secret;

    /** Duración del token en milisegundos: 24 horas (86 400 000 ms). */
    private static final long EXPIRATION_TIME = 86_400_000L;

    /**
     * Genera un token JWT firmado para el usuario indicado.
     *
     * <p>El token incluye:</p>
     * <ul>
     *   <li>{@code subject}: nombre de usuario ({@link UserDetails#getUsername()}).</li>
     *   <li>{@code issuedAt}: fecha y hora de emisión.</li>
     *   <li>{@code expiration}: fecha de vencimiento (emisión + 24 h).</li>
     * </ul>
     *
     * @param userDetails objeto con la información del usuario autenticado
     * @return token JWT compacto firmado con HMAC-SHA256
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Construye el token JWT con los claims, subject y tiempos de vigencia.
     *
     * @param claims  mapa de claims personalizados adicionales (puede estar vacío)
     * @param subject nombre de usuario que se incluye como {@code sub}
     * @return token JWT serializado y compacto
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Construye la clave criptográfica HMAC-SHA a partir del secreto configurado.
     *
     * @return instancia de {@link SecretKey} lista para firmar y verificar tokens
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Extrae el nombre de usuario ({@code subject}) del token JWT.
     *
     * @param token token JWT del que extraer el subject
     * @return nombre de usuario contenido en el claim {@code sub}
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token token JWT del que extraer la expiración
     * @return fecha y hora de vencimiento del token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico del token usando el resolver proporcionado.
     *
     * @param <T>            tipo del claim a extraer
     * @param token          token JWT a parsear
     * @param claimsResolver función que extrae el claim deseado del objeto {@link Claims}
     * @return valor del claim extraído
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parsea y verifica la firma del token JWT, retornando todos sus claims.
     *
     * @param token token JWT a parsear
     * @return objeto {@link Claims} con todos los claims del token
     * @throws io.jsonwebtoken.JwtException si la firma no es válida o el token está malformado
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Comprueba si el token JWT ha expirado comparando su fecha de vencimiento
     * con la fecha y hora actual del sistema.
     *
     * @param token token JWT a comprobar
     * @return {@code true} si el token ha expirado; {@code false} si aún es válido
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Valida el token JWT verificando que el usuario coincida y el token no haya expirado.
     *
     * @param token       token JWT recibido del cliente
     * @param userDetails objeto con la información del usuario a comparar
     * @return {@code true} si el token es válido para el usuario indicado;
     *         {@code false} en caso contrario
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
