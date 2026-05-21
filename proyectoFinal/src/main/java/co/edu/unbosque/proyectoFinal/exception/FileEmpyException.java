package co.edu.unbosque.proyectoFinal.exception;

/**
 * Excepción lanzada cuando el archivo enviado para análisis está vacío.
 *
 * <p>Se lanza en {@link co.edu.unbosque.proyectoFinal.service.VirustotalService}
 * cuando el {@code MultipartFile} recibido no contiene datos (su tamaño es 0
 * o el método {@code isEmpty()} retorna {@code true}).</p>
 *
 * <p>El {@link GlobalExceptionHandler} la traduce a una respuesta
 * {@code 400 Bad Request}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see GlobalExceptionHandler
 */
public class FileEmpyException extends Exception {

	/**
	 * Crea una nueva excepción indicando que el archivo enviado está vacío.
	 *
	 * @param mensaje descripción del error
	 */
	public FileEmpyException(String mensaje) {
		super(mensaje);
	}
}
