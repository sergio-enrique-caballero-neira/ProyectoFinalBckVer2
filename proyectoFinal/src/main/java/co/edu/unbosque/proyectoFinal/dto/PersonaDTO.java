package co.edu.unbosque.proyectoFinal.dto;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Clase abstracta base para los DTOs de personas en el sistema.
 *
 * <p>Implementa {@link UserDetails} de Spring Security para que los DTOs
 * puedan ser utilizados directamente en el proceso de autenticación JWT,
 * sin necesidad de convertirlos a entidades primero.</p>
 *
 * <p>Define los atributos comunes a todos los tipos de persona:
 * identificador, nombre, email, teléfono, contraseña y rol. Las
 * subclases ({@link UsuarioDTO} y {@link AdministradorDTO}) heredan estos
 * atributos y añaden los propios de cada tipo.</p>
 *
 * <p>Los métodos de cuenta de Spring Security ({@code isAccountNonExpired},
 * {@code isAccountNonLocked}, {@code isCredentialsNonExpired},
 * {@code isEnabled}) retornan {@code true} por defecto al no estar
 * sobreescritos, indicando que las cuentas están activas y sin restricciones.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see UsuarioDTO
 * @see AdministradorDTO
 */
public abstract class PersonaDTO implements UserDetails {

	private static final long serialVersionUID = 1L;

	/** Identificador único de la persona en la base de datos. */
	private long id;
	/** Nombre de usuario (único en el sistema, usado como credencial de login). */
	private String nombre;
	/** Dirección de correo electrónico única del usuario. */
	private String email;
	/** Número de teléfono de 10 dígitos, único en el sistema. */
	private String telefono;
	/** Contraseña (almacenada como hash BCrypt en la BD; en texto plano solo en creación). */
	private String contrasena;
	/** Rol del usuario en el sistema ({@code USUARIO} o {@code ADMIN}). */
	private Role role;

	/**
	 * Constructor vacío requerido por frameworks de serialización y Spring.
	 */
	protected PersonaDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con todos los atributos comunes de una persona.
	 *
	 * @param nombre     nombre de usuario
	 * @param email      correo electrónico
	 * @param telefono   número de teléfono
	 * @param contrasena contraseña (se cifrará en la capa de servicio antes de persistir)
	 */
	protected PersonaDTO(String nombre, String email, String telefono, String contrasena) {
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
		/** Administrador con permisos completos sobre usuarios, administradores y VirusTotal. */
		ADMIN
	}

	/**
	 * Retorna el identificador único de la persona.
	 * @return ID de la persona
	 */
	public long getId() { return id; }

	/**
	 * Establece el identificador único de la persona.
	 * @param id ID a asignar
	 */
	public void setId(long id) { this.id = id; }

	/**
	 * Retorna el nombre de usuario.
	 * @return nombre de usuario
	 */
	public String getNombre() { return nombre; }

	/**
	 * Establece el nombre de usuario.
	 * @param nombre nuevo nombre de usuario
	 */
	public void setNombre(String nombre) { this.nombre = nombre; }

	/**
	 * Retorna el email del usuario.
	 * @return dirección de correo electrónico
	 */
	public String getEmail() { return email; }

	/**
	 * Establece el email del usuario.
	 * @param email nueva dirección de correo electrónico
	 */
	public void setEmail(String email) { this.email = email; }

	/**
	 * Retorna el teléfono del usuario.
	 * @return número de teléfono de 10 dígitos
	 */
	public String getTelefono() { return telefono; }

	/**
	 * Establece el teléfono del usuario.
	 * @param telefono nuevo número de teléfono
	 */
	public void setTelefono(String telefono) { this.telefono = telefono; }

	/**
	 * Retorna la contraseña del usuario (cifrada si viene de la BD).
	 * @return contraseña en texto plano o hash BCrypt
	 */
	public String getContrasena() { return contrasena; }

	/**
	 * Establece la contraseña del usuario.
	 * @param contrasena nueva contraseña
	 */
	public void setContrasena(String contrasena) { this.contrasena = contrasena; }

	/**
	 * Retorna el rol del usuario en el sistema.
	 * @return rol ({@code USUARIO} o {@code ADMIN})
	 */
	public Role getRole() { return role; }

	/**
	 * Establece el rol del usuario en el sistema.
	 * @param role rol a asignar
	 */
	public void setRole(Role role) { this.role = role; }
}
