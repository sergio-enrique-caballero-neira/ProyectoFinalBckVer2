package co.edu.unbosque.proyectoFinal.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no se encuentra en el sistema.
 *
 * <p>Se usa en los servicios para señalar que la entidad buscada por ID o por
 * nombre no existe en la base de datos. Ejemplos de uso:</p>
 * <ul>
 *   <li>Usuario o administrador no encontrado por ID en operaciones de
 *       actualización, eliminación o historial.</li>
 *   <li>Lista de usuarios o administradores vacía al consultar todos.</li>
 *   <li>Análisis VirusTotal no encontrado con el ID proporcionado.</li>
 * </ul>
 *
 * <p>El {@link GlobalExceptionHandler} la traduce a una respuesta
 * {@code 404 Not Found}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see GlobalExceptionHandler
 */
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * Crea una nueva excepción de recurso no encontrado con el mensaje especificado.
	 *
	 * @param mensaje descripción del recurso que no se encontró
	 */
	public ResourceNotFoundException(String mensaje) {
		super(mensaje);
	}
}
