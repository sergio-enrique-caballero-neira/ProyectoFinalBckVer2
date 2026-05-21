package co.edu.unbosque.proyectoFinal.dto;

/**
 * DTO que representa el objeto {@code data} dentro de la respuesta de VirusTotal.
 *
 * <p>Contiene el identificador único del análisis ({@code id}), el tipo de
 * recurso ({@code type}), el nombre original del archivo analizado y los
 * atributos completos del análisis ({@link AttributesDTO}).</p>
 *
 * <p>El campo {@code id} retornado al subir un archivo es el {@code analysisId}
 * que debe usarse para consultar los resultados en
 * {@code GET /virustotal/analisis}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see VirusTotalUploadResponseDTO
 * @see AttributesDTO
 */
public class DataDTO {

	/** Identificador interno de este objeto en la base de datos local. */
	private long data_id;

	/**
	 * ID del análisis en VirusTotal. Después de subir un archivo,
	 * este valor debe usarse para consultar el resultado.
	 */
	private String id;

	/** Tipo de recurso en la API de VirusTotal (p. ej. {@code "analysis"}). */
	private String type;

	/** Nombre original del archivo tal como fue enviado a VirusTotal. */
	private String nombreArchivo;

	/** Atributos del análisis: estado, estadísticas y resultados por motor. */
	private AttributesDTO attributes;

	/**
	 * Constructor vacío requerido por Gson y frameworks de serialización.
	 */
	public DataDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con tipo y atributos del análisis.
	 *
	 * @param type       tipo del recurso en VirusTotal
	 * @param attributes atributos del análisis
	 */
	public DataDTO(String type, AttributesDTO attributes) {
		super();
		this.type = type;
		this.attributes = attributes;
	}

	/** @return ID del análisis en VirusTotal */
	public String getId() { return id; }
	/** @param id ID del análisis a establecer */
	public void setId(String id) { this.id = id; }

	/** @return tipo de recurso en la API de VirusTotal */
	public String getType() { return type; }
	/** @param type tipo de recurso a establecer */
	public void setType(String type) { this.type = type; }

	/** @return atributos del análisis (estado, estadísticas, resultados) */
	public AttributesDTO getAttributes() { return attributes; }
	/** @param attributes atributos del análisis a establecer */
	public void setAttributes(AttributesDTO attributes) { this.attributes = attributes; }

	/** @return nombre original del archivo analizado */
	public String getNombreArchivo() { return nombreArchivo; }
	/** @param nombreArchivo nombre del archivo a establecer */
	public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

	/** @return identificador interno en la base de datos local */
	public long getData_id() { return data_id; }
	/** @param data_id identificador interno a establecer */
	public void setData_id(long data_id) { this.data_id = data_id; }

	/**
	 * Representación en cadena con el ID y tipo del objeto data.
	 * @return cadena con formato {@code "Data [id=..., type=..., attributes=]"}
	 */
	@Override
	public String toString() {
		return "Data [id=" + id + ", type=" + type + ", attributes=" + "]";
	}
}
