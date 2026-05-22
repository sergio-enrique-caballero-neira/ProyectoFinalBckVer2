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
 * Controlador REST para autenticacion de usuarios y administradores.
 * Maneja el endpoint de login que genera tokens JWT.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class AuthController {

    /** Gestor de autenticacion de Spring Security. */
    private final AuthenticationManager authenticationManager;
    /** Utilidad para generacion y validacion de tokens JWT. */
    private final JwtUtil jwtUtil;

    /**
     * Constructor que inyecta los componentes de autenticacion y generacion de tokens.
     * @param authenticationManager gestor de autenticacion de Spring Security
     * @param jwtUtil utilidad para generacion y validacion de tokens JWT
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Login unificado para Usuario y Administrador.
     * @param nombre nombre de usuario
     * @param contrasena contraseña del usuario
     * @return ResponseEntity con token JWT y rol, o 401 si las credenciales son invalidas
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
     * DTO interno para la respuesta de autenticacion con token JWT y rol del usuario.
     */
    private static class AuthResponse {
        /** Token JWT de autenticacion. */
        private final String token;
        /** Rol del usuario autenticado. */
        private final String role;

        /**
         * Crea una respuesta de autenticacion.
         * @param token token JWT generado
         * @param rol rol del usuario autenticado
         */
        public AuthResponse(String token, String rol) {
            this.token = token;
            this.role = rol;
        }

        /**
         * Obtiene el token JWT.
         * @return token JWT
         */
        public String getToken() { return token; }
        /**
         * Obtiene el rol del usuario.
         * @return rol del usuario (USUARIO o ADMIN)
         */
        public String getRole()  { return role; }
    }
}
