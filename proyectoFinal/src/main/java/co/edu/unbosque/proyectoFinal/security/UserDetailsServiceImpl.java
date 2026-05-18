package co.edu.unbosque.proyectoFinal.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectoFinal.entity.Administrador;
import co.edu.unbosque.proyectoFinal.entity.Usuario;
import co.edu.unbosque.proyectoFinal.repository.AdministradorRepository;
import co.edu.unbosque.proyectoFinal.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	private final AdministradorRepository administradorRepository;

	public UserDetailsServiceImpl(UsuarioRepository usuarioRepository,
			AdministradorRepository administradorRepository) {
		this.usuarioRepository = usuarioRepository;
		this.administradorRepository = administradorRepository;
	}

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
