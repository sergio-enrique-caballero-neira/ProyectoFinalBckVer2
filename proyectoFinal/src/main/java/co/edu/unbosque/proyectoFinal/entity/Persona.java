package co.edu.unbosque.proyectoFinal.entity;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Clase base abstracta para todas las personas del sistema (usuarios y administradores).
 * Implementa UserDetails para integracion con Spring Security.
 * Usa @MappedSuperclass para que las subclases hereden las columnas en sus propias tablas.
 */
@MappedSuperclass
public abstract class Persona implements UserDetails{

	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
    private String nombre;
    private String email;
    private String telefono;
    private String contrasena;
    @Enumerated(EnumType.STRING)
	private Role role;
    
    /**
     * Constructor protegido vacio.
     */
    protected Persona() {
		// TODO Auto-generated constructor stub
	}

    /**
     * Constructor con datos basicos de la persona.
     * @param nombre nombre de la persona (usado como username en Spring Security)
     * @param email correo electronico
     * @param telefono numero de telefono
     * @param contrasena contraseña encriptada
     */
    protected Persona(String nombre, String email, String telefono, String contrasena) {
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
