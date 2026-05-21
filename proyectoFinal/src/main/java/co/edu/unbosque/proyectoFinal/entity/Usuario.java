package co.edu.unbosque.proyectoFinal.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa a un usuario normal del sistema.
 *
 * <p>Extiende {@link Persona} añadiendo el historial de análisis VirusTotal,
 * y se persiste en la tabla {@code usuarios}. La relación con el historial
 * es {@code @OneToMany} con cascada completa ({@code CascadeType.ALL}), lo
 * que significa que al eliminar un usuario se eliminan también todos sus
 * registros de análisis.</p>
 *
 * <p>El rol se establece automáticamente como {@link Persona.Role#USUARIO}
 * en todos los constructores. La autoridad resultante para Spring Security
 * es {@code "ROLE_USUARIO"}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see Persona
 * @see VirusTotalUploadResponse
 * @see co.edu.unbosque.proyectoFinal.repository.UsuarioRepository
 */
@Entity
@Table(name = "usuarios")
public class Usuario extends Persona {

	private static final long serialVersionUID = 1L;

	/**
	 * Historial de análisis VirusTotal del usuario.
	 * La cascada {@code ALL} garantiza que los registros de historial
	 * se eliminen automáticamente al eliminar el usuario.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<VirusTotalUploadResponse> historial;

	/**
	 * Constructor vacío. Establece el rol como {@code USUARIO} e inicializa
	 * el historial como una lista vacía.
	 */
	public Usuario() {
		this.setRole(Persona.Role.USUARIO);
	}

	/**
	 * Constructor con historial de análisis ya existente.
	 * @param historial lista de respuestas de análisis VirusTotal
	 */
	public Usuario(List<VirusTotalUploadResponse> historial) {
		super();
		this.historial = List.copyOf(historial);
		this.setRole(Persona.Role.USUARIO);
	}

	/**
	 * Constructor completo con todos los campos del usuario.
	 *
	 * @param nombre     nombre único del usuario
	 * @param email      correo electrónico único
	 * @param telefono   teléfono de 10 dígitos
	 * @param contrasena contraseña (se cifrará con BCrypt en el servicio)
	 * @param historial  historial previo de análisis VirusTotal
	 */
	public Usuario(String nombre, String email, String telefono, String contrasena,
			List<VirusTotalUploadResponse> historial) {
		super(nombre, email, telefono, contrasena);
		this.historial = List.copyOf(historial);
		this.setRole(Persona.Role.USUARIO);
	}

	/**
	 * Constructor para creación de nuevos usuarios. El historial se inicializa
	 * como una {@link ArrayList} vacía mutable para permitir agregar análisis
	 * posteriormente.
	 *
	 * @param nombre     nombre único del usuario
	 * @param email      correo electrónico único
	 * @param telefono   teléfono de 10 dígitos
	 * @param contrasena contraseña en texto plano
	 */
	public Usuario(String nombre, String email, String telefono, String contrasena) {
		super(nombre, email, telefono, contrasena);
		this.historial = new ArrayList<>();
		this.setRole(Persona.Role.USUARIO);
	}

	/** @return historial de análisis VirusTotal del usuario */
	public List<VirusTotalUploadResponse> getHistorial() { return historial; }

	/**
	 * Reemplaza el historial con una copia inmutable de la lista proporcionada.
	 * @param historial nueva lista de respuestas de análisis
	 */
	public void setHistorial(List<VirusTotalUploadResponse> historial) {
		this.historial = List.copyOf(historial);
	}

	/**
	 * Retorna las autoridades de Spring Security.
	 * @return colección con la autoridad {@code ROLE_USUARIO}
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
	}

	/**
	 * Retorna la contraseña requerida por {@code UserDetails} (hash BCrypt).
	 * @return contraseña del usuario
	 */
	@Override
	public String getPassword() { return super.getContrasena(); }

	/**
	 * Retorna el nombre de usuario requerido por {@code UserDetails}.
	 * @return nombre del usuario
	 */
	@Override
	public String getUsername() { return super.getNombre(); }
}
