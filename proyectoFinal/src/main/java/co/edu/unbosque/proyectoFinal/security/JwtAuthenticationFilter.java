package co.edu.unbosque.proyectoFinal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que intercepta cada petición HTTP exactamente una vez.
 *
 * <p>Extiende {@link OncePerRequestFilter} para garantizar que la lógica de
 * validación del token se ejecuta una sola vez por petición, independientemente
 * de la cadena de filtros configurada en Spring Security.</p>
 *
 * <p><strong>Flujo de procesamiento por petición:</strong></p>
 * <ol>
 *   <li>Lee el encabezado {@code Authorization} de la solicitud HTTP.</li>
 *   <li>Si el encabezado existe y comienza con {@code "Bearer "}, extrae el token.</li>
 *   <li>Extrae el nombre de usuario del token usando {@link JwtUtil#extractUsername(String)}.</li>
 *   <li>Si el usuario existe y no hay autenticación previa en el contexto de seguridad,
 *       carga sus detalles desde {@link UserDetailsServiceImpl}.</li>
 *   <li>Valida el token con {@link JwtUtil#validateToken(String, UserDetails)}.</li>
 *   <li>Si es válido, establece un {@link UsernamePasswordAuthenticationToken}
 *       en el {@link SecurityContextHolder}, marcando la petición como autenticada.</li>
 *   <li>Continúa la cadena de filtros ({@code filterChain.doFilter}).</li>
 * </ol>
 *
 * <p>Si el encabezado no contiene un token Bearer, o si el token es inválido o ha
 * expirado, la petición continúa sin autenticar. Spring Security rechazará entonces
 * las peticiones que requieran autenticación devolviendo {@code 401 Unauthorized}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see JwtUtil
 * @see UserDetailsServiceImpl
 * @see SecurityConfig
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Utilidad para parsear, validar y generar tokens JWT. */
    private final JwtUtil jwtUtil;

    /** Servicio que carga los detalles del usuario desde la base de datos. */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Construye el filtro con sus dependencias.
     *
     * @param jwtUtil            utilidad JWT para extracción y validación de tokens
     * @param userDetailsService servicio de carga de usuarios para Spring Security
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Ejecuta la lógica de validación JWT en cada petición HTTP.
     *
     * <p>Si el token es válido, establece el contexto de autenticación de Spring Security
     * para que los filtros de autorización posteriores puedan verificar los roles del usuario.
     * Si el token no está presente o es inválido, la petición continúa sin autenticar.</p>
     *
     * @param request     solicitud HTTP recibida
     * @param response    respuesta HTTP a enviar
     * @param filterChain cadena de filtros de Spring Security
     * @throws ServletException si ocurre un error en el procesamiento del servlet
     * @throws IOException      si ocurre un error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token malformado o firma inválida — se deja sin autenticar
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
