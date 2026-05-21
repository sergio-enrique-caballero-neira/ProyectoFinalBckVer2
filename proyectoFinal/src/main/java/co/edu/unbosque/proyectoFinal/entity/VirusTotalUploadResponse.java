package co.edu.unbosque.proyectoFinal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Entidad JPA que representa la respuesta raíz de VirusTotal asociada a un
 * análisis de archivo realizado por un usuario.
 *
 * <p>Cada instancia corresponde a una entrada en el historial de análisis de
 * un {@link Usuario}. Contiene una relación {@code @OneToOne} con cascada
 * completa hacia {@link Data}, que almacena los detalles del análisis.</p>
 *
 * <p>Esta entidad modela el nivel superior de la respuesta JSON de VirusTotal:</p>
 * <pre>
 * {
 *   "data": { ... }
 * }
 * </pre>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see Data
 * @see Usuario
 */
@Entity
public class VirusTotalUploadResponse {

	/** Identificador único generado automáticamente por la base de datos. */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id_Response;

	/**
	 * Datos del análisis. La cascada {@code ALL} garantiza que el objeto
	 * {@code Data} se persiste y elimina junto con esta entidad.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private Data data;

	/**
	 * Constructor vacío requerido por JPA.
	 */
	public VirusTotalUploadResponse() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con el objeto data del análisis.
	 * @param data datos del análisis VirusTotal
	 */
	public VirusTotalUploadResponse(Data data) {
		super();
		this.data = data;
	}

	/** @return datos del análisis */
	public Data getData() { return data; }
	/** @param data nuevos datos del análisis */
	public void setData(Data data) { this.data = data; }

	/** @return identificador único en la base de datos */
	public long getId_Response() { return id_Response; }
	/** @param id_Response identificador a asignar */
	public void setId_Response(long id_Response) { this.id_Response = id_Response; }

	/** @return representación textual delegando al objeto {@code data} */
	@Override
	public String toString() { return data.toString(); }
}
