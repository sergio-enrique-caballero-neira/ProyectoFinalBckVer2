package co.edu.unbosque.proyectoFinal.dto;

import java.util.Map;

/**
 * DTO para la transferencia de atributos de analisis de VirusTotal.
 * Contiene el estado del analisis, estadisticas y resultados por motor.
 */
public class AttributesDTO {

	private long id_Attributes;

	private String status;
	private StatsDTO stats;
	private Map<String, EngineResultDTO> results;

	/**
	 * Constructor vacio.
	 */
	public AttributesDTO() {
	}

	/**
	 * Constructor con estado, estadisticas y resultados.
	 * @param status estado del analisis (queued, completed, etc.)
	 * @param stats estadisticas de deteccion
	 * @param results resultados por motor de antivirus
	 */
	public AttributesDTO(String status, StatsDTO stats, Map<String, EngineResultDTO> results) {
		super();
		this.status = status;
		this.stats = stats;
		this.results = Map.copyOf(results);
	}

	/**
	 * Obtiene el estado del analisis.
	 * @return estado del analisis
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Establece el estado del analisis.
	 * @param status nuevo estado
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Obtiene las estadisticas de deteccion.
	 * @return estadisticas de deteccion
	 */
	public StatsDTO getStats() {
		return stats;
	}

	/**
	 * Establece las estadisticas de deteccion.
	 * @param stats nuevas estadisticas
	 */
	public void setStats(StatsDTO stats) {
		this.stats = stats;
	}

	/**
	 * Obtiene los resultados por motor de antivirus.
	 * @return mapa de resultados por motor
	 */
	public Map<String, EngineResultDTO> getResults() {
		return results;
	}

	/**
	 * Establece los resultados por motor de antivirus.
	 * @param results nuevo mapa de resultados
	 */
	public void setResults(Map<String, EngineResultDTO> results) {
		this.results = Map.copyOf(results);
	}

	/**
	 * Obtiene el identificador interno de los atributos.
	 * @return identificador interno
	 */
	public long getId_Attributes() {
		return id_Attributes;
	}

	/**
	 * Establece el identificador interno de los atributos.
	 * @param id_Attributes nuevo identificador
	 */
	public void setId_Attributes(long id_Attributes) {
		this.id_Attributes = id_Attributes;
	}

	@Override
	public String toString() {
		return "Attributes [status=" + status + ", stats=" + stats.toString() + ", results=" + results + "]";
	}

}
