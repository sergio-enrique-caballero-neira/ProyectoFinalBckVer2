package co.edu.unbosque.proyectoFinal.entity;

import jakarta.persistence.Embeddable;

/**
 * Clase embebida JPA que representa las estadisticas de deteccion de VirusTotal.
 * Contiene el conteo de detecciones por categoria: malicious, suspicious, undetected, harmless.
 */
@Embeddable
public class Stats {

	private int malicious;
	private int suspicious;
	private int undetected;
	private int harmless;
	
	/**
	 * Constructor vacio.
	 */
	public Stats() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor con todas las estadisticas.
	 * @param malicious numero de motores que detectaron como malicioso
	 * @param suspicious numero de motores que detectaron como sospechoso
	 * @param undetected numero de motores que no detectaron nada
	 * @param harmless numero de motores que detectaron como inofensivo
	 */
	public Stats(int malicious, int suspicious, int undetected, int harmless) {
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

	@Override
	public String toString() {
		return "Stats [malicious=" + malicious + ", suspicious=" + suspicious + ", undetected=" + undetected
				+ ", harmless=" + harmless + "]";
	}

}
