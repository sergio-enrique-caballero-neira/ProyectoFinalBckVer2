package co.edu.unbosque.proyectoFinal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Manejador global de excepciones para la API.
 * Captura y transforma excepciones en respuestas HTTP con codigos de estado apropiados.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * Maneja excepciones de analisis en cola.
	 * @param ex excepcion lanzada
	 * @return respuesta con estado 400 BAD REQUEST
	 */
	@ExceptionHandler(QueueException.class)
    public ResponseEntity<String> handleQueue(QueueException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
	
	/**
	 * Maneja excepciones de archivo vacio.
	 * @param ex excepcion lanzada
	 * @return respuesta con estado 400 BAD REQUEST
	 */
	@ExceptionHandler(FileEmpyException.class)
    public ResponseEntity<String> handleEmpyFile(FileEmpyException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
	
	/**
	 * Maneja excepciones de archivo ya enviado.
	 * @param ex excepcion lanzada
	 * @return respuesta con estado 409 CONFLICT
	 */
	@ExceptionHandler(AlreadySubmittedException.class)
    public ResponseEntity<String> handleAlreadySubmitted(AlreadySubmittedException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }
	
	/**
	 * Maneja excepciones de tamaño maximo de archivo excedido.
	 * @param ex excepcion lanzada
	 * @return respuesta con estado 400 BAD REQUEST
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUpload(MaxUploadSizeExceededException ex) {
        return new ResponseEntity<String>("El archivo no puede pasar los 32mb", HttpStatus.BAD_REQUEST);
    }
	
	/**
	 * Maneja excepciones de estado ilegal (demasiados archivos subidos muy seguido).
	 * @param ex excepcion lanzada
	 * @return respuesta con estado 429 TOO MANY REQUESTS
	 */
	@ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleClosed(IllegalStateException ex) {
        return new ResponseEntity<String>("Ah subido demasiados archivos muy seguido espere unos minutos", HttpStatus.TOO_MANY_REQUESTS);
    }
	
	/**
	 * Maneja excepciones de recurso no encontrado.
	 * @param ex excepcion lanzada
	 * @return respuesta con estado 404 NOT FOUND
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
	
    /**
     * Maneja excepciones de solicitud incorrecta.
     * @param ex excepcion lanzada
     * @return respuesta con estado 400 BAD REQUEST
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja cualquier otra excepcion no controlada.
     * @param ex la excepcion lanzada
     * @return respuesta con estado 500 y mensaje generico de error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
