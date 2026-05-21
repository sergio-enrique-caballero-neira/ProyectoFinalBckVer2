package co.edu.unbosque.proyectoFinal.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * DTO (Data Transfer Object) para la entidad {@code Usuario}.
 *
 * <p>Extiende {@link PersonaDTO} añadiendo el historial de análisis VirusTotal
 * del usuario ({@code List<VirusTotalUploadResponseDTO>}). Implementa los métodos
 * de {@code UserDetails} para participar en el flujo de autenticación JWT.</p>
 *
 * <p>El rol se establece automáticamente como {@link PersonaDTO.Role#USUARIO}
 * en todos los constructores, y la autoridad de Spring Security resultante es
 * {@code "ROLE_USUARIO"}.</p>
 *
 * <p>Las listas del historial se almacenan como copias inmutables mediante
 * {@code List.copyOf()} para garantizar la integridad de los datos transferidos.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see PersonaDTO
 * @see VirusTotalUploadResponseDTO
 * @see co.edu.unbosque.proyectoFinal.entity.Usuario
 */
public class UsuarioDTO extends PersonaDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * Historial de análisis VirusTotal realizados por el usuario.
	 * Cada elemento representa un archivo analizado con su resultado.
	 */
	private List<VirusTotalUploadResponseDTO> historial;

	/**
	 * Constructor vacío. Establece el rol como {@code USUARIO} automáticamente.
	 */
	public UsuarioDTO() {
		this.setRole(PersonaDTO.Role.USUARIO);
	}

	/**
	 * Constructor con solo el historial de análisis.
	 *
	 * @param historial lista de respuestas de análisis VirusTotal
	 */
	public UsuarioDTO(List<VirusTotalUploadResponseDTO> historial) {
		super();
		this.historial = List.copyOf(historial);
		this.setRole(PersonaDTO.Role.USUARIO);
	}

	/**
	 * Constructor completo con todos los campos del usuario.
	 *
	 * @param nombre     nombre único del usuario
	 * @param email      correo electrónico
	 * @param telefono   número de teléfono
	 * @param contrasena contraseña (se cifrará antes de persistir)
	 * @param historial  historial de análisis VirusTotal del usuario
	 */
	public UsuarioDTO(String nombre, String email, String telefono, String contrasena,
			List<VirusTotalUploadResponseDTO> historial) {
		super(nombre, email, telefono, contrasena);
		this.historial = List.copyOf(historial);
		this.setRole(PersonaDTO.Role.USUARIO);
	}

	/**
	 * Constructor sin historial, para creación inicial de usuarios.
	 *
	 * @param nombre     nombre único del usuario
	 * @param email      correo electrónico
	 * @param telefono   número de teléfono
	 * @param contrasena contraseña en texto plano
	 */
	public UsuarioDTO(String nombre, String email, String telefono, String contrasena) {
		super(nombre, email, telefono, contrasena);
		this.setRole(PersonaDTO.Role.USUARIO);
	}

	/**
	 * Retorna el historial de análisis VirusTotal del usuario.
	 * @return lista inmutable de respuestas de análisis
	 */
	public List<VirusTotalUploadResponseDTO> getHistorial() { return historial; }

	/**
	 * Reemplaza el historial de análisis del usuario con una copia inmutable de la lista.
	 * @param historial nueva lista de respuestas de análisis VirusTotal
	 */
	public void setHistorial(List<VirusTotalUploadResponseDTO> historial) {
		this.historial = List.copyOf(historial);
	}

	/**
	 * Retorna las autoridades de Spring Security del usuario.
	 * La única autoridad es {@code "ROLE_USUARIO"}.
	 *
	 * @return colección con la autoridad {@code ROLE_USUARIO}
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
	}

	/**
	 * Retorna la contraseña requerida por {@code UserDetails}.
	 * Delega a {@link PersonaDTO#getContrasena()}.
	 *
	 * @return contraseña del usuario
	 */
	@Override
	public String getPassword() { return super.getContrasena(); }

	/**
	 * Retorna el nombre de usuario requerido por {@code UserDetails}.
	 * Delega a {@link PersonaDTO#getNombre()}.
	 *
	 * @return nombre del usuario
	 */
	@Override
	public String getUsername() { return super.getNombre(); }
}
