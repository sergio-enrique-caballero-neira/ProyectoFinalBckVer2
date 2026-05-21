package co.edu.unbosque.proyectoFinal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Entidad JPA que representa los datos de un analisis de VirusTotal.
 * Contiene el ID del analisis, tipo, nombre del archivo y atributos asociados.
 */
@Entity
public class Data {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long data_id;
	
	private String id;
	private String type;
	private String nombreArchivo;

	@OneToOne(cascade = CascadeType.ALL)
	private Attributes attributes;

	/**
	 * Constructor vacio.
	 */
	public Data() {
	}

	/**
	 * Constructor con tipo, atributos y nombre del archivo.
	 * @param type tipo de analisis
	 * @param attributes atributos del analisis
	 * @param nombreArchivo nombre original del archivo
	 */
	public Data(String type, Attributes attributes, String nombreArchivo) {
		super();
		this.type = type;
		this.attributes = attributes;
		this.nombreArchivo = nombreArchivo;
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
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * Establece los atributos del analisis.
	 * @param attributes nuevos atributos
	 */
	public void setAttributes(Attributes attributes) {
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
	public Long getData_id() {
		return data_id;
	}

	/**
	 * Establece el identificador interno del objeto Data.
	 * @param data_id nuevo identificador interno
	 */
	public void setData_id(Long data_id) {
		this.data_id = data_id;
	}

	@Override
	public String toString() {
		return "Data [id=" + id + ", type=" + type + ", attributes=" + "]";
	}

}
