package co.edu.unbosque.proyectoFinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectoFinal.entity.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador,Long>{
	
	public Optional<Administrador> findByNombre(String nombre);
	
}
