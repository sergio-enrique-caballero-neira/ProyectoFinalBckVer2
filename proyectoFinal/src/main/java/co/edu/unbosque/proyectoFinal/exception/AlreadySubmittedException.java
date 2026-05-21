package co.edu.unbosque.proyectoFinal.exception;

/**
 * Excepcion lanzada cuando un archivo ya fue enviado previamente a VirusTotal para escaneo.
 */
public class AlreadySubmittedException extends Exception{
	
	/**
	 * Crea una nueva excepcion de archivo ya enviado con el mensaje especificado.
	 * @param mensaje mensaje de error
	 */
	public AlreadySubmittedException(String mensaje) {
		super(mensaje);
	}
	
}
