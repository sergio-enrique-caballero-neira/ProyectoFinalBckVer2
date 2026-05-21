package co.edu.unbosque.proyectoFinal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectoFinal.dto.AdministradorDTO;
import co.edu.unbosque.proyectoFinal.entity.Administrador;
import co.edu.unbosque.proyectoFinal.exception.*;
import co.edu.unbosque.proyectoFinal.repository.AdministradorRepository;

/**
 * Servicio para la gestion de administradores (ADMIN).
 * Implementa operaciones CRUD con validacion de datos y encriptacion de contraseñas.
 */
@Service
public class AdministradorService implements CRUDoperation<AdministradorDTO> {
	
	@Autowired
	private AdministradorRepository administradorRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	/**
	 * Constructor vacio de AdministradorService.
	 */
	public AdministradorService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Crea un nuevo administrador validando los datos y encriptando la contraseña.
	 * @param data datos del administrador a crear
	 * @return 0 si la operacion fue exitosa
	 * @throws BadRequestException si los datos son invalidos o el administrador ya existe
	 */
	@Override
	public int create(AdministradorDTO data) {
		checkData(data);
		
		existByNombre(data.getNombre());
		existByEmail(data.getEmail());
		existByTelefono(data.getTelefono());
		
	    Administrador entidad = mapper.map(data, Administrador.class);
	    entidad.setContrasena(passwordEncoder.encode(entidad.getContrasena()));
	    administradorRepository.save(entidad);
		return 0;
	}

	/**
	 * Obtiene todos los administradores registrados.
	 * @return lista de AdministradorDTO
	 * @throws BadRequestException si no se encuentran administradores
	 */
	@Override
	public List<AdministradorDTO> getAll() {
		List<Administrador> entityList = (List<Administrador>) administradorRepository.findAll();
		List<AdministradorDTO> dtoList = new ArrayList<>();
		
		entityList.forEach((entity) -> {
			dtoList.add(mapper.map(entity, AdministradorDTO.class));
		});
		
		if (dtoList.isEmpty()) {
			throw new BadRequestException("No se encontraron administradores");
		}
		
		return dtoList;
	}

	/**
	 * Elimina un administrador por su identificador.
	 * @param id identificador del administrador
	 * @return 0 si la operacion fue exitosa
	 * @throws BadRequestException si el ID es invalido
	 * @throws ResourceNotFoundException si el administrador no existe
	 */
	@Override
	public int deleteByID(Long id) {
		if (id == null || id < 0) {
			throw new BadRequestException("ID inválido");
		}
		
		Optional<Administrador> encontrado = administradorRepository.findById(id);
		
		if (!encontrado.isPresent()) {
			throw new ResourceNotFoundException("Administrador no encontrado con id: " + id);
		}
		
		administradorRepository.deleteById(id);
		return 0;
	}

	/**
	 * Actualiza los datos de un administrador existente.
	 * @param id identificador del administrador
	 * @param data nuevos datos del administrador
	 * @return 0 si la operacion fue exitosa
	 * @throws BadRequestException si el ID es invalido
	 * @throws ResourceNotFoundException si el administrador no existe
	 */
	@Override
	public int updateByID(Long id, AdministradorDTO data) {
		if (id == null || id <= 0) {
			throw new BadRequestException("ID inválido");
		}
		
		Optional<Administrador> encontrado = administradorRepository.findById(id);
		
		if (!encontrado.isPresent()) {
			throw new ResourceNotFoundException("Administrador no encontrado con id: " + id);
		}
		
		checkData(data);
		
		AdministradorDTO temp = mapper.map(encontrado.get(), AdministradorDTO.class);
		temp.setNombre(data.getNombre());
		temp.setContrasena(passwordEncoder.encode(data.getContrasena()));
		temp.setEmail(data.getEmail());
		temp.setTelefono(data.getTelefono());
		temp.setCargo(data.getCargo());
		
		administradorRepository.save(mapper.map(temp, Administrador.class));
		return 0;
	}

	/**
	 * Cuenta el numero total de administradores registrados.
	 * @return numero total de administradores
	 */
	@Override
	public long count() {
		return administradorRepository.count();
	}

	/**
	 * Verifica si existe un administrador con el identificador dado.
	 * @param id identificador del administrador
	 * @return true si existe, false en caso contrario
	 */
	@Override
	public boolean exist(Long id) {
		return administradorRepository.existsById(id);
	}

	/**
	 * Verifica si ya existe un administrador con el email proporcionado.
	 * @param email correo electronico a verificar
	 * @throws BadRequestException si el email ya esta registrado
	 */
	public boolean existByEmail(String email) {
		administradorRepository.findAll().forEach(administrador -> {
			if (administrador.getEmail().equals(email)) {
				throw new BadRequestException("El email ya esta registrado");
			}
		});
		return false;
	}

