package co.edu.unbosque.proyectoFinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectoFinal.entity.Administrador;

/**
 * Repositorio JPA para la entidad Administrador.
 * Proporciona operaciones CRUD estandar y busqueda por nombre.
 */
public interface AdministradorRepository extends JpaRepository<Administrador,Long>{
	
	/**
	 * Busca un administrador por su nombre.
	 * @param nombre nombre del administrador
	 * @return Optional con el administrador encontrado o vacio
	 */
	public Optional<Administrador> findByNombre(String nombre);
	
}
