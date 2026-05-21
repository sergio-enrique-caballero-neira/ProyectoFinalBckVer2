package co.edu.unbosque.proyectoFinal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Manejador global de excepciones para todos los controladores REST.
 *
 * <p>Anotado con {@code @RestControllerAdvice}, intercepta las excepciones
 * lanzadas por cualquier controlador de la aplicación y las convierte en
 * respuestas HTTP con el código de estado apropiado y un mensaje descriptivo.</p>
 *
 * <p><strong>Tabla de manejo de excepciones:</strong></p>
 * <table border="1">
 *   <tr><th>Excepción</th><th>HTTP</th><th>Descripción</th></tr>
 *   <tr><td>{@link QueueException}</td><td>400</td><td>Análisis en cola en VirusTotal</td></tr>
 *   <tr><td>{@link FileEmpyException}</td><td>400</td><td>Archivo vacío</td></tr>
 *   <tr><td>{@link BadRequestException}</td><td>400</td><td>Solicitud inválida</td></tr>
 *   <tr><td>{@link MaxUploadSizeExceededException}</td><td>400</td><td>Archivo mayor a 32 MB</td></tr>
 *   <tr><td>{@link AlreadySubmittedException}</td><td>409</td><td>Archivo ya enviado a VirusTotal</td></tr>
 *   <tr><td>{@link IllegalStateException}</td><td>429</td><td>Demasiadas subidas seguidas</td></tr>
 *   <tr><td>{@link ResourceNotFoundException}</td><td>404</td><td>Recurso no encontrado</td></tr>
 *   <tr><td>{@link Exception}</td><td>500</td><td>Error interno no controlado</td></tr>
 * </table>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Maneja {@link QueueException}: el análisis VirusTotal aún está en cola.
	 *
	 * @param ex excepción capturada
	 * @return {@code 400 Bad Request} con el mensaje de la excepción
	 */
	@ExceptionHandler(QueueException.class)
	public ResponseEntity<String> handleQueue(QueueException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja {@link FileEmpyException}: el archivo enviado está vacío.
	 *
	 * @param ex excepción capturada
	 * @return {@code 400 Bad Request} con el mensaje de la excepción
	 */
	@ExceptionHandler(FileEmpyException.class)
	public ResponseEntity<String> handleEmpyFile(FileEmpyException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja {@link AlreadySubmittedException}: el archivo ya fue enviado previamente.
	 *
	 * @param ex excepción capturada
	 * @return {@code 409 Conflict} con el hash SHA-256 del archivo duplicado
	 */
	@ExceptionHandler(AlreadySubmittedException.class)
	public ResponseEntity<String> handleAlreadySubmitted(AlreadySubmittedException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
	}

	/**
	 * Maneja {@link MaxUploadSizeExceededException}: el archivo supera el límite de 32 MB.
	 *
	 * @param ex excepción capturada
	 * @return {@code 400 Bad Request} con mensaje de tamaño máximo
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<String> handleMaxUpload(MaxUploadSizeExceededException ex) {
		return new ResponseEntity<String>("El archivo no puede pasar los 32mb", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja {@link IllegalStateException}: límite de subidas frecuentes a VirusTotal.
	 *
	 * @param ex excepción capturada
	 * @return {@code 429 Too Many Requests} con mensaje de espera
	 */
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<String> handleClosed(IllegalStateException ex) {
		return new ResponseEntity<String>("Ah subido demasiados archivos muy seguido espere unos minutos",
				HttpStatus.TOO_MANY_REQUESTS);
	}

	/**
	 * Maneja {@link ResourceNotFoundException}: el recurso no existe en el sistema.
	 *
	 * @param ex excepción capturada
	 * @return {@code 404 Not Found} con el mensaje de la excepción
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Maneja {@link BadRequestException}: solicitud inválida del cliente.
	 *
	 * @param ex excepción capturada
	 * @return {@code 400 Bad Request} con el mensaje de la excepción
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja cualquier otra excepción no controlada explícitamente.
	 *
	 * @param ex excepción capturada
	 * @return {@code 500 Internal Server Error} con mensaje genérico
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleAll(Exception ex) {
		return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
