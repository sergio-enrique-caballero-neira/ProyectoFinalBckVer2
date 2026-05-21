package co.edu.unbosque.proyectoFinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectoFinal.entity.Administrador;

/**
 * Repositorio Spring Data JPA para la entidad {@link Administrador}.
 *
 * <p>Extiende {@link JpaRepository} con tipo de entidad {@code Administrador}
 * y tipo de clave primaria {@code Long}, lo que proporciona automáticamente
 * los métodos CRUD estándar: {@code save}, {@code findById}, {@code findAll},
 * {@code deleteById}, {@code existsById}, {@code count}, entre otros.</p>
 *
 * <p>Declara además un método de consulta derivado:
 * {@link #findByNombre(String)}, utilizado por
 * {@link co.edu.unbosque.proyectoFinal.security.UserDetailsServiceImpl}
 * para cargar un administrador por su nombre de usuario durante el proceso
 * de autenticación JWT.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see co.edu.unbosque.proyectoFinal.service.AdministradorService
 * @see co.edu.unbosque.proyectoFinal.security.UserDetailsServiceImpl
 */
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

	/**
	 * Busca un administrador por su nombre de usuario.
	 *
	 * <p>Spring Data JPA genera automáticamente la consulta JPQL equivalente:
	 * {@code SELECT a FROM Administrador a WHERE a.nombre = :nombre}.</p>
	 *
	 * @param nombre nombre de usuario del administrador a buscar
	 * @return {@link Optional} con el administrador si existe, o vacío si no
	 */
	public Optional<Administrador> findByNombre(String nombre);
}
