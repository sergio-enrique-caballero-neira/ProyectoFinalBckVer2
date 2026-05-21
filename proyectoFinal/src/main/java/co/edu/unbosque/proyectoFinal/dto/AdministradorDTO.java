package co.edu.unbosque.proyectoFinal.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * DTO (Data Transfer Object) para la entidad {@code Administrador}.
 *
 * <p>Extiende {@link PersonaDTO} añadiendo el campo {@code cargo} propio del
 * administrador. Implementa los métodos de {@code UserDetails} requeridos por
 * Spring Security para que el DTO pueda participar en el flujo de autenticación
 * JWT sin necesidad de entidades JPA.</p>
 *
 * <p>El rol se establece automáticamente como {@link PersonaDTO.Role#ADMIN}
 * en todos los constructores, y la autoridad de Spring Security resultante es
 * {@code "ROLE_ADMIN"}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see PersonaDTO
 * @see co.edu.unbosque.proyectoFinal.entity.Administrador
 */
public class AdministradorDTO extends PersonaDTO {

	private static final long serialVersionUID = 1L;

	/** Cargo o título del administrador dentro de la organización. */
	private String cargo;

	/**
	 * Constructor vacío. Establece el rol como {@code ADMIN} automáticamente.
	 */
	public AdministradorDTO() {
		this.setRole(PersonaDTO.Role.ADMIN);
	}

	/**
	 * Constructor con solo el campo {@code cargo}.
	 *
	 * @param cargo cargo del administrador
	 */
	public AdministradorDTO(String cargo) {
		super();
		this.cargo = cargo;
		this.setRole(PersonaDTO.Role.ADMIN);
	}

	/**
	 * Constructor completo con todos los campos del administrador.
	 *
	 * @param nombre     nombre único del administrador
	 * @param email      correo electrónico
	 * @param telefono   número de teléfono
	 * @param contrasena contraseña (se cifrará antes de persistir)
	 * @param cargo      cargo del administrador
	 */
	public AdministradorDTO(String nombre, String email, String telefono, String contrasena, String cargo) {
		super(nombre, email, telefono, contrasena);
		this.cargo = cargo;
		this.setRole(PersonaDTO.Role.ADMIN);
	}

	/**
	 * Constructor sin {@code cargo}, útil para actualizaciones parciales.
	 *
	 * @param nombre     nombre único del administrador
	 * @param email      correo electrónico
	 * @param telefono   número de teléfono
	 * @param contrasena contraseña
	 */
	public AdministradorDTO(String nombre, String email, String telefono, String contrasena) {
		super(nombre, email, telefono, contrasena);
		this.setRole(PersonaDTO.Role.ADMIN);
	}

	/**
	 * Retorna el cargo del administrador.
	 * @return cargo o título del administrador
	 */
	public String getCargo() { return cargo; }

	/**
	 * Establece el cargo del administrador.
	 * @param cargo nuevo cargo
	 */
	public void setCargo(String cargo) { this.cargo = cargo; }

	/**
	 * Retorna las autoridades de Spring Security del administrador.
	 * La única autoridad es {@code "ROLE_ADMIN"}.
	 *
	 * @return colección con la autoridad {@code ROLE_ADMIN}
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
	}

	/**
	 * Retorna la contraseña requerida por {@code UserDetails}.
	 * Delega a {@link PersonaDTO#getContrasena()}.
	 *
	 * @return contraseña del administrador
	 */
	@Override
	public String getPassword() { return super.getContrasena(); }

	/**
	 * Retorna el nombre de usuario requerido por {@code UserDetails}.
	 * Delega a {@link PersonaDTO#getNombre()}.
	 *
	 * @return nombre de usuario del administrador
	 */
	@Override
	public String getUsername() { return super.getNombre(); }
}
