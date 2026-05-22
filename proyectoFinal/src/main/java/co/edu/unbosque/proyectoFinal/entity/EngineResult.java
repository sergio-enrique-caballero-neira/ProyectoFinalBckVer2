package co.edu.unbosque.proyectoFinal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad JPA que representa el resultado de un motor de antivirus individual.
 * Contiene el nombre del motor, categoria de deteccion y resultado.
 */
@Entity
public class EngineResult {

	/** Identificador interno del resultado del motor. */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id_EngineResult;

	/** Nombre del motor de antivirus. */
	private String engine_name;
	/** Categoria de deteccion (malicious, harmless, etc.). */
	private String category;
	/** Resultado del analisis del motor. */
	private String result;

	/**
	 * Constructor vacio.
	 */
	public EngineResult() {
	}

	/**
	 * Constructor con datos del motor.
	 * @param engine_name nombre del motor de antivirus
	 * @param category categoria de deteccion
	 * @param result resultado del analisis
	 */
	public EngineResult(String engine_name, String category, String result) {
		super();
		this.engine_name = engine_name;
		this.category = category;
		this.result = result;
	}

	/**
	 * Obtiene el identificador interno del resultado.
	 * @return identificador interno
	 */
	public long getId_EngineResult() {
		return id_EngineResult;
	}

	/**
	 * Establece el identificador interno del resultado.
	 * @param id_EngineResult nuevo identificador
	 */
	public void setId_EngineResult(long id_EngineResult) {
		this.id_EngineResult = id_EngineResult;
	}

	/**
	 * Obtiene el nombre del motor de antivirus.
	 * @return nombre del motor
	 */
	public String getEngine_name() {
		return engine_name;
	}

	/**
	 * Establece el nombre del motor de antivirus.
	 * @param engine_name nuevo nombre
	 */
	public void setEngine_name(String engine_name) {
		this.engine_name = engine_name;
	}

	/**
	 * Obtiene la categoria de deteccion.
	 * @return categoria
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Establece la categoria de deteccion.
	 * @param category nueva categoria
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Obtiene el resultado del analisis del motor.
	 * @return resultado
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Establece el resultado del analisis del motor.
	 * @param result nuevo resultado
	 */
	public void setResult(String result) {
		this.result = result;
	}

}
