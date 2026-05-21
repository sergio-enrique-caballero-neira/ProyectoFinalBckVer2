package co.edu.unbosque.proyectoFinal.entity;

import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Entidad JPA que almacena los atributos de un análisis VirusTotal.
 *
 * <p>Contiene el estado del análisis, las estadísticas embebidas de detección
 * ({@link Stats}) y el mapa de resultados por motor antivirus
 * ({@code Map<String, EngineResult>}).</p>
 *
 * <p>Las estadísticas ({@link Stats}) se almacenan como objeto embebido
 * ({@code @Embedded}) en las mismas columnas de esta tabla. Los resultados
 * por motor ({@link EngineResult}) se relacionan con cascada completa
 * ({@code @OneToMany(cascade = CascadeType.ALL)}).</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see Data
 * @see Stats
 * @see EngineResult
 */
@Entity
public class Attributes {

	/** Identificador único generado automáticamente por la base de datos. */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id_Attributes;

	/** Estado del análisis: {@code "queued"}, {@code "in-progress"} o {@code "completed"}. */
	private String status;

	/** Estadísticas globales de detección, embebidas en esta misma tabla. */
	@Embedded
	private Stats stats;

	/**
	 * Mapa de resultados individuales por motor antivirus.
	 * Clave: nombre del motor. Valor: resultado del motor.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private Map<String, EngineResult> results;

	/** Constructor vacío requerido por JPA. */
	public Attributes() {}

	/**
	 * Constructor con todos los atributos del análisis.
	 *
	 * @param status  estado del análisis
	 * @param stats   estadísticas de detección
	 * @param results mapa de resultados por motor
	 */
	public Attributes(String status, Stats stats, Map<String, EngineResult> results) {
		super();
		this.status = status;
		this.stats = stats;
		this.results = Map.copyOf(results);
	}

	/** @return estado del análisis */
	public String getStatus() { return status; }
	/** @param status nuevo estado */
	public void setStatus(String status) { this.status = status; }

	/** @return estadísticas de detección */
	public Stats getStats() { return stats; }
	/** @param stats nuevas estadísticas */
	public void setStats(Stats stats) { this.stats = stats; }

	/** @return mapa inmutable de resultados por motor */
	public Map<String, EngineResult> getResults() { return results; }
	/** @param results nuevo mapa de resultados (se copia como inmutable) */
	public void setResults(Map<String, EngineResult> results) { this.results = Map.copyOf(results); }

	/** @return identificador interno en la base de datos */
	public long getId_Attributes() { return id_Attributes; }
	/** @param id_Attributes identificador a asignar */
	public void setId_Attributes(long id_Attributes) { this.id_Attributes = id_Attributes; }

	/** @return representación textual con estado, estadísticas y resultados */
	@Override
	public String toString() {
		return "Attributes [status=" + status + ", stats=" + stats.toString() + ", results=" + results + "]";
	}
}
