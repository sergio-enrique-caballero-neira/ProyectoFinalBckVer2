package co.edu.unbosque.proyectoFinal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectoFinal.dto.UsuarioDTO;
import co.edu.unbosque.proyectoFinal.dto.VirusTotalUploadResponseDTO;
import co.edu.unbosque.proyectoFinal.entity.Usuario;
import co.edu.unbosque.proyectoFinal.entity.VirusTotalUploadResponse;
import co.edu.unbosque.proyectoFinal.exception.*;
import co.edu.unbosque.proyectoFinal.repository.UsuarioRepository;

/**
 * Servicio para la gestion de usuarios regulares (USUARIO).
 * Implementa operaciones CRUD y manejo del historial de analisis de VirusTotal.
 */
@Service
public class UsuarioService implements CRUDoperation<UsuarioDTO> {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	/**
	 * Constructor vacio de UsuarioService.
	 */
	public UsuarioService() {
	}

	/**
	 * Crea un nuevo usuario validando los datos y encriptando la contraseña.
	 * @param data datos del usuario a crear
	 * @return 0 si la operacion fue exitosa
	 * @throws BadRequestException si los datos son invalidos o el usuario ya existe
	 */
	@Override
	public int create(UsuarioDTO data) {
		checkData(data);

		existByNombre(data.getNombre());
		existByEmail(data.getEmail());
		existByTelefono(data.getTelefono());
		
		Usuario entidad = mapper.map(data, Usuario.class);
	    entidad.setContrasena(passwordEncoder.encode(entidad.getContrasena()));

	    usuarioRepository.save(entidad);
		return 0;
	}

	/**
	 * Obtiene todos los usuarios registrados.
	 * @return lista de UsuarioDTO
	 * @throws ResourceNotFoundException si no se encuentran usuarios
	 */
	@Override
	public List<UsuarioDTO> getAll() {
		List<Usuario> entityList = (List<Usuario>) usuarioRepository.findAll();
		List<UsuarioDTO> dtoList = new ArrayList<>();

		entityList.forEach((entity) -> {
			dtoList.add(mapper.map(entity, UsuarioDTO.class));
		});

		if (dtoList.isEmpty()) {
			throw new ResourceNotFoundException("No se encontraron usuarios");
		}

		return dtoList;
	}

	/**
	 * Elimina un usuario por su identificador.
	 * @param id identificador del usuario
	 * @return 0 si la operacion fue exitosa
	 * @throws BadRequestException si el ID es invalido
	 * @throws ResourceNotFoundException si el usuario no existe
	 */
	@Override
	public int deleteByID(Long id) {
		if (id == null || id < 0) {
			throw new BadRequestException("ID inválido");
		}

		Optional<Usuario> encontrado = usuarioRepository.findById(id);
		if (!encontrado.isPresent()) {
			throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
		}

		usuarioRepository.deleteById(id);
		return 0;
	}

	/**
	 * Actualiza los datos de un usuario existente.
	 * @param id identificador del usuario
	 * @param data nuevos datos del usuario
	 * @return 0 si la operacion fue exitosa
	 * @throws BadRequestException si el ID es invalido
	 * @throws ResourceNotFoundException si el usuario no existe
	 */
	@Override
	public int updateByID(Long id, UsuarioDTO data) {
		if (id == null || id <= 0) {
			throw new BadRequestException("ID inválido");
		}
		
		Optional<Usuario> encontrado = usuarioRepository.findById(id);
		
		if (!encontrado.isPresent()) {
			throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
		}

		checkData(data);

		UsuarioDTO temp = mapper.map(encontrado.get(), UsuarioDTO.class);
		temp.setNombre(data.getNombre());
		temp.setContrasena(passwordEncoder.encode(data.getContrasena()));
		temp.setEmail(data.getEmail());
		temp.setTelefono(data.getTelefono());

		usuarioRepository.save(mapper.map(temp, Usuario.class));

		return 0;
	}

	/**
	 * Cuenta el numero total de usuarios registrados.
	 * @return numero total de usuarios
	 */
	@Override
	public long count() {
		return usuarioRepository.count();
	}

	/**
	 * Verifica si existe un usuario con el identificador dado.
	 * @param id identificador del usuario
	 * @return true si existe, false en caso contrario
	 */
	@Override
	public boolean exist(Long id) {
		return usuarioRepository.existsById(id);
	}

	/**
	 * Verifica si ya existe un usuario con el email proporcionado.
	 * @param email correo electronico a verificar
	 * @throws BadRequestException si el email ya esta registrado
	 */
	public boolean existByEmail(String email) {
		usuarioRepository.findAll().forEach(usuario -> {
			if (usuario.getEmail().equals(email)) {
				throw new BadRequestException("El email ya esta registrado");
			}
		});
		return false;
	}