	/**
	 * Verifica si ya existe un administrador con el telefono proporcionado.
	 * @param telefono numero de telefono a verificar
	 * @throws BadRequestException si el telefono ya esta registrado
	 */
	public boolean existByTelefono(String telefono) {
		administradorRepository.findAll().forEach(administrador -> {
			if (administrador.getTelefono().equals(telefono)) {
				throw new BadRequestException("El telefono ya esta registrado");
			}
		});
		return false;
	}

	/**
	 * Verifica si ya existe un administrador con el nombre proporcionado.
	 * @param nombre nombre a verificar
	 * @throws BadRequestException si el nombre ya esta registrado
	 */
	public boolean existByNombre(String nombre) {
		administradorRepository.findAll().forEach(administrador -> {
			if (administrador.getNombre().equals(nombre)) {
				throw new BadRequestException("El nombre de administrador ya esta registrado");
			}
		});
		return false;
	}
	
	/**
	 * Valida todos los datos de un administrador incluyendo nombre, contraseña, email, telefono y cargo.
	 * @param data datos del administrador a validar
	 * @return true si todos los datos son validos
	 * @throws BadRequestException si algun dato es invalido
	 */
	public boolean checkData(AdministradorDTO data) {
	    validateNotNull(data);
	    validateNombre(data.getNombre());
	    validateContrasena(data.getContrasena());
	    validateEmail(data.getEmail());
	    validateTelefono(data.getTelefono());
	    validateCargo(data.getCargo());
	    return true;
	}

	/**
	 * Valida que los datos del administrador no sean nulos.
	 * @param data datos del administrador
	 * @throws BadRequestException si los datos son nulos
	 */
	private void validateNotNull(AdministradorDTO data) {
	    if (data == null) {
	        throw new BadRequestException("Datos de usuario no proporcionados");
	    }
	}

	/**
	 * Valida el nombre del administrador: longitud entre 6-50 caracteres, solo alfanumericos.
	 * @param nombre nombre a validar
	 * @throws BadRequestException si el nombre es invalido
	 */
	private void validateNombre(String nombre) {
	    if (nombre == null || nombre.length() < 6 || nombre.length() > 50) {
	        throw new BadRequestException("El nombre debe tener mínimo 6 caracteres y máximo 50");
	    }
	    if (!nombre.matches("^[a-zA-Z0-9]{6,50}$")) {
	        throw new BadRequestException("El nombre de usuario no puede contener espacios ni caracteres especiales");
	    }
	}

	/**
	 * Valida la contraseña: 8-64 caracteres con mayusculas, minusculas, numeros y especiales.
	 * @param contrasena contraseña a validar
	 * @throws BadRequestException si la contraseña es debil o invalida
	 */
	private void validateContrasena(String contrasena) {
	    if (contrasena == null || contrasena.isBlank() || contrasena.length() < 8 || contrasena.length() > 64) {
	        throw new BadRequestException("Contraseña inválida: mínimo 8 y máximo 64 caracteres");
	    }
	    if (!contrasena.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>?,./]).+$")) {
	        throw new BadRequestException("Contraseña débil: debe incluir mayúsculas, minúsculas, números y caracteres especiales");
	    }
	}

	/**
	 * Valida el formato del correo electronico.
	 * @param email correo a validar
	 * @throws BadRequestException si el email es invalido
	 */
	private void validateEmail(String email) {
	    if (email == null || email.isBlank() || email.length() > 120) {
	        throw new BadRequestException("Email inválido");
	    }
	    if (!email.trim().matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$") || email.contains(" ")) {
	        throw new BadRequestException("Formato de email inválido");
	    }
	}

	/**
	 * Valida el numero de telefono: solo numeros, exactamente 10 digitos.
	 * @param telefono telefono a validar
	 * @throws BadRequestException si el telefono es invalido
	 */
	private void validateTelefono(String telefono) {
	    if (telefono == null || telefono.isBlank() || !telefono.trim().matches("^\\d{10}$")) {
	        throw new BadRequestException("Teléfono inválido: solo números, de 10 dígitos");
	    }
	}
	
	/**
	 * Valida el cargo del administrador: entre 3 y 50 caracteres.
	 * @param cargo cargo a validar
	 * @throws BadRequestException si el cargo es invalido
	 */
	private void validateCargo(String cargo) {
	    if (cargo == null || cargo.isBlank() || cargo.length() < 3 || cargo.length() > 50) {
	        throw new BadRequestException("Cargo inválido: debe tener entre 3 y 50 caracteres");
	    }
	}
	
}
