package co.edu.unbosque.proyectoFinal.dto;

/**
 * DTO para la transferencia de respuestas de subida de archivos a VirusTotal.
 * Contiene los datos del analisis incluyendo ID, tipo y atributos.
 */
public class VirusTotalUploadResponseDTO {
	
	private long id_Response;
	
	private DataDTO data;
	
	/**
	 * Constructor vacio.
	 */
	public VirusTotalUploadResponseDTO() {
	}

	/**
	 * Constructor con datos del analisis.
	 * @param data datos de la respuesta de VirusTotal
	 */
	public VirusTotalUploadResponseDTO(DataDTO data) {
		super();
		this.data = data;
	}

	/**
	 * Obtiene los datos del analisis.
	 * @return datos de la respuesta
	 */
	public DataDTO getData() {
		return data;
	}

	/**
	 * Establece los datos del analisis.
	 * @param data nuevos datos de la respuesta
	 */
	public void setData(DataDTO data) {
		this.data = data;
	}

	/**
	 * Obtiene el identificador interno de la respuesta.
	 * @return identificador de la respuesta
	 */
	public long getId_Response() {
		return id_Response;
	}

	/**
	 * Establece el identificador interno de la respuesta.
	 * @param id_Response nuevo identificador
	 */
	public void setId_Response(long id_Response) {
		this.id_Response = id_Response;
	}

	@Override
	public String toString() {
		return data.toString();
	}
	
	
	
}
