package co.edu.unbosque.proyectoFinal.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectoFinal.repository.AdministradorRepository;
import co.edu.unbosque.proyectoFinal.repository.UsuarioRepository;

/**
 * Implementación de {@link UserDetailsService} que carga usuarios desde la
 * base de datos para el proceso de autenticación de Spring Security.
 *
 * <p>Spring Security invoca {@link #loadUserByUsername(String)} durante el login
 * para obtener los detalles del usuario (nombre, contraseña cifrada, roles) y
 * compararlos con las credenciales recibidas.</p>
 *
 * <p><strong>Orden de búsqueda:</strong></p>
 * <ol>
 *   <li>Busca primero en la tabla {@code usuarios} mediante
 *       {@link UsuarioRepository#findByNombre(String)}.</li>
 *   <li>Si no se encuentra, busca en la tabla {@code administradores} mediante
 *       {@link AdministradorRepository#findByNombre(String)}.</li>
 *   <li>Si tampoco se encuentra en ninguna tabla, lanza
 *       {@link UsernameNotFoundException}.</li>
 * </ol>
 *
 * <p>Tanto {@link co.edu.unbosque.proyectoFinal.entity.Usuario} como
 * {@link co.edu.unbosque.proyectoFinal.entity.Administrador} implementan
 * {@link UserDetails}, por lo que pueden ser retornados directamente.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see JwtAuthenticationFilter
 * @see SecurityConfig
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /** Repositorio de usuarios normales del sistema. */
    private final UsuarioRepository usuarioRepository;

    /** Repositorio de administradores del sistema. */
    private final AdministradorRepository administradorRepository;

    /**
     * Construye el servicio con los repositorios necesarios mediante inyección
     * de dependencias por constructor.
     *
     * @param usuarioRepository       repositorio de usuarios
     * @param administradorRepository repositorio de administradores
     */
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository,
                                  AdministradorRepository administradorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
    }

    /**
     * Carga un usuario por su nombre de usuario buscando primero en la tabla de
     * usuarios y luego en la de administradores.
     *
     * @param username nombre de usuario a buscar (no puede ser {@code null})
     * @return objeto {@link UserDetails} correspondiente al usuario o administrador encontrado
     * @throws UsernameNotFoundException si el nombre de usuario no existe en ninguna tabla
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByNombre(username)
                .map(u -> (UserDetails) u)
                .orElseGet(() -> administradorRepository.findByNombre(username)
                        .map(a -> (UserDetails) a)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Usuario no encontrado: " + username)));
    }
}
