package co.edu.unbosque.proyectoFinal.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa a un administrador del sistema.
 *
 * <p>Extiende {@link Persona} añadiendo el campo {@code cargo}, y se persiste
 * en la tabla {@code administradores}. Implementa los métodos de
 * {@code UserDetails} para que Spring Security pueda autenticar administradores
 * directamente desde la base de datos.</p>
 *
 * <p>El rol se establece automáticamente como {@link Persona.Role#ADMIN}
 * en todos los constructores. La autoridad resultante para Spring Security
 * es {@code "ROLE_ADMIN"}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see Persona
 * @see co.edu.unbosque.proyectoFinal.repository.AdministradorRepository
 */
@Entity
@Table(name = "administradores")
public class Administrador extends Persona {

	private static final long serialVersionUID = 1L;

	/** Cargo o título del administrador dentro de la organización. */
	private String cargo;

	/**
	 * Constructor vacío. Establece el rol como {@code ADMIN} automáticamente.
	 */
	public Administrador() {
		this.setRole(Persona.Role.ADMIN);
	}

	/**
	 * Constructor con solo el campo {@code cargo}.
	 * @param cargo cargo del administrador
	 */
	public Administrador(String cargo) {
		super();
		this.cargo = cargo;
		this.setRole(Persona.Role.ADMIN);
	}

	/**
	 * Constructor completo con todos los campos del administrador.
	 *
	 * @param nombre     nombre único del administrador
	 * @param email      correo electrónico único
	 * @param telefono   teléfono de 10 dígitos
	 * @param contrasena contraseña (se cifrará con BCrypt en el servicio)
	 * @param cargo      cargo del administrador
	 */
	public Administrador(String nombre, String email, String telefono, String contrasena, String cargo) {
		super(nombre, email, telefono, contrasena);
		this.cargo = cargo;
		this.setRole(Persona.Role.ADMIN);
	}

	/**
	 * Constructor sin {@code cargo}.
	 *
	 * @param nombre     nombre único del administrador
	 * @param email      correo electrónico
	 * @param telefono   teléfono
	 * @param contrasena contraseña
	 */
	public Administrador(String nombre, String email, String telefono, String contrasena) {
		super(nombre, email, telefono, contrasena);
		this.setRole(Persona.Role.ADMIN);
	}

	/** @return cargo del administrador */
	public String getCargo() { return cargo; }
	/** @param cargo nuevo cargo del administrador */
	public void setCargo(String cargo) { this.cargo = cargo; }

	/**
	 * Retorna las autoridades de Spring Security.
	 * @return colección con la autoridad {@code ROLE_ADMIN}
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
	}

	/**
	 * Retorna la contraseña requerida por {@code UserDetails} (hash BCrypt).
	 * @return contraseña del administrador
	 */
	@Override
	public String getPassword() { return super.getContrasena(); }

	/**
	 * Retorna el nombre de usuario requerido por {@code UserDetails}.
	 * @return nombre de usuario del administrador
	 */
	@Override
	public String getUsername() { return super.getNombre(); }
}
