package co.edu.unbosque.proyectoFinal.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectoFinal.entity.Administrador;
import co.edu.unbosque.proyectoFinal.entity.Usuario;
import co.edu.unbosque.proyectoFinal.repository.AdministradorRepository;
import co.edu.unbosque.proyectoFinal.repository.UsuarioRepository;

/**
 * Implementacion de UserDetailsService que busca usuarios y administradores en la base de datos.
 * Busca primero en la tabla de usuarios y luego en la de administradores por nombre.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	private final AdministradorRepository administradorRepository;

	/**
	 * Constructor que inyecta los repositorios de usuarios y administradores.
	 * @param usuarioRepository repositorio de usuarios
	 * @param administradorRepository repositorio de administradores
	 */
	public UserDetailsServiceImpl(UsuarioRepository usuarioRepository,
			AdministradorRepository administradorRepository) {
		this.usuarioRepository = usuarioRepository;
		this.administradorRepository = administradorRepository;
	}

	/**
	 * Carga un usuario o administrador por su nombre de usuario.
	 * Busca primero en usuarios regulares y luego en administradores.
	 * @param username nombre de usuario
	 * @return detalles del usuario encontrado
	 * @throws UsernameNotFoundException si no se encuentra el usuario
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		for (Usuario u : usuarioRepository.findAll()) {
			if (u.getNombre().equals(username)) {
				return usuarioRepository.findByNombre(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
			}
		}

		for (Administrador a : administradorRepository.findAll()) {
			if (a.getNombre().equals(username)) {
				return administradorRepository.findByNombre(username).orElseThrow(() -> new UsernameNotFoundException("Admin not found with username: " + username));
			}
		}

		throw new UsernameNotFoundException("Usuario no encontrado: " + username);
	}
}
