package co.edu.unbosque.proyectoFinal.exception;

/**
 * Excepción lanzada cuando el análisis de VirusTotal aún se encuentra en cola
 * y no ha sido procesado.
 *
 * <p>VirusTotal procesa los archivos de forma asíncrona. Cuando se consulta el
 * resultado de un análisis y su estado es {@code "queued"}, significa que aún
 * no ha sido procesado por los motores antivirus. En ese caso,
 * {@link co.edu.unbosque.proyectoFinal.service.VirustotalService} lanza esta
 * excepción para que el cliente sepa que debe reintentar más tarde.</p>
 *
 * <p>El {@link GlobalExceptionHandler} la traduce a una respuesta
 * {@code 400 Bad Request}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see GlobalExceptionHandler
 */
public class QueueException extends Exception {

	/**
	 * Crea una nueva excepción indicando que el análisis está en cola.
	 *
	 * @param mensaje descripción del estado de la cola
	 */
	public QueueException(String mensaje) {
		super(mensaje);
	}
}
