package co.edu.unbosque.proyectoFinal.entity;

import jakarta.persistence.Embeddable;

/**
 * Clase embebible JPA que representa las estadísticas globales de un análisis
 * VirusTotal.
 *
 * <p>Anotada con {@code @Embeddable}, sus columnas se incluyen directamente
 * en la tabla de {@link Attributes} sin generar una tabla propia. Contiene
 * los contadores de detección de todos los motores antivirus que analizaron
 * el archivo.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see Attributes
 */
@Embeddable
public class Stats {

	/** Número de motores que detectaron el archivo como malicioso. */
	private int malicious;
	/** Número de motores que marcaron el archivo como sospechoso. */
	private int suspicious;
	/** Número de motores que no detectaron amenaza. */
	private int undetected;
	/** Número de motores que confirmaron el archivo como inofensivo. */
	private int harmless;

	/** Constructor vacío requerido por JPA. */
	public Stats() {}

	/**
	 * Constructor con todos los contadores de detección.
	 *
	 * @param malicious  detecciones maliciosas
	 * @param suspicious detecciones sospechosas
	 * @param undetected motores sin detección
	 * @param harmless   motores que confirmaron inofensivo
	 */
	public Stats(int malicious, int suspicious, int undetected, int harmless) {
		super();
		this.malicious = malicious;
		this.suspicious = suspicious;
		this.undetected = undetected;
		this.harmless = harmless;
	}

	/** @return detecciones maliciosas */
	public int getMalicious() { return malicious; }
	/** @param malicious cantidad a establecer */
	public void setMalicious(int malicious) { this.malicious = malicious; }

	/** @return detecciones sospechosas */
	public int getSuspicious() { return suspicious; }
	/** @param suspicious cantidad a establecer */
	public void setSuspicious(int suspicious) { this.suspicious = suspicious; }

	/** @return motores sin detección */
	public int getUndetected() { return undetected; }
	/** @param undetected cantidad a establecer */
	public void setUndetected(int undetected) { this.undetected = undetected; }

	/** @return motores que confirmaron inofensivo */
	public int getHarmless() { return harmless; }
	/** @param harmless cantidad a establecer */
	public void setHarmless(int harmless) { this.harmless = harmless; }

	/** @return representación textual de todas las estadísticas */
	@Override
	public String toString() {
		return "Stats [malicious=" + malicious + ", suspicious=" + suspicious + ", undetected=" + undetected
				+ ", harmless=" + harmless + "]";
	}
}
