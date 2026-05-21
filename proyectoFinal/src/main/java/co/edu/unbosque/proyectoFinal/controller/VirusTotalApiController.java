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
 * Controlador REST para la integración con la API pública de VirusTotal.
 *
 * <p>Expone dos operaciones bajo la ruta base {@code /virustotal}, accesibles
 * para los roles {@code USUARIO} y {@code ADMIN}:</p>
 * <ol>
 *   <li><strong>Subir archivo</strong> ({@code POST /virustotal/subir}): envía un
 *       archivo a VirusTotal para su análisis antivirus y registra la entrada
 *       en el historial del usuario.</li>
 *   <li><strong>Consultar análisis</strong> ({@code GET /virustotal/analisis}):
 *       recupera el resultado de un análisis previo usando su ID y actualiza
 *       el historial del usuario con los datos completos.</li>
 * </ol>
 *
 * <p>El flujo típico es: subir el archivo → recibir el {@code analysisId} →
 * consultar el análisis con ese ID hasta que el estado sea {@code "completed"}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see VirustotalService
 * @see UsuarioService
 */
@RestController
@RequestMapping("/virustotal")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
@Transactional
@SecurityRequirement(name = "bearerAuth")
public class VirusTotalApiController {

	/** Servicio que gestiona la comunicación HTTP con la API de VirusTotal. */
	@Autowired
	private VirustotalService virustotalService;

	/** Servicio de usuarios, usado para persistir el historial de análisis. */
	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Constructor vacío requerido por el framework Spring.
	 */
	public VirusTotalApiController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sube un archivo a VirusTotal para su análisis antivirus y registra el
	 * resultado preliminar en el historial del usuario.
	 *
	 * <p>Restricciones del archivo:</p>
	 * <ul>
	 *   <li>No puede estar vacío.</li>
	 *   <li>No puede superar 32 MB.</li>
	 *   <li>Si el archivo ya fue enviado previamente, VirusTotal devuelve un error
	 *       {@code AlreadySubmittedError} y el servicio lanza una excepción con el
	 *       hash SHA-256 del archivo para facilitar su identificación.</li>
	 * </ul>
	 *
	 * <p>La petición debe enviarse como {@code multipart/form-data} con el campo
	 * {@code file} conteniendo el archivo.</p>
	 *
	 * @param id   identificador del usuario que realiza el análisis
	 * @param file archivo a analizar (multipart)
	 * @return {@code 201 Created} con el ID del análisis ({@code analysisId}) como
	 *         texto plano, que debe usarse para consultar el resultado posterior
	 * @throws Exception si ocurre un error en la comunicación con VirusTotal
	 */
	@PostMapping(value = "/subir", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadFile(@RequestParam Long id,
			@RequestParam("file") MultipartFile file) throws Exception {
		VirusTotalUploadResponseDTO response = virustotalService.subirArchivo(file);
		usuarioService.agregarDatoHistorial(id, response);
		return new ResponseEntity<String>(response.getData().getId(), HttpStatus.CREATED);
	}

	/**
	 * Consulta el resultado de un análisis VirusTotal por su ID y actualiza el
	 * historial del usuario con los datos de detección completos.
	 *
	 * <p>Si el análisis aún está en cola ({@code status: "queued"}), el servicio
	 * lanza una {@link co.edu.unbosque.proyectoFinal.exception.QueueException} y
	 * el cliente debe reintentar más tarde. Si el análisis está completado
	 * ({@code status: "completed"}), se devuelve el objeto completo con las
	 * estadísticas de detección de cada motor antivirus.</p>
	 *
	 * @param id         identificador del usuario dueño del historial
	 * @param analysisId ID del análisis devuelto por {@code /virustotal/subir}
	 * @return {@code 200 OK} con el {@link VirusTotalUploadResponseDTO} completo
	 * @throws Exception si ocurre un error en la comunicación con VirusTotal
	 */
	@GetMapping("/analisis")
	public ResponseEntity<VirusTotalUploadResponseDTO> getAnalisis(@RequestParam long id,
			@RequestParam String analysisId) throws Exception {
		VirusTotalUploadResponseDTO response = virustotalService.getAnalysis(analysisId);
		usuarioService.actulizarDatoHistorial(id, response, analysisId);
		return new ResponseEntity<VirusTotalUploadResponseDTO>(response, HttpStatus.OK);
	}

}
