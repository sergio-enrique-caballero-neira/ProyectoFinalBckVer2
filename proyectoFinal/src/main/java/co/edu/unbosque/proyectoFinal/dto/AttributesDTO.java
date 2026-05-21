package co.edu.unbosque.proyectoFinal.dto;

import java.util.Map;

/**
 * DTO que representa los atributos de un análisis VirusTotal.
 *
 * <p>Mapea el campo {@code attributes} del objeto {@code data} en la respuesta
 * JSON de VirusTotal. Contiene:</p>
 * <ul>
 *   <li>{@code status} – Estado actual del análisis ({@code "queued"},
 *       {@code "in-progress"} o {@code "completed"}).</li>
 *   <li>{@code stats} – Contadores agregados de los resultados de todos
 *       los motores antivirus.</li>
 *   <li>{@code results} – Mapa de resultados individuales por nombre de motor
 *       antivirus, donde la clave es el nombre del motor y el valor es
 *       el resultado de ese motor.</li>
 * </ul>
 *
 * <p>El mapa {@code results} se almacena como copia inmutable para evitar
 * modificaciones externas no controladas.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see DataDTO
 * @see StatsDTO
 * @see EngineResultDTO
 */
public class AttributesDTO {

	/** Identificador interno en la base de datos local. */
	private long id_Attributes;

	/**
	 * Estado del análisis en VirusTotal.
	 * Valores posibles: {@code "queued"}, {@code "in-progress"}, {@code "completed"}.
	 */
	private String status;

	/** Estadísticas globales de la detección (conteo por categoría). */
	private StatsDTO stats;

	/**
	 * Mapa de resultados por motor antivirus.
	 * La clave es el nombre del motor (p. ej. {@code "Kaspersky"}) y el valor
	 * es el resultado de ese motor.
	 */
	private Map<String, EngineResultDTO> results;

	/**
	 * Constructor vacío requerido por Gson y frameworks de serialización.
	 */
	public AttributesDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con todos los atributos del análisis.
	 *
	 * @param status  estado actual del análisis
	 * @param stats   estadísticas globales de detección
	 * @param results mapa de resultados por motor antivirus
	 */
	public AttributesDTO(String status, StatsDTO stats, Map<String, EngineResultDTO> results) {
		super();
		this.status = status;
		this.stats = stats;
		this.results = Map.copyOf(results);
	}

	/** @return estado del análisis */
	public String getStatus() { return status; }
	/** @param status nuevo estado del análisis */
	public void setStatus(String status) { this.status = status; }

	/** @return estadísticas globales de detección */
	public StatsDTO getStats() { return stats; }
	/** @param stats nuevas estadísticas de detección */
	public void setStats(StatsDTO stats) { this.stats = stats; }

	/** @return mapa inmutable de resultados por motor antivirus */
	public Map<String, EngineResultDTO> getResults() { return results; }
	/**
	 * Reemplaza el mapa de resultados con una copia inmutable.
	 * @param results nuevo mapa de resultados
	 */
	public void setResults(Map<String, EngineResultDTO> results) { this.results = Map.copyOf(results); }

	/** @return identificador interno en la base de datos local */
	public long getId_Attributes() { return id_Attributes; }
	/** @param id_Attributes identificador interno a establecer */
	public void setId_Attributes(long id_Attributes) { this.id_Attributes = id_Attributes; }

	/**
	 * Representación en cadena con estado, estadísticas y resultados.
	 * @return cadena con formato {@code "Attributes [status=..., stats=..., results=...]"}
	 */
	@Override
	public String toString() {
		return "Attributes [status=" + status + ", stats=" + stats.toString() + ", results=" + results + "]";
	}
}
