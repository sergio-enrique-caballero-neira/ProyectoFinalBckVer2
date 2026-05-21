package co.edu.unbosque.proyectoFinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unbosque.proyectoFinal.dto.VirusTotalUploadResponseDTO;
import co.edu.unbosque.proyectoFinal.service.UsuarioService;
import co.edu.unbosque.proyectoFinal.service.VirustotalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador REST para la integracion con la API de VirusTotal.
 * Permite subir archivos para escaneo y consultar los resultados de analisis.
 */
@RestController
@RequestMapping("/virustotal")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
@Transactional
@SecurityRequirement(name = "bearerAuth")
public class VirusTotalApiController {

	@Autowired
	private VirustotalService virustotalService;

	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Constructor vacio de VirusTotalApiController.
	 */
	public VirusTotalApiController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sube un archivo a VirusTotal para escaneo y lo agrega al historial del usuario.
	 * @param id identificador del usuario
	 * @param archivo archivo a escanear
	 * @return ResponseEntity con el ID del analisis y estado 201 CREATED
	 * @throws Exception si ocurre un error durante la subida
	 */
	@PostMapping(value = "/subir", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadFile(@RequestParam Long id, @RequestParam("file") MultipartFile file) throws Exception {
		VirusTotalUploadResponseDTO response = virustotalService.subirArchivo(file);
		usuarioService.agregarDatoHistorial(id, response);
		return new ResponseEntity<String>(response.getData().getId(), HttpStatus.CREATED);
	}

	/**
	 * Obtiene los resultados de un analisis de VirusTotal por su ID y actualiza el historial del usuario.
	 * @param id identificador del usuario
	 * @param analysisId identificador del analisis en VirusTotal
	 * @return ResponseEntity con los resultados del analisis y estado 200 OK
	 * @throws Exception si ocurre un error durante la consulta
	 */
	@GetMapping("/analisis")
	public ResponseEntity<VirusTotalUploadResponseDTO> getAnalisis(@RequestParam long id, @RequestParam String analysisId) throws Exception {
		VirusTotalUploadResponseDTO response = virustotalService.getAnalysis(analysisId);
		usuarioService.actulizarDatoHistorial(id, response, analysisId);
		return new ResponseEntity<VirusTotalUploadResponseDTO>(response, HttpStatus.OK);
	}

}
