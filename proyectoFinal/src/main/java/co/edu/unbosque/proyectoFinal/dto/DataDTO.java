package co.edu.unbosque.proyectoFinal.dto;


/**
 * DTO para la transferencia de datos de analisis de VirusTotal.
 * Contiene el ID del analisis, tipo, nombre del archivo y atributos.
 */
public class DataDTO {
	
	private long data_id;
	
	private String id;
	private String type;
	private String nombreArchivo;

	private AttributesDTO attributes;

	/**
	 * Constructor vacio.
	 */
	public DataDTO() {
	}

	/**
	 * Constructor con tipo y atributos.
	 * @param type tipo de analisis
	 * @param attributes atributos del analisis
	 */
	public DataDTO(String type, AttributesDTO attributes) {
		super();
		this.type = type;
		this.attributes = attributes;
	}

	/**
	 * Obtiene el identificador del analisis en VirusTotal.
	 * @return ID del analisis
	 */
	public String getId() {
		return id;
	}

	/**
	 * Establece el identificador del analisis.
	 * @param id nuevo ID del analisis
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Obtiene el tipo de analisis.
	 * @return tipo de analisis
	 */
	public String getType() {
		return type;
	}

	/**
	 * Establece el tipo de analisis.
	 * @param type nuevo tipo
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Obtiene los atributos del analisis.
	 * @return atributos del analisis
	 */
	public AttributesDTO getAttributes() {
		return attributes;
	}

	/**
	 * Establece los atributos del analisis.
	 * @param attributes nuevos atributos
	 */
	public void setAttributes(AttributesDTO attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * Obtiene el nombre original del archivo.
	 * @return nombre del archivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * Establece el nombre original del archivo.
	 * @param nombreArchivo nuevo nombre del archivo
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * Obtiene el identificador interno del objeto Data.
	 * @return identificador interno
	 */
	public long getData_id() {
		return data_id;
	}

	/**
	 * Establece el identificador interno del objeto Data.
	 * @param data_id nuevo identificador interno
	 */
	public void setData_id(long data_id) {
		this.data_id = data_id;
	}

	@Override
	public String toString() {
		return "Data [id=" + id + ", type=" + type + ", attributes=" + "]";
	}

}
