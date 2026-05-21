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

import co.edu.unbosque.proyectoFinal.dto.UsuarioDTO;
import co.edu.unbosque.proyectoFinal.dto.VirusTotalUploadResponseDTO;
import co.edu.unbosque.proyectoFinal.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador REST para la gestión de usuarios normales del sistema.
 *
 * <p>Expone operaciones CRUD sobre la entidad {@code Usuario} y consultas
 * relacionadas con el historial de análisis VirusTotal bajo la ruta base
 * {@code /usuario}. Los permisos por endpoint son:</p>
 *
 * <ul>
 *   <li>{@code POST   /usuario/crear}            → Público (sin autenticación)</li>
 *   <li>{@code GET    /usuario/mostrartodo}       → Roles {@code USUARIO} y {@code ADMIN}</li>
 *   <li>{@code GET    /usuario/getIdByUsername}   → Roles {@code USUARIO} y {@code ADMIN}</li>
 *   <li>{@code GET    /usuario/getHistorialById}  → Roles {@code USUARIO} y {@code ADMIN}</li>
 *   <li>{@code PUT    /usuario/actualizar}        → Solo rol {@code ADMIN}</li>
 *   <li>{@code DELETE /usuario/eliminar}          → Solo rol {@code ADMIN}</li>
 * </ul>
 *
 * <p>Todas las operaciones son transaccionales y se delegan a {@link UsuarioService}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see UsuarioService
 */
@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
@Transactional
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

	/** Servicio que encapsula la lógica de negocio de usuarios. */
	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Constructor vacío requerido por el framework Spring.
	 */
	public UsuarioController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Registra un nuevo usuario normal en el sistema.
	 *
	 * <p>Este endpoint es público y no requiere autenticación. Valida que el
	 * nombre, email y teléfono sean únicos y que la contraseña cumpla las
	 * políticas de seguridad. La contraseña se almacena cifrada con BCrypt.</p>
	 *
	 * @param nombre     nombre único del usuario (6-50 chars alfanuméricos)
	 * @param email      dirección de correo electrónico válida y única
	 * @param telefono   número de teléfono de 10 dígitos, único en el sistema
	 * @param contrasena contraseña en texto plano (mín. 8 chars, con mayúsculas,
	 *                   minúsculas, números y caracteres especiales)
	 * @return {@code 201 Created} con mensaje de confirmación
	 */
	@PostMapping("/crear")
	public ResponseEntity<String> crearUsuarioNormal(@RequestParam String nombre, @RequestParam String email,
			@RequestParam String telefono, @RequestParam String contrasena) {
		UsuarioDTO nuevo = new UsuarioDTO(nombre, email, telefono, contrasena);
		usuarioService.create(nuevo);
		return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
	}

	/**
	 * Retorna la lista completa de usuarios normales registrados en el sistema.
	 *
	 * @return {@code 202 Accepted} con la lista de {@link UsuarioDTO}, o error
	 *         si no hay usuarios registrados
	 */
	@GetMapping("/mostrartodo")
	public ResponseEntity<List<UsuarioDTO>> mostrarTodo() {
		return new ResponseEntity<>(usuarioService.getAll(), HttpStatus.ACCEPTED);
	}

	/**
	 * Obtiene el identificador numérico de un usuario dado su nombre de usuario.
	 *
	 * <p>Útil para que el frontend obtenga el ID del usuario autenticado justo
	 * después del login, antes de realizar operaciones de historial VirusTotal.</p>
	 *
	 * @param nombre nombre único del usuario a consultar
	 * @return {@code 202 Accepted} con el ID del usuario como {@code Long}
	 */
	@GetMapping("/getIdByUsername")
	public ResponseEntity<Long> getIdByUsername(@RequestParam String nombre) {
		return new ResponseEntity<>(usuarioService.getIdByUsername(nombre), HttpStatus.ACCEPTED);
	}

	/**
	 * Retorna el historial completo de análisis VirusTotal de un usuario por su ID.
	 *
	 * <p>Cada elemento del historial representa una subida de archivo y su
	 * resultado de análisis antivirus asociado.</p>
	 *
	 * @param id identificador único del usuario
	 * @return {@code 202 Accepted} con la lista de {@link VirusTotalUploadResponseDTO}
	 *         del usuario, o error si el ID no existe
	 */
	@GetMapping("/getHistorialById")
	public ResponseEntity<List<VirusTotalUploadResponseDTO>> getHistorialById(@RequestParam long id) {
		return new ResponseEntity<>(usuarioService.getHistorialById(id), HttpStatus.ACCEPTED);
	}

	/**
	 * Actualiza todos los datos de un usuario existente identificado por su ID.
	 *
	 * <p>Reemplaza nombre, email, teléfono y contraseña con los nuevos valores.
	 * La nueva contraseña se almacena cifrada con BCrypt. Requiere rol
	 * {@code ADMIN}.</p>
	 *
	 * @param id         identificador único del usuario a actualizar
	 * @param nombre     nuevo nombre del usuario
	 * @param email      nuevo email del usuario
	 * @param telefono   nuevo teléfono del usuario
	 * @param contrasena nueva contraseña en texto plano
	 * @return {@code 202 Accepted} con mensaje de confirmación, o error si el ID no existe
	 */
	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizar(@RequestParam long id, @RequestParam String nombre,
			@RequestParam String email, @RequestParam String telefono, @RequestParam String contrasena) {
		UsuarioDTO nuevo = new UsuarioDTO(nombre, email, telefono, contrasena);
		usuarioService.updateByID(id, nuevo);
		return new ResponseEntity<>("Dato actualizado con exito", HttpStatus.ACCEPTED);
	}

	/**
	 * Elimina un usuario del sistema por su identificador único.
	 *
	 * <p>La eliminación es en cascada: también se eliminan todos los registros
	 * del historial VirusTotal asociados al usuario. Requiere rol {@code ADMIN}.</p>
	 *
	 * @param id identificador único del usuario a eliminar
	 * @return {@code 202 Accepted} con mensaje de confirmación, o error si el ID no existe
	 */
	@DeleteMapping("/eliminar")
	public ResponseEntity<String> eliminar(@RequestParam long id) {
		usuarioService.deleteByID(id);
		return new ResponseEntity<>("Dato eliminado con exito", HttpStatus.ACCEPTED);
	}
}
