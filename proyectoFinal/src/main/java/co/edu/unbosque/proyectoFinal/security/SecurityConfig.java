package co.edu.unbosque.proyectoFinal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuracion de seguridad de Spring Security.
 * Define las reglas de autorizacion, autenticacion JWT, politica de sesiones sin estado
 * y los filtros de seguridad para la aplicacion.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/** Filtro de autenticacion JWT. */
	private final JwtAuthenticationFilter jwtAuthFilter;
	/** Servicio de carga de detalles de usuario. */
	private final UserDetailsService userDetailsService;

	/**
	 * Constructor que inyecta el filtro JWT y el servicio de detalles de usuario.
	 * @param jwtAuthFilter filtro de autenticacion JWT
	 * @param userDetailsService servicio de carga de detalles del usuario
	 */
	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Configura la cadena de filtros de seguridad con las reglas de autorizacion.
	 * @param http objeto HttpSecurity para configurar la seguridad web
	 * @return SecurityFilterChain configurado
	 * @throws Exception si ocurre un error en la configuracion
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.requestMatchers("/usuario/mostrartodo").hasAnyRole("USUARIO", "ADMIN")
						.requestMatchers("/usuario/crear").permitAll()
						.requestMatchers("/usuario/getIdByUsername").hasAnyRole("USUARIO", "ADMIN")
						.requestMatchers("/usuario/getHistorialById").hasAnyRole("USUARIO", "ADMIN")
						.requestMatchers("/usuario/**").hasRole("ADMIN")
						.requestMatchers("/administrador/**").hasRole("ADMIN")
						.requestMatchers("/virustotal/**").hasAnyRole("USUARIO", "ADMIN").anyRequest()
						.authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	/**
	 * Proveedor de autenticacion que utiliza el servicio de detalles de usuario
	 * y el codificador de contraseñas BCrypt.
	 * @return AuthenticationProvider configurado
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Expone el AuthenticationManager para ser usado en los controladores.
	 * @param config configuracion de autenticacion
	 * @return AuthenticationManager
	 * @throws Exception si ocurre un error al obtener el gestor
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Codificador de contraseñas BCrypt.
	 * @return PasswordEncoder BCrypt
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
