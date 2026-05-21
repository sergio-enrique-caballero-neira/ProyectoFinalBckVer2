package co.edu.unbosque.proyectoFinal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración central de Spring Security para la aplicación.
 *
 * <p>Establece una arquitectura de seguridad <strong>stateless</strong> basada en JWT,
 * donde el servidor no mantiene sesiones HTTP. Cada petición debe incluir el token
 * JWT en el encabezado {@code Authorization: Bearer <token>}.</p>
 *
 * <p><strong>Componentes configurados:</strong></p>
 * <ul>
 *   <li>{@link SecurityFilterChain}: define las reglas de autorización por ruta y rol,
 *       deshabilita CSRF (innecesario en APIs stateless) y establece la política de
 *       sesión como {@code STATELESS}.</li>
 *   <li>{@link JwtAuthenticationFilter}: se registra antes del filtro estándar
 *       {@link UsernamePasswordAuthenticationFilter} para procesar el token JWT
 *       en cada petición.</li>
 *   <li>{@link DaoAuthenticationProvider}: proveedor de autenticación que usa
 *       {@link UserDetailsServiceImpl} para cargar el usuario y
 *       {@link BCryptPasswordEncoder} para verificar la contraseña.</li>
 *   <li>{@link AuthenticationManager}: bean expuesto para ser inyectado en
 *       {@link co.edu.unbosque.proyectoFinal.controller.AuthController}.</li>
 *   <li>{@link PasswordEncoder}: bean BCrypt expuesto para cifrado de contraseñas en
 *       los servicios y en {@link co.edu.unbosque.proyectoFinal.configuration.LoadDatabase}.</li>
 * </ul>
 *
 * <p><strong>Matriz de autorización por ruta:</strong></p>
 * <pre>
 * POST   /auth/**                       → Público (sin autenticación)
 * GET    /swagger-ui/**                 → Público
 * GET    /v3/api-docs/**               → Público
 * POST   /usuario/crear                → Público
 * GET    /usuario/mostrartodo          → USUARIO, ADMIN
 * GET    /usuario/getIdByUsername      → USUARIO, ADMIN
 * GET    /usuario/getHistorialById     → USUARIO, ADMIN
 * PUT    /usuario/**                   → ADMIN
 * DELETE /usuario/**                   → ADMIN
 * /**    /administrador/**             → ADMIN
 * /**    /virustotal/**                → USUARIO, ADMIN
 * </pre>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see JwtAuthenticationFilter
 * @see UserDetailsServiceImpl
 * @see JwtUtil
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Servicio que carga los detalles del usuario desde la base de datos. */
    private final UserDetailsServiceImpl userDetailsService;

    /** Utilidad para operaciones con tokens JWT. */
    private final JwtUtil jwtUtil;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param userDetailsService servicio de carga de usuarios
     * @param jwtUtil            utilidad JWT
     */
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Configura la cadena de filtros de seguridad con las reglas de autorización,
     * el filtro JWT y la política de sesión stateless.
     *
     * @param http objeto de configuración de seguridad HTTP de Spring
     * @return cadena de filtros de seguridad configurada
     * @throws Exception si ocurre un error durante la construcción de la cadena
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/usuario/crear").permitAll()
                // Rutas para USUARIO y ADMIN
                .requestMatchers("/usuario/mostrartodo").hasAnyRole("USUARIO", "ADMIN")
                .requestMatchers("/usuario/getIdByUsername").hasAnyRole("USUARIO", "ADMIN")
                .requestMatchers("/usuario/getHistorialById").hasAnyRole("USUARIO", "ADMIN")
                // Rutas exclusivas de ADMIN
                .requestMatchers("/usuario/**").hasRole("ADMIN")
                .requestMatchers("/administrador/**").hasRole("ADMIN")
                // VirusTotal: USUARIO y ADMIN
                .requestMatchers("/virustotal/**").hasAnyRole("USUARIO", "ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtUtil, userDetailsService),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura el proveedor de autenticación DAO con BCrypt y el servicio de usuarios.
     *
     * @return instancia de {@link DaoAuthenticationProvider} configurada
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Expone el {@link AuthenticationManager} como bean de Spring para ser
     * inyectado en {@link co.edu.unbosque.proyectoFinal.controller.AuthController}.
     *
     * @param config configuración de autenticación de Spring Security
     * @return gestor de autenticación del contexto
     * @throws Exception si ocurre un error al obtener el gestor
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Registra el codificador de contraseñas BCrypt como bean de Spring.
     *
     * <p>BCrypt es una función de hash adaptativa diseñada específicamente para
     * contraseñas, con un factor de coste que aumenta el tiempo de cómputo para
     * dificultar ataques de fuerza bruta.</p>
     *
     * @return nueva instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
