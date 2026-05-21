package co.edu.unbosque.proyectoFinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectoFinal.entity.Usuario;

/**
 * Repositorio JPA para la entidad Usuario.
 * Proporciona operaciones CRUD estandar y busqueda por nombre.
 */
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{
	
	/**
	 * Busca un usuario por su nombre.
	 * @param nombre nombre del usuario
	 * @return Optional con el usuario encontrado o vacio
	 */
	public Optional<Usuario> findByNombre(String nombre);

}
