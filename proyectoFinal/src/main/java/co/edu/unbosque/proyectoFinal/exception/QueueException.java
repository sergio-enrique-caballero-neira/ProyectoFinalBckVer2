package co.edu.unbosque.proyectoFinal.exception;

/**
 * Excepcion lanzada cuando un analisis de VirusTotal aun esta en cola de procesamiento.
 */
public class QueueException extends Exception {
	
	/**
	 * Crea una nueva excepcion de cola con el mensaje especificado.
	 * @param mensaje mensaje de error
	 */
	public QueueException(String mensaje) {
		super(mensaje);
	}

}
