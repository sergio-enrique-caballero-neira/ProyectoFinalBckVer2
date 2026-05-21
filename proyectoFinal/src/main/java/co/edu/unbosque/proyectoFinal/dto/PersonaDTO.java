package co.edu.unbosque.proyectoFinal.dto;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * DTO base para la transferencia de datos de personas (usuarios y administradores).
 * Implementa UserDetails para integracion con Spring Security.
 */
public abstract class PersonaDTO implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private long id;
    private String nombre;
    private String email;
    private String telefono;
    private String contrasena;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
