package co.edu.unbosque.proyectoFinal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectoFinal.entity.Persona;
import co.edu.unbosque.proyectoFinal.security.JwtUtil;

/**
 * Controlador REST que gestiona la autenticación de usuarios y administradores.
 *
 * <p>Expone el endpoint de inicio de sesión unificado bajo la ruta base {@code /auth}.
 * No requiere autenticación previa; es el punto de entrada para obtener el
 * token JWT necesario para acceder al resto de la API.</p>
 *
 * <p><strong>Flujo de autenticación:</strong></p>
 * <ol>
 *   <li>El cliente envía {@code nombre} y {@code contrasena} como parámetros de la petición.</li>
 *   <li>Spring Security delega la verificación al {@link AuthenticationManager}.</li>
 *   <li>Si las credenciales son válidas, se genera un token JWT firmado con el rol del usuario.</li>
 *   <li>El token y el rol se devuelven en el cuerpo de la respuesta.</li>
 * </ol>
 *
 * <p><strong>Importante:</strong> las contraseñas deben estar cifradas con BCrypt
 * en la base de datos. {@link co.edu.unbosque.proyectoFinal.configuration.LoadDatabase}
 * se encarga de esto al arrancar la aplicación para los usuarios de prueba.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see JwtUtil
 * @see co.edu.unbosque.proyectoFinal.security.SecurityConfig
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * Constructor con inyección de dependencias por constructor.
     *
     * @param authenticationManager gestor de autenticación de Spring Security
     * @param jwtUtil               utilidad para generar y validar tokens JWT
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Endpoint de inicio de sesión unificado para usuarios y administradores.
     *
     * <p>Acepta las credenciales como parámetros de la petición HTTP ({@code @RequestParam}).
     * Si la autenticación es exitosa, retorna un objeto JSON con el token JWT y el
     * rol del usuario ({@code "USUARIO"} o {@code "ADMIN"}).
     * Si las credenciales son inválidas, retorna un error {@code 401 Unauthorized}.</p>
     *
     * <p><strong>Ejemplo de respuesta exitosa:</strong></p>
     * <pre>
     * {
     *   "token": "eyJhbGciOiJIUzI1NiJ9...",
     *   "role": "ADMIN"
     * }
     * </pre>
     *
     * @param nombre    nombre de usuario (único en el sistema)
     * @param contrasena contraseña en texto plano (se valida contra el hash BCrypt)
     * @return {@code 200 OK} con el token JWT y el rol, o {@code 401 Unauthorized}
     *         si las credenciales son incorrectas
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String nombre,
            @RequestParam String contrasena) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(nombre, contrasena));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            String role = null;
            if (userDetails instanceof Persona) {
                role = ((Persona) userDetails).getRole().name();
            }

            return ResponseEntity.ok(new AuthResponse(jwt, role));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Nombre o contraseña inválidos");
        }
    }

    /**
     * Clase interna inmutable que representa el cuerpo de la respuesta de autenticación.
     *
     * <p>Contiene el token JWT generado y el rol del usuario autenticado,
     * que el cliente deberá incluir en el encabezado {@code Authorization}
     * de las siguientes peticiones.</p>
     */
    private static class AuthResponse {
        /** Token JWT firmado con las claims del usuario. */
        private final String token;
        /** Rol del usuario autenticado ({@code "USUARIO"} o {@code "ADMIN"}). */
        private final String role;

        /**
         * Construye una respuesta de autenticación con el token y el rol indicados.
         *
         * @param token token JWT generado
         * @param role  rol del usuario autenticado
         */
        public AuthResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }

        /**
         * Retorna el token JWT.
         * @return token JWT como cadena de texto
         */
        public String getToken() { return token; }

        /**
         * Retorna el rol del usuario autenticado.
         * @return nombre del rol ({@code "USUARIO"} o {@code "ADMIN"})
         */
        public String getRole()  { return role; }
    }
}
