package co.edu.unbosque.proyectoFinal.entity;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Superclase mapeada que define la estructura común de todas las personas
 * en el sistema (usuarios y administradores).
 *
 * <p>Anotada con {@code @MappedSuperclass}, sus campos se incluyen en las
 * tablas de las subclases ({@code usuarios} y {@code administradores}) pero
 * no genera una tabla propia. Implementa {@link UserDetails} de Spring Security
 * para integrarse con el mecanismo de autenticación JWT.</p>
 *
 * <p>El campo {@code role} se persiste como cadena de texto en la base de datos
 * mediante {@code @Enumerated(EnumType.STRING)} para mayor legibilidad y
 * resistencia a reordenaciones del enum.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see Usuario
 * @see Administrador
 */
@MappedSuperclass
public abstract class Persona implements UserDetails {

	private static final long serialVersionUID = 1L;

	/** Identificador único generado automáticamente por la base de datos. */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
	/** Nombre de usuario único en el sistema, usado como credencial de login. */
	private String nombre;
	/** Dirección de correo electrónico única del usuario. */
	private String email;
	/** Número de teléfono de 10 dígitos, único en el sistema. */
	private String telefono;
	/** Contraseña almacenada como hash BCrypt. */
	private String contrasena;
	/** Rol del usuario, persistido como texto en la base de datos. */
	@Enumerated(EnumType.STRING)
	private Role role;

	/**
	 * Constructor vacío requerido por JPA.
	 */
	protected Persona() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con los campos comunes de una persona.
	 *
	 * @param nombre     nombre de usuario único
	 * @param email      correo electrónico único
	 * @param telefono   teléfono de 10 dígitos
	 * @param contrasena contraseña (se cifrará con BCrypt en la capa de servicio)
	 */
	protected Persona(String nombre, String email, String telefono, String contrasena) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
		this.contrasena = contrasena;
	}

	/**
	 * Enumeración de roles disponibles en el sistema de control de acceso.
	 */
	public enum Role {
		/** Usuario regular con permisos básicos de consulta y uso de VirusTotal. */
		USUARIO,
		/** Administrador con permisos completos sobre usuarios y administradores. */
		ADMIN
	}

	/** @return identificador único de la persona */
	public long getId() { return id; }
	/** @param id identificador a asignar */
	public void setId(long id) { this.id = id; }

	/** @return nombre de usuario */
	public String getNombre() { return nombre; }
	/** @param nombre nuevo nombre de usuario */
	public void setNombre(String nombre) { this.nombre = nombre; }

	/** @return correo electrónico */
	public String getEmail() { return email; }
	/** @param email nuevo correo electrónico */
	public void setEmail(String email) { this.email = email; }

	/** @return número de teléfono */
	public String getTelefono() { return telefono; }
	/** @param telefono nuevo número de teléfono */
	public void setTelefono(String telefono) { this.telefono = telefono; }

	/** @return contraseña (hash BCrypt) */
	public String getContrasena() { return contrasena; }
	/** @param contrasena nueva contraseña */
	public void setContrasena(String contrasena) { this.contrasena = contrasena; }

	/** @return rol del usuario en el sistema */
	public Role getRole() { return role; }
	/** @param role rol a asignar */
	public void setRole(Role role) { this.role = role; }
}
