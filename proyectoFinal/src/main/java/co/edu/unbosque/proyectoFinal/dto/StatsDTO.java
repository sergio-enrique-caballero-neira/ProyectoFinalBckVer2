package co.edu.unbosque.proyectoFinal.dto;

/**
 * DTO que representa las estadísticas globales de un análisis VirusTotal.
 *
 * <p>Contiene los contadores agregados de todos los motores antivirus que
 * analizaron el archivo. Cada contador indica cuántos motores clasificaron
 * el archivo en esa categoría:</p>
 * <ul>
 *   <li>{@code malicious}  – Motores que detectaron el archivo como malicioso.</li>
 *   <li>{@code suspicious} – Motores que marcaron el archivo como sospechoso.</li>
 *   <li>{@code undetected} – Motores que no detectaron amenaza.</li>
 *   <li>{@code harmless}   – Motores que confirmaron el archivo como inofensivo.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see AttributesDTO
 */
public class StatsDTO {

	/** Número de motores antivirus que detectaron el archivo como malicioso. */
	private int malicious;
	/** Número de motores que marcaron el archivo como sospechoso. */
	private int suspicious;
	/** Número de motores que no detectaron amenaza en el archivo. */
	private int undetected;
	/** Número de motores que confirmaron el archivo como inofensivo. */
	private int harmless;

	/**
	 * Constructor vacío requerido por Gson y frameworks de serialización.
	 */
	public StatsDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con todos los contadores de detección.
	 *
	 * @param malicious  cantidad de detecciones maliciosas
	 * @param suspicious cantidad de detecciones sospechosas
	 * @param undetected cantidad de motores sin detección
	 * @param harmless   cantidad de motores que confirmaron archivo inofensivo
	 */
	public StatsDTO(int malicious, int suspicious, int undetected, int harmless) {
		super();
		this.malicious = malicious;
		this.suspicious = suspicious;
		this.undetected = undetected;
		this.harmless = harmless;
	}

	/** @return número de detecciones maliciosas */
	public int getMalicious() { return malicious; }
	/** @param malicious cantidad de detecciones maliciosas */
	public void setMalicious(int malicious) { this.malicious = malicious; }

	/** @return número de detecciones sospechosas */
	public int getSuspicious() { return suspicious; }
	/** @param suspicious cantidad de detecciones sospechosas */
	public void setSuspicious(int suspicious) { this.suspicious = suspicious; }

	/** @return número de motores sin detección */
	public int getUndetected() { return undetected; }
	/** @param undetected cantidad de motores sin detección */
	public void setUndetected(int undetected) { this.undetected = undetected; }

	/** @return número de motores que confirmaron archivo inofensivo */
	public int getHarmless() { return harmless; }
	/** @param harmless cantidad de motores que confirmaron inofensivo */
	public void setHarmless(int harmless) { this.harmless = harmless; }

	/**
	 * Representación en cadena con todos los contadores.
	 * @return cadena con formato {@code "Stats [malicious=..., suspicious=..., undetected=..., harmless=...]"}
	 */
	@Override
	public String toString() {
		return "Stats [malicious=" + malicious + ", suspicious=" + suspicious + ", undetected=" + undetected
				+ ", harmless=" + harmless + "]";
	}
}
