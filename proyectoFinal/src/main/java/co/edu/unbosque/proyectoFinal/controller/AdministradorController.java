package co.edu.unbosque.proyectoFinal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectoFinal.dto.AdministradorDTO;
import co.edu.unbosque.proyectoFinal.service.AdministradorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador REST para la gestión de administradores del sistema.
 *
 * <p>Expone operaciones CRUD sobre la entidad {@code Administrador} bajo la
 * ruta base {@code /administrador}. Todos los endpoints de este controlador
 * requieren autenticación JWT con rol {@code ADMIN}.</p>
 *
 * <p>Las operaciones son transaccionales ({@code @Transactional}) y se
 * delegan completamente a {@link AdministradorService}.</p>
 *
 * <p><strong>Endpoints disponibles:</strong></p>
 * <ul>
 *   <li>{@code POST   /administrador/crear}       – Crea un nuevo administrador.</li>
 *   <li>{@code GET    /administrador/mostrartodo} – Lista todos los administradores.</li>
 *   <li>{@code PUT    /administrador/actualizar}  – Actualiza un administrador por ID.</li>
 *   <li>{@code DELETE /administrador/eliminar}    – Elimina un administrador por ID.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see AdministradorService
 */
@RestController
@RequestMapping("/administrador")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
@Transactional
@SecurityRequirement(name = "bearerAuth")
public class AdministradorController {

	/** Servicio que encapsula la lógica de negocio de administradores. */
	@Autowired
	private AdministradorService administradorService;

	/**
	 * Constructor vacío requerido por el framework Spring.
	 */
	public AdministradorController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Crea un nuevo administrador en el sistema.
	 *
	 * <p>Valida que el nombre, email y teléfono no estén ya registrados,
	 * y que la contraseña cumpla las políticas de seguridad definidas en
	 * {@link AdministradorService}. La contraseña se almacena cifrada con BCrypt.</p>
	 *
	 * @param nombre     nombre único del administrador (6-50 chars alfanuméricos)
	 * @param email      dirección de correo electrónico válida y única
	 * @param telefono   número de teléfono de 10 dígitos, único en el sistema
	 * @param contrasena contraseña en texto plano (mín. 8 chars, debe incluir
	 *                   mayúsculas, minúsculas, números y caracteres especiales)
	 * @param cargo      cargo o título del administrador (3-50 chars)
	 * @return {@code 201 Created} con mensaje de confirmación
	 */
	@PostMapping("/crear")
	public ResponseEntity<String> crearUsuarioNormal(@RequestParam String nombre, @RequestParam String email,
			@RequestParam String telefono, @RequestParam String contrasena, @RequestParam String cargo) {
		AdministradorDTO nuevo = new AdministradorDTO(nombre, email, telefono, contrasena, cargo);
		administradorService.create(nuevo);
		return new ResponseEntity<>("Dato Creado con exito", HttpStatus.CREATED);
	}

	/**
	 * Retorna la lista completa de administradores registrados en el sistema.
	 *
	 * <p>Si no existe ningún administrador, el servicio lanza una excepción
	 * que el {@link co.edu.unbosque.proyectoFinal.exception.GlobalExceptionHandler}
	 * traduce a una respuesta {@code 400 Bad Request}.</p>
	 *
	 * @return {@code 202 Accepted} con la lista de {@link AdministradorDTO}
	 */
	@GetMapping("/mostrartodo")
	public ResponseEntity<List<AdministradorDTO>> mostrarTodo() {
		return new ResponseEntity<>(administradorService.getAll(), HttpStatus.ACCEPTED);
	}

	/**
	 * Actualiza los datos de un administrador existente identificado por su ID.
	 *
	 * <p>Reemplaza todos los campos del administrador (nombre, email, teléfono,
	 * contraseña y cargo) con los nuevos valores proporcionados. La nueva
	 * contraseña se almacena cifrada con BCrypt.</p>
	 *
	 * @param id         identificador único del administrador a actualizar
	 * @param nombre     nuevo nombre del administrador
	 * @param email      nuevo email del administrador
	 * @param telefono   nuevo teléfono del administrador
	 * @param contrasena nueva contraseña en texto plano
	 * @param cargo      nuevo cargo del administrador
	 * @return {@code 202 Accepted} con mensaje de confirmación, o error si el ID no existe
	 */
	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizar(@RequestParam long id, @RequestParam String nombre,
			@RequestParam String email, @RequestParam String telefono, @RequestParam String contrasena,
			@RequestParam String cargo) {
		AdministradorDTO nuevo = new AdministradorDTO(nombre, email, telefono, contrasena, cargo);
		administradorService.updateByID(id, nuevo);
		return new ResponseEntity<>("Dato actualizado con exito", HttpStatus.ACCEPTED);
	}

	/**
	 * Elimina un administrador del sistema por su identificador único.
	 *
	 * <p>Si el ID no corresponde a ningún administrador registrado, el servicio
	 * lanza una {@link co.edu.unbosque.proyectoFinal.exception.ResourceNotFoundException}.</p>
	 *
	 * @param id identificador único del administrador a eliminar
	 * @return {@code 202 Accepted} con mensaje de confirmación, o error si el ID no existe
	 */
	@DeleteMapping("/eliminar")
	public ResponseEntity<String> eliminar(@RequestParam long id) {
		administradorService.deleteByID(id);
		return new ResponseEntity<>("Dato eliminado con exito", HttpStatus.ACCEPTED);
	}

}
