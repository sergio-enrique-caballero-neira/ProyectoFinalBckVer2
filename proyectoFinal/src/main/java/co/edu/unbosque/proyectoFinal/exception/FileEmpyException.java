package co.edu.unbosque.proyectoFinal.exception;

/**
 * Excepcion lanzada cuando se intenta subir un archivo vacio a VirusTotal.
 */
public class FileEmpyException extends Exception {
	
    /**
     * Crea una nueva excepcion de archivo vacio con el mensaje especificado.
     * @param mensaje mensaje de error
     */
    public FileEmpyException(String mensaje) {
		super(mensaje);
	}
	
}
