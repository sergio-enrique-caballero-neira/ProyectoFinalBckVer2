package co.edu.unbosque.proyectoFinal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Entidad JPA que representa el objeto {@code data} de la respuesta VirusTotal.
 *
 * <p>Almacena el identificador del análisis en VirusTotal ({@code id}), el tipo
 * del recurso, el nombre original del archivo enviado y los atributos completos
 * del análisis a través de una relación {@code @OneToOne} con {@link Attributes}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see VirusTotalUploadResponse
 * @see Attributes
 */
@Entity
public class Data {

	/** Identificador único generado automáticamente por la base de datos. */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long data_id;

	/**
	 * ID del análisis en VirusTotal. Se usa para consultar el resultado con
	 * {@code GET /virustotal/analisis?analysisId=...}.
	 */
	private String id;
	/** Tipo del recurso en la API de VirusTotal (p. ej. {@code "analysis"}). */
	private String type;
	/** Nombre original del archivo tal como fue enviado a VirusTotal. */
	private String nombreArchivo;

	/**
	 * Atributos del análisis: estado, estadísticas y resultados por motor.
	 * Cascada {@code ALL}: se persiste y elimina junto con este objeto.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private Attributes attributes;

	/** Constructor vacío requerido por JPA. */
	public Data() {}

	/**
	 * Constructor con tipo, atributos y nombre del archivo.
	 *
	 * @param type          tipo del recurso VirusTotal
	 * @param attributes    atributos del análisis
	 * @param nombreArchivo nombre original del archivo
	 */
	public Data(String type, Attributes attributes, String nombreArchivo) {
		super();
		this.type = type;
		this.attributes = attributes;
		this.nombreArchivo = nombreArchivo;
	}

	/** @return ID del análisis en VirusTotal */
	public String getId() { return id; }
	/** @param id ID del análisis a establecer */
	public void setId(String id) { this.id = id; }

	/** @return tipo del recurso en VirusTotal */
	public String getType() { return type; }
	/** @param type tipo del recurso a establecer */
	public void setType(String type) { this.type = type; }

	/** @return atributos del análisis */
	public Attributes getAttributes() { return attributes; }
	/** @param attributes atributos del análisis a establecer */
	public void setAttributes(Attributes attributes) { this.attributes = attributes; }

	/** @return nombre original del archivo analizado */
	public String getNombreArchivo() { return nombreArchivo; }
	/** @param nombreArchivo nombre del archivo a establecer */
	public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

	/** @return identificador interno en la base de datos */
	public Long getData_id() { return data_id; }
	/** @param data_id identificador interno a establecer */
	public void setData_id(Long data_id) { this.data_id = data_id; }

	/** @return representación textual con ID y tipo */
	@Override
	public String toString() {
		return "Data [id=" + id + ", type=" + type + ", attributes=" + "]";
	}
}
