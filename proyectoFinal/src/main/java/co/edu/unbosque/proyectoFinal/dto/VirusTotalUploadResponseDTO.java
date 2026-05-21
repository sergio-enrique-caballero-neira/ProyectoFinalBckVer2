package co.edu.unbosque.proyectoFinal.dto;

/**
 * DTO raíz que representa la respuesta completa de la API de VirusTotal
 * para una operación de subida o consulta de análisis.
 *
 * <p>Modela la estructura JSON devuelta por los endpoints:
 * <ul>
 *   <li>{@code POST https://www.virustotal.com/api/v3/files} (subida de archivo)</li>
 *   <li>{@code GET  https://www.virustotal.com/api/v3/analyses/{id}} (consulta de análisis)</li>
 * </ul>
 *
 * <p>La estructura JSON tiene la forma:</p>
 * <pre>
 * {
 *   "data": {
 *     "id": "string",
 *     "type": "string",
 *     "attributes": { ... }
 *   }
 * }
 * </pre>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see DataDTO
 */
public class VirusTotalUploadResponseDTO {

	/** Identificador interno de la respuesta en la base de datos local. */
	private long id_Response;

	/** Objeto {@code data} de la respuesta JSON de VirusTotal. */
	private DataDTO data;

	/**
	 * Constructor vacío requerido por Gson y frameworks de serialización.
	 */
	public VirusTotalUploadResponseDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con el objeto data de la respuesta.
	 *
	 * @param data objeto principal de la respuesta VirusTotal
	 */
	public VirusTotalUploadResponseDTO(DataDTO data) {
		super();
		this.data = data;
	}

	/**
	 * Retorna el objeto {@code data} de la respuesta VirusTotal.
	 * @return objeto {@link DataDTO} con el ID del análisis y sus atributos
	 */
	public DataDTO getData() { return data; }

	/**
	 * Establece el objeto {@code data} de la respuesta VirusTotal.
	 * @param data nuevo objeto data
	 */
	public void setData(DataDTO data) { this.data = data; }

	/**
	 * Retorna el identificador interno de la respuesta en la base de datos local.
	 * @return ID de la respuesta
	 */
	public long getId_Response() { return id_Response; }

	/**
	 * Establece el identificador interno de la respuesta.
	 * @param id_Response ID a asignar
	 */
	public void setId_Response(long id_Response) { this.id_Response = id_Response; }

	/**
	 * Representación en cadena de la respuesta, delegando al objeto {@code data}.
	 * @return representación textual del campo {@code data}
	 */
	@Override
	public String toString() { return data.toString(); }
}
