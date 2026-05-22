package co.edu.unbosque.proyectoFinal.controller;

import java.util.ArrayList;
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
 * Controlador REST para la gestion de usuarios regulares (USUARIO).
 * Expone endpoints CRUD para crear, listar, actualizar y eliminar usuarios,
 * ademas de consultar el historial de analisis de VirusTotal.
 */
@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
@Transactional
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {
	
	/** Servicio de logica de negocio para usuarios. */
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Constructor vacio de UsuarioController.
	 */
	public UsuarioController() {
	}
	
    /**
     * Crea un nuevo usuario regular con los datos proporcionados.
     * @param nombre nombre de usuario
     * @param email correo electronico
     * @param telefono numero de telefono
     * @param contrasena contraseña del usuario
     * @return ResponseEntity con mensaje de exito y estado 201 CREATED
     */
    @PostMapping("/crear")
    public ResponseEntity<String> crearUsuarioNormal(@RequestParam String nombre, @RequestParam String email, @RequestParam String telefono, @RequestParam String contrasena){
        UsuarioDTO nuevo = new UsuarioDTO(nombre, email, telefono,contrasena, new ArrayList<VirusTotalUploadResponseDTO>());
        usuarioService.create(nuevo);
        return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
    }

    /**
     * Obtiene la lista completa de usuarios registrados.
     * @return ResponseEntity con lista de UsuarioDTO y estado 202 ACCEPTED
     */
    @GetMapping("/mostrartodo")
    public ResponseEntity<List<UsuarioDTO>> mostrarTodo(){
        return new ResponseEntity<>(usuarioService.getAll(), HttpStatus.ACCEPTED);
    }
    
    /**
     * Obtiene el ID de un usuario por su nombre.
     * @param nombre nombre del usuario
     * @return ResponseEntity con el ID del usuario y estado 202 ACCEPTED
     */
    @GetMapping("/getIdByUsername")
    public ResponseEntity<Long> getIdByUsername(@RequestParam String nombre){
		return new ResponseEntity<>(usuarioService.getIdByUsername(nombre), HttpStatus.ACCEPTED);
	}
    
    /**
     * Obtiene el historial de analisis de VirusTotal de un usuario por su ID.
     * @param id identificador del usuario
     * @return ResponseEntity con lista de respuestas de VirusTotal y estado 202 ACCEPTED
     */
    @GetMapping("/getHistorialById")
    public ResponseEntity<List<VirusTotalUploadResponseDTO>> getHistorialById(@RequestParam long id){
		return new ResponseEntity<>(usuarioService.getHistorialById(id), HttpStatus.ACCEPTED);
	}
    
    /**
     * Actualiza los datos de un usuario existente.
     * @param id identificador del usuario
     * @param nombre nuevo nombre de usuario
     * @param email nuevo correo electronico
     * @param telefono nuevo numero de telefono
     * @param contrasena nueva contraseña
     * @return ResponseEntity con mensaje de exito y estado 202 ACCEPTED
     */
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizar(@RequestParam long id, @RequestParam String nombre, @RequestParam String email, @RequestParam String telefono, @RequestParam String contrasena){
        UsuarioDTO nuevo = new UsuarioDTO(nombre, email, telefono, contrasena);
        usuarioService.updateByID(id,nuevo);
        return new ResponseEntity<>("Dato actualizado con exito", HttpStatus.ACCEPTED);
    }
    
    /**
     * Elimina un usuario por su identificador.
     * @param id identificador del usuario a eliminar
     * @return ResponseEntity con mensaje de exito y estado 202 ACCEPTED
     */
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminar(@RequestParam long id){
        usuarioService.deleteByID(id);
        return new ResponseEntity<>("Dato eliminado con exito", HttpStatus.ACCEPTED);
    }
}
