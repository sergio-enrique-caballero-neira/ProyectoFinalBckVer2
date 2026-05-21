package co.edu.unbosque.proyectoFinal.exception;

/**
 * Excepción lanzada cuando el archivo enviado a VirusTotal ya fue procesado
 * anteriormente por la API.
 *
 * <p>VirusTotal devuelve un error {@code AlreadySubmittedError} cuando se intenta
 * subir un archivo cuyo hash SHA-256 ya existe en su base de datos de análisis
 * recientes. En ese caso, {@link co.edu.unbosque.proyectoFinal.service.VirustotalService}
 * captura esta condición y lanza esta excepción incluyendo el hash SHA-256 del
 * archivo para que el cliente pueda buscar el análisis existente.</p>
 *
 * <p>El {@link GlobalExceptionHandler} la traduce a una respuesta
 * {@code 409 Conflict}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see GlobalExceptionHandler
 */
public class AlreadySubmittedException extends Exception {

	/**
	 * Crea una nueva excepción indicando que el archivo ya fue enviado previamente.
	 *
	 * @param mensaje mensaje descriptivo, típicamente incluye el hash SHA-256 del archivo
	 */
	public AlreadySubmittedException(String mensaje) {
		super(mensaje);
	}
}
