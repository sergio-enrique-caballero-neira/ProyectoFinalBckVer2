package co.edu.unbosque.proyectoFinal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad JPA que representa el resultado individual de un motor antivirus
 * dentro de un análisis VirusTotal.
 *
 * <p>Cada instancia corresponde al veredicto de un único motor antivirus sobre
 * el archivo analizado. Las instancias se relacionan con {@link Attributes}
 * mediante un mapa {@code Map<String, EngineResult>}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see Attributes
 */
@Entity
public class EngineResult {

	/** Identificador único generado automáticamente por la base de datos. */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id_EngineResult;

	/** Nombre del motor antivirus (p. ej. {@code "Kaspersky"}, {@code "Avast"}). */
	private String engine_name;
	/** Categoría del veredicto: {@code "malicious"}, {@code "undetected"}, {@code "harmless"}, {@code "suspicious"}. */
	private String category;
	/** Nombre de la amenaza detectada, o {@code null} si no se encontró amenaza. */
	private String result;

	/** Constructor vacío requerido por JPA. */
	public EngineResult() {}

	/**
	 * Constructor con todos los campos del resultado del motor.
	 *
	 * @param engine_name nombre del motor antivirus
	 * @param category    categoría del veredicto
	 * @param result      nombre de la amenaza o {@code null}
	 */
	public EngineResult(String engine_name, String category, String result) {
		super();
		this.engine_name = engine_name;
		this.category = category;
		this.result = result;
	}

	/** @return identificador interno en la base de datos */
	public long getId_EngineResult() { return id_EngineResult; }
	/** @param id_EngineResult identificador a establecer */
	public void setId_EngineResult(long id_EngineResult) { this.id_EngineResult = id_EngineResult; }

	/** @return nombre del motor antivirus */
	public String getEngine_name() { return engine_name; }
	/** @param engine_name nombre del motor a establecer */
	public void setEngine_name(String engine_name) { this.engine_name = engine_name; }

	/** @return categoría del veredicto */
	public String getCategory() { return category; }
	/** @param category categoría a establecer */
	public void setCategory(String category) { this.category = category; }

	/** @return nombre de la amenaza detectada o {@code null} */
	public String getResult() { return result; }
	/** @param result nombre de la amenaza a establecer */
	public void setResult(String result) { this.result = result; }
}
