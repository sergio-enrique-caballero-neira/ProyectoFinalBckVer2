package co.edu.unbosque.proyectoFinal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro de autenticacion JWT que intercepta cada peticion HTTP.
 * Extrae el token del encabezado Authorization, valida el token y establece
 * la autenticacion en el contexto de seguridad de Spring si es valido.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Utilidad para validacion de tokens JWT. */
    private final JwtUtil jwtUtil;
    /** Servicio para cargar detalles de usuarios desde la base de datos. */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor que inyecta las dependencias para validacion de tokens y carga de usuarios.
     * @param jwtUtil utilidad para validacion de tokens JWT
     * @param userDetailsService servicio para cargar detalles de usuarios
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Procesa cada peticion HTTP extrayendo y validando el token JWT.
     * Si el token es valido, establece la autenticacion en el contexto de seguridad.
     * @param request peticion HTTP
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros
     * @throws ServletException si ocurre un error de servlet
     * @throws IOException si ocurre un error de E/S
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("Error extrayendo el nombre de usuario del token", e);
            }	
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
