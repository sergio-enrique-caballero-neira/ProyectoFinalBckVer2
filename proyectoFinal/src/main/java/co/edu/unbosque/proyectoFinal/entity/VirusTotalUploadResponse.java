package co.edu.unbosque.proyectoFinal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Entidad JPA que representa la respuesta de subida de un archivo a VirusTotal.
 * Se almacena como parte del historial de analisis de un usuario.
 */
@Entity
public class VirusTotalUploadResponse {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id_Response;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Data data;
	
	/**
	 * Constructor vacio.
	 */
	public VirusTotalUploadResponse() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con datos del analisis.
	 * @param data datos de la respuesta de VirusTotal
	 */
	public VirusTotalUploadResponse(Data data) {
		super();
		this.data = data;
	}

	/**
	 * Obtiene los datos del analisis.
	 * @return datos de la respuesta
	 */
	public Data getData() {
		return data;
	}

	/**
	 * Establece los datos del analisis.
	 * @param data nuevos datos de la respuesta
	 */
	public void setData(Data data) {
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
