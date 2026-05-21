package co.edu.unbosque.proyectoFinal.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un administrador del sistema.
 * Se almacena en la tabla "administradores" y tiene rol ADMIN.
 * Incluye un campo adicional "cargo" para describir la funcion del administrador.
 */
@Entity
@Table(name = "administradores")
public class Administrador extends Persona {

	private static final long serialVersionUID = 1L;
	
	private String cargo;
	
	/**
	 * Constructor vacio. Establece el rol como ADMIN.
	 */
	public Administrador() {
		this.setRole(Persona.Role.ADMIN);
	}

	/**
	 * Constructor solo con cargo.
	 * @param cargo cargo del administrador
	 */
	public Administrador(String cargo) {
		super();
		this.cargo = cargo;
		this.setRole(Persona.Role.ADMIN);
	}

	/**
	 * Constructor completo con todos los datos del administrador.
	 * @param nombre nombre del administrador
	 * @param email correo electronico
	 * @param telefono numero de telefono
	 * @param contrasena contraseña encriptada
	 * @param cargo cargo del administrador
	 */
	public Administrador(String nombre, String email, String telefono, String contrasena, String cargo) {
		super(nombre, email, telefono, contrasena);
		this.cargo = cargo;
		this.setRole(Persona.Role.ADMIN);
	}

	/**
	 * Constructor con datos basicos sin cargo.
	 * @param nombre nombre del administrador
	 * @param email correo electronico
	 * @param telefono numero de telefono
	 * @param contrasena contraseña encriptada
	 */
	public Administrador(String nombre, String email, String telefono, String contrasena) {
		super(nombre, email, telefono, contrasena);
		this.setRole(Persona.Role.ADMIN);
	}

	/**
	 * Obtiene el cargo del administrador.
	 * @return cargo del administrador
	 */
	public String getCargo() {
		return cargo;
	}

	/**
	 * Establece el cargo del administrador.
	 * @param cargo nuevo cargo
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
	}

	@Override
	public String getPassword() {
		return super.getContrasena();
	}

	@Override
	public String getUsername() {
		return super.getNombre();
	}
	
}
