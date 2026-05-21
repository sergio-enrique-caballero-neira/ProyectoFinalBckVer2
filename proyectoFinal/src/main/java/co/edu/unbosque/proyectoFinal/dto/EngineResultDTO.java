package co.edu.unbosque.proyectoFinal.dto;

/**
 * DTO para la transferencia de resultados de un motor de antivirus individual.
 * Contiene el nombre del motor, categoria de deteccion y resultado.
 */
public class EngineResultDTO {

	private long id_EngineResult;

	private String engine_name;
	private String category;
	private String result;

	/**
	 * Constructor vacio.
	 */
	public EngineResultDTO() {
	}

	/**
	 * Constructor con datos del motor.
	 * @param engine_name nombre del motor de antivirus
	 * @param categoria categoria de deteccion
	 * @param result resultado del analisis
	 */
	public EngineResultDTO(String engine_name, String category, String result) {
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
