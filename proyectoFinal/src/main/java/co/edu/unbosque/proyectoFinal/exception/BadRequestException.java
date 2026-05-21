package co.edu.unbosque.proyectoFinal.exception;

/**
 * Excepción lanzada cuando la solicitud enviada por el cliente es inválida o
 * contiene errores de formato o de negocio.
 *
 * <p>Se usa en los servicios para señalar condiciones como:</p>
 * <ul>
 *   <li>Datos de entrada nulos o en blanco.</li>
 *   <li>Formato inválido de nombre, email, teléfono o contraseña.</li>
 *   <li>Nombre, email o teléfono ya registrados en el sistema.</li>
 *   <li>ID inválido (nulo o negativo) en operaciones de actualización o eliminación.</li>
 *   <li>Lista de resultados vacía al consultar todos los registros.</li>
 * </ul>
 *
 * <p>El {@link GlobalExceptionHandler} la traduce a una respuesta
 * {@code 400 Bad Request}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see GlobalExceptionHandler
 */
public class BadRequestException extends RuntimeException {

	/**
	 * Crea una nueva excepción de solicitud incorrecta con el mensaje especificado.
	 *
	 * @param mensaje descripción del error de validación o de negocio
	 */
	public BadRequestException(String mensaje) {
		super(mensaje);
	}
}
