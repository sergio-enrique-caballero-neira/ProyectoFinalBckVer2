package co.edu.unbosque.proyectoFinal.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * DTO para la transferencia de datos de usuarios regulares.
 * Extiende PersonaDTO y agrega el historial de analisis de VirusTotal.
 */
public class UsuarioDTO extends PersonaDTO {
	/**id*/
	private static final long serialVersionUID = 1L;
	
	/** Historial de analisis de VirusTotal del usuario. */
	private List<VirusTotalUploadResponseDTO> historial;

	/**
	 * Constructor vacio. Establece el rol como USUARIO.
	 */
	public UsuarioDTO() {
		this.setRole(PersonaDTO.Role.USUARIO);
	}

	/**
	 * Constructor con historial de analisis.
	 * @param historial lista de respuestas de VirusTotal
	 */
	public UsuarioDTO(List<VirusTotalUploadResponseDTO> historial) {
		super();
		this.historial = List.copyOf(historial);
		this.setRole(PersonaDTO.Role.USUARIO);
	}

	/**
	 * Constructor completo con todos los datos del usuario.
	 * @param nombre nombre de usuario
	 * @param email correo electronico
	 * @param telefono numero de telefono
	 * @param contrasena contraseña
	 * @param historial lista de respuestas de VirusTotal
	 */
	public UsuarioDTO(String nombre, String email, String telefono, String contrasena,
			List<VirusTotalUploadResponseDTO> historial) {
		super(nombre, email, telefono, contrasena);
		this.historial = List.copyOf(historial);
		this.setRole(PersonaDTO.Role.USUARIO);
	}

	/**
	 * Constructor con datos basicos sin historial.
	 * @param nombre nombre de usuario
	 * @param email correo electronico
	 * @param telefono numero de telefono
	 * @param contrasena contraseña
	 */
	public UsuarioDTO(String nombre, String email, String telefono, String contrasena) {
		super(nombre, email, telefono, contrasena);
		this.setRole(PersonaDTO.Role.USUARIO);
	}

	/**
	 * Obtiene el historial de analisis de VirusTotal.
	 * @return lista de respuestas de VirusTotal
	 */
	public List<VirusTotalUploadResponseDTO> getHistorial() {
		return historial;
	}

	/**
	 * Establece el historial de analisis de VirusTotal.
	 * @param historial nueva lista de respuestas
	 */
	public void setHistorial(List<VirusTotalUploadResponseDTO> historial) {
		this.historial = List.copyOf(historial);
	}
	
	/**
	 * Obtiene las autoridades (roles) del usuario para Spring Security.
	 * @return coleccion con el rol del usuario prefijado con ROLE_
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
	}

	/**
	 * Obtiene la contraseña del usuario para autenticacion.
	 * @return contraseña del usuario
	 */
	@Override
	public String getPassword() {
		return super.getContrasena();
	}

	/**
	 * Obtiene el nombre de usuario para autenticacion.
	 * @return nombre del usuario
	 */
	@Override
	public String getUsername() {
		return super.getNombre();
	}
}
