package co.edu.unbosque.proyectoFinal.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void testHandleQueue() {
		QueueException ex = new QueueException("En cola");

		ResponseEntity<String> response = handler.handleQueue(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("En cola", response.getBody());
	}

	@Test
	void testHandleEmpyFile() {
		FileEmpyException ex = new FileEmpyException("Archivo vacio");

		ResponseEntity<String> response = handler.handleEmpyFile(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Archivo vacio", response.getBody());
	}

	@Test
	void testHandleAlreadySubmitted() {
		AlreadySubmittedException ex = new AlreadySubmittedException("Ya enviado");

		ResponseEntity<String> response = handler.handleAlreadySubmitted(ex);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Ya enviado", response.getBody());
	}

	@Test
	void testHandleMaxUpload() {
		MaxUploadSizeExceededException ex = new MaxUploadSizeExceededException(50_000_000L);

		ResponseEntity<String> response = handler.handleMaxUpload(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("El archivo no puede pasar los 32mb", response.getBody());
	}

	@Test
	void testHandleClosed() {
		IllegalStateException ex = new IllegalStateException("Too many requests");

		ResponseEntity<String> response = handler.handleClosed(ex);

		assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
		assertEquals("Ah subido demasiados archivos muy seguido espere unos minutos", response.getBody());
	}

	@Test
	void testHandleResourceNotFound() {
		ResourceNotFoundException ex = new ResourceNotFoundException("No encontrado");

		ResponseEntity<String> response = handler.handleResourceNotFound(ex);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("No encontrado", response.getBody());
	}

	@Test
	void testHandleBadRequest() {
		BadRequestException ex = new BadRequestException("Solicitud invalida");

		ResponseEntity<String> response = handler.handleBadRequest(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Solicitud invalida", response.getBody());
	}

	@Test
	void testHandleAll() {
		Exception ex = new RuntimeException("Error inesperado");

		ResponseEntity<String> response = handler.handleAll(ex);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Error interno del servidor", response.getBody());
	}
}
