package co.edu.unbosque.proyectoFinal.dto;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * DTO base para la transferencia de datos de personas (usuarios y administradores).
 * Implementa UserDetails para integracion con Spring Security.
 */
public abstract class PersonaDTO implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	/** Identificador unico de la persona. */
	private long id;
	/** Nombre de usuario de la persona. */
    private String nombre;
    /** Correo electronico de la persona. */
    private String email;
    /** Numero de telefono de la persona. */
    private String telefono;
    /** Contraseña de la persona. */
    private String contrasena;
    /** Rol de la persona (USUARIO o ADMIN). */
    private Role role;
    
    /**
     * Constructor protegido vacio.
     */
    protected PersonaDTO() {
	}

    /**
     * Constructor con datos basicos de la persona.
     * @param nombre nombre de la persona
     * @param email correo electronico
     * @param telefono numero de telefono
     * @param contrasena contraseña
     */
    protected PersonaDTO(String nombre, String email, String telefono, String contrasena) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
		this.contrasena = contrasena;
	}
	
	/**
	 * Enumeracion de roles disponibles en el sistema.
	 */
	public enum Role {
		/** Usuario regular con permisos basicos */
		USUARIO, 
		/** Administrador con permisos completos */
		ADMIN
	}

	/**
	 * Obtiene el identificador de la persona.
	 * @return id de la persona
	 */
	public long getId() {
		return id;
	}

	/**
	 * Establece el identificador de la persona.
	 * @param id nuevo id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre de usuario.
	 * @return nombre de la persona
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre de usuario.
	 * @param nombre nuevo nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene el correo electronico.
	 * @return email de la persona
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Establece el correo electronico.
	 * @param email nuevo email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Obtiene el numero de telefono.
	 * @return telefono de la persona
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Establece el numero de telefono.
	 * @param telefono nuevo telefono
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Obtiene la contraseña.
	 * @return contraseña de la persona
	 */
	public String getContrasena() {
		return contrasena;
	}

	/**
	 * Establece la contraseña.
	 * @param contrasena nueva contraseña
	 */
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	/**
	 * Obtiene el rol de la persona.
	 * @return rol (USUARIO o ADMIN)
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Establece el rol de la persona.
	 * @param role nuevo rol
	 */
	public void setRole(Role role) {
		this.role = role;
	}
	
}
