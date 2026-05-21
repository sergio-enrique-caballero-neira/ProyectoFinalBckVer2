package co.edu.unbosque.proyectoFinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectoFinal.entity.Usuario;

/**
 * Repositorio Spring Data JPA para la entidad {@link Usuario}.
 *
 * <p>Extiende {@link JpaRepository} con tipo de entidad {@code Usuario}
 * y tipo de clave primaria {@code Long}, lo que proporciona automáticamente
 * los métodos CRUD estándar: {@code save}, {@code findById}, {@code findAll},
 * {@code deleteById}, {@code existsById}, {@code count}, entre otros.</p>
 *
 * <p>Declara además un método de consulta derivado:
 * {@link #findByNombre(String)}, utilizado tanto en la autenticación JWT
 * como en la consulta de ID por nombre de usuario desde el servicio.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see co.edu.unbosque.proyectoFinal.service.UsuarioService
 * @see co.edu.unbosque.proyectoFinal.security.UserDetailsServiceImpl
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Busca un usuario por su nombre de usuario.
	 *
	 * <p>Spring Data JPA genera automáticamente la consulta JPQL equivalente:
	 * {@code SELECT u FROM Usuario u WHERE u.nombre = :nombre}.</p>
	 *
	 * @param nombre nombre de usuario a buscar
	 * @return {@link Optional} con el usuario si existe, o vacío si no
	 */
	public Optional<Usuario> findByNombre(String nombre);
}
