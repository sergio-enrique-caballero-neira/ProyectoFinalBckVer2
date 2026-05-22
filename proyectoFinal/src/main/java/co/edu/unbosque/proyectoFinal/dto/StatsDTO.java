package co.edu.unbosque.proyectoFinal.dto;

/**
 * DTO para la transferencia de estadisticas de deteccion de VirusTotal.
 * Contiene el conteo de detecciones por categoria: malicious, suspicious, undetected, harmless.
 */
public class StatsDTO {

	/** Numero de motores que detectaron el archivo como malicioso. */
	private int malicious;
	/** Numero de motores que detectaron el archivo como sospechoso. */
	private int suspicious;
	/** Numero de motores que no detectaron nada. */
	private int undetected;
	/** Numero de motores que detectaron el archivo como inofensivo. */
	private int harmless;
	
	/**
	 * Constructor vacio.
	 */
	public StatsDTO() {
	}
	
	/**
	 * Constructor con todas las estadisticas.
	 * @param malicious numero de motores que detectaron como malicioso
	 * @param suspicious numero de motores que detectaron como sospechoso
	 * @param undetected numero de motores que no detectaron nada
	 * @param harmless numero de motores que detectaron como inofensivo
	 */
	public StatsDTO(int malicious, int suspicious, int undetected, int harmless) {
		super();
		this.malicious = malicious;
		this.suspicious = suspicious;
		this.undetected = undetected;
		this.harmless = harmless;
	}

	/**
	 * Obtiene el numero de detecciones maliciosas.
	 * @return conteo de malicious
	 */
	public int getMalicious() {
		return malicious;
	}

	/**
	 * Establece el numero de detecciones maliciosas.
	 * @param malicious nuevo conteo
	 */
	public void setMalicious(int malicious) {
		this.malicious = malicious;
	}

	/**
	 * Obtiene el numero de detecciones sospechosas.
	 * @return conteo de suspicious
	 */
	public int getSuspicious() {
		return suspicious;
	}

	/**
	 * Establece el numero de detecciones sospechosas.
	 * @param suspicious nuevo conteo
	 */
	public void setSuspicious(int suspicious) {
		this.suspicious = suspicious;
	}

	/**
	 * Obtiene el numero de no detecciones.
	 * @return conteo de undetected
	 */
	public int getUndetected() {
		return undetected;
	}

	/**
	 * Establece el numero de no detecciones.
	 * @param undetected nuevo conteo
	 */
	public void setUndetected(int undetected) {
		this.undetected = undetected;
	}

	/**
	 * Obtiene el numero de detecciones inofensivas.
	 * @return conteo de harmless
	 */
	public int getHarmless() {
		return harmless;
	}

	/**
	 * Establece el numero de detecciones inofensivas.
	 * @param harmless nuevo conteo
	 */
	public void setHarmless(int harmless) {
		this.harmless = harmless;
	}

	/**
	 * Representacion textual de las estadisticas de deteccion.
	 * @return cadena con los conteos de deteccion
	 */
	@Override
	public String toString() {
		return "Stats [malicious=" + malicious + ", suspicious=" + suspicious + ", undetected=" + undetected
				+ ", harmless=" + harmless + "]";
	}

}
