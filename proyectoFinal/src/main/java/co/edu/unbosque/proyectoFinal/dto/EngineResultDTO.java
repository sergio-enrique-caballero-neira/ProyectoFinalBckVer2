package co.edu.unbosque.proyectoFinal.dto;

/**
 * DTO que representa el resultado individual de un motor antivirus en un análisis VirusTotal.
 *
 * <p>Cada motor antivirus devuelve tres datos clave sobre el archivo analizado:</p>
 * <ul>
 *   <li>{@code engine_name} – Nombre del motor antivirus (p. ej. {@code "Kaspersky"},
 *       {@code "Windows Defender"}).</li>
 *   <li>{@code category}    – Categoría del veredicto ({@code "malicious"},
 *       {@code "undetected"}, {@code "harmless"}, {@code "suspicious"}).</li>
 *   <li>{@code result}      – Nombre de la amenaza detectada, o {@code null}
 *       si el motor no encontró amenaza.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see AttributesDTO
 */
public class EngineResultDTO {

	/** Identificador interno en la base de datos local. */
	private long id_EngineResult;

	/** Nombre del motor antivirus que realizó el análisis. */
	private String engine_name;
	/** Categoría del veredicto del motor ({@code "malicious"}, {@code "undetected"}, etc.). */
	private String category;
	/** Nombre de la amenaza detectada, o {@code null} si el archivo es seguro. */
	private String result;

	/**
	 * Constructor vacío requerido por Gson y frameworks de serialización.
	 */
	public EngineResultDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con todos los campos del resultado del motor.
	 *
	 * @param engine_name nombre del motor antivirus
	 * @param category    categoría del veredicto
	 * @param result      nombre de la amenaza detectada o {@code null}
	 */
	public EngineResultDTO(String engine_name, String category, String result) {
		super();
		this.engine_name = engine_name;
		this.category = category;
		this.result = result;
	}

	/** @return identificador interno en la base de datos local */
	public long getId_EngineResult() { return id_EngineResult; }
	/** @param id_EngineResult identificador interno a establecer */
	public void setId_EngineResult(long id_EngineResult) { this.id_EngineResult = id_EngineResult; }

	/** @return nombre del motor antivirus */
	public String getEngine_name() { return engine_name; }
	/** @param engine_name nombre del motor a establecer */
	public void setEngine_name(String engine_name) { this.engine_name = engine_name; }

	/** @return categoría del veredicto */
	public String getCategory() { return category; }
	/** @param category categoría del veredicto a establecer */
	public void setCategory(String category) { this.category = category; }

	/** @return nombre de la amenaza detectada o {@code null} */
	public String getResult() { return result; }
	/** @param result nombre de la amenaza a establecer */
	public void setResult(String result) { this.result = result; }
}
