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
 * Controlador REST para la gestion de administradores (ADMIN).
 * Expone endpoints CRUD para crear, listar, actualizar y eliminar administradores.
 */
@RestController
@RequestMapping("/administrador")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
@Transactional
@SecurityRequirement(name = "bearerAuth")
public class AdministradorController {
	
	@Autowired
	private AdministradorService administradorService;
	
	/**
	 * Constructor vacio de AdministradorController.
	 */
	public AdministradorController() {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Crea un nuevo administrador con los datos proporcionados.
     * @param nombre nombre del administrador
     * @param email correo electronico
     * @param telefono numero de telefono
     * @param contrasena contraseña del administrador
     * @param cargo cargo del administrador
     * @return ResponseEntity con mensaje de exito y estado 201 CREATED
     */
    @PostMapping("/crear")
    public ResponseEntity<String> crearUsuarioNormal(@RequestParam String nombre, @RequestParam String email, @RequestParam String telefono, @RequestParam String contrasena, @RequestParam String cargo){
        AdministradorDTO nuevo = new AdministradorDTO(nombre, email, telefono,contrasena, cargo);
        administradorService.create(nuevo);
        return new ResponseEntity<>("Dato Creado con exito", HttpStatus.CREATED);
    }

    /**
     * Obtiene la lista completa de administradores registrados.
     * @return ResponseEntity con lista de AdministradorDTO y estado 202 ACCEPTED
     */
    @GetMapping("/mostrartodo")
    public ResponseEntity<List<AdministradorDTO>> mostrarTodo(){
        return new ResponseEntity<>(administradorService.getAll(), HttpStatus.ACCEPTED);
    }
    
    /**
     * Actualiza los datos de un administrador existente.
     * @param id identificador del administrador
     * @param nombre nuevo nombre
     * @param email nuevo correo electronico
     * @param telefono nuevo numero de telefono
     * @param contrasena nueva contraseña
     * @param cargo nuevo cargo
     * @return ResponseEntity con mensaje de exito y estado 202 ACCEPTED
     */
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizar(@RequestParam long id, @RequestParam String nombre, @RequestParam String email, @RequestParam String telefono, @RequestParam String contrasena, @RequestParam String cargo){
    	AdministradorDTO nuevo = new AdministradorDTO(nombre, email, telefono, contrasena, cargo);
    	administradorService.updateByID(id,nuevo);
        return new ResponseEntity<>("Dato actualizado con exito", HttpStatus.ACCEPTED);
    }
    
    /**
     * Elimina un administrador por su identificador.
     * @param id identificador del administrador a eliminar
     * @return ResponseEntity con mensaje de exito y estado 202 ACCEPTED
     */
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminar(@RequestParam long id){
    	administradorService.deleteByID(id);
        return new ResponseEntity<>("Dato eliminado con exito", HttpStatus.ACCEPTED);
    }
	
}