	/**
	 * Verifica si ya existe un usuario con el telefono proporcionado.
	 * @param telefono numero de telefono a verificar
	 * @throws BadRequestException si el telefono ya esta registrado
	 */
	public boolean existByTelefono(String telefono) {
		usuarioRepository.findAll().forEach(usuario -> {
			if (usuario.getTelefono().equals(telefono)) {
				throw new BadRequestException("El telefono ya esta registrado");
			}
		});
		return false;
	}

	/**
	 * Verifica si ya existe un usuario con el nombre proporcionado.
	 * @param nombre nombre de usuario a verificar
	 * @throws BadRequestException si el nombre ya esta registrado
	 */
	public boolean existByNombre(String nombre) {
		usuarioRepository.findAll().forEach(usuario -> {
			if (usuario.getNombre().equals(nombre)) {
				throw new BadRequestException("El nombre de usuario ya esta registrado");
			}
		});
		return false;
	}

	/**
	 * Agrega una respuesta de analisis de VirusTotal al historial de un usuario.
	 * @param id identificador del usuario
	 * @param datoDTO datos de la respuesta de VirusTotal
	 * @return 0 si la operacion fue exitosa
	 * @throws ResourceNotFoundException si el usuario no existe
	 * @throws BadRequestException si el archivo ya fue escaneado previamente
	 */
	public int agregarDatoHistorial(Long id, VirusTotalUploadResponseDTO datoDTO) {

		Optional<Usuario> encontrado = usuarioRepository.findById(id);

		if (!encontrado.isPresent()) {
			throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
		}

		Usuario temp = encontrado.get();

		temp.getHistorial().forEach(virustotalresponse -> {
			if (virustotalresponse.getData().getId().equals(datoDTO.getData().getId())) {
				throw new BadRequestException("El archivo ya fue scaneado, revise su historial de analisis");
			}
		});

		VirusTotalUploadResponse dato = mapper.map(datoDTO, VirusTotalUploadResponse.class);

		temp.getHistorial().add(dato);

		usuarioRepository.save(mapper.map(temp, Usuario.class));

		return 0;
	}

	/**
	 * Actualiza un analisis existente en el historial de un usuario con los resultados de VirusTotal.
	 * @param id identificador del usuario
	 * @param datoDTO datos actualizados de la respuesta de VirusTotal
	 * @param analysisId identificador del analisis a actualizar
	 * @return 0 si la operacion fue exitosa
	 * @throws ResourceNotFoundException si el usuario no existe
	 */
	public int actulizarDatoHistorial(Long id, VirusTotalUploadResponseDTO datoDTO, String analysisId) {

		VirusTotalUploadResponse dato = mapper.map(datoDTO, VirusTotalUploadResponse.class);

		Optional<Usuario> encontrado = usuarioRepository.findById(id);

		if (!encontrado.isPresent()) {
			throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
		}

		Usuario temp = encontrado.get();

		for (VirusTotalUploadResponse response : temp.getHistorial()) {
			if (response.getData().getId().equals(analysisId)) {
				response.getData().setAttributes(dato.getData().getAttributes());
				response.getData().setId(dato.getData().getId());
				break;
			}
		}

		usuarioRepository.save(mapper.map(temp, Usuario.class));

		return 0;
	}
	
	/**
	 * Obtiene el identificador de un usuario por su nombre.
	 * @param nombre nombre del usuario
	 * @return identificador del usuario
	 */
	public long getIdByUsername(String nombre) {
		return usuarioRepository.findByNombre(nombre).get().getId();
	}
	
	/**
	 * Valida los datos de un usuario incluyendo nombre, contraseña, email y telefono.
	 * @param data datos del usuario a validar
	 * @return true si todos los datos son validos
	 * @throws BadRequestException si algun dato es invalido
	 */
	public boolean checkData(UsuarioDTO data) {
	    validateNotNull(data);
	    validateNombre(data.getNombre());
	    validateContrasena(data.getContrasena());
	    validateEmail(data.getEmail());
	    validateTelefono(data.getTelefono());
	    return true;
	}

	/**
	 * Valida que los datos del usuario no sean nulos.
	 * @param data datos del usuario
	 * @throws BadRequestException si los datos son nulos
	 */
	private void validateNotNull(UsuarioDTO data) {
	    if (data == null) {
	        throw new BadRequestException("Datos de usuario no proporcionados");
	    }
	}

	/**
	 * Valida el nombre de usuario: longitud entre 6-50 caracteres, solo alfanumericos.
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
	 * Obtiene el historial de analisis de VirusTotal de un usuario por su ID.
	 * @param id identificador del usuario
	 * @return lista de respuestas de VirusTotal
	 * @throws ResourceNotFoundException si el usuario no existe
	 */
	public List<VirusTotalUploadResponseDTO> getHistorialById(long id) {
		Optional<Usuario> encontrado = usuarioRepository.findById(id);

		if (!encontrado.isPresent()) {
			throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
		}

		List<VirusTotalUploadResponseDTO> historialDTO = new ArrayList<>();

		encontrado.get().getHistorial().forEach(dato -> {
			historialDTO.add(mapper.map(dato, VirusTotalUploadResponseDTO.class));
		});

		return historialDTO;
	}

}
