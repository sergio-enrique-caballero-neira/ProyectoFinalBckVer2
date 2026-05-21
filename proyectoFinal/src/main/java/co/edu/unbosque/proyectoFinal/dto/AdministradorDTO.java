package co.edu.unbosque.proyectoFinal.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * DTO para la transferencia de datos de administradores.
 * Extiende PersonaDTO y agrega el cargo del administrador.
 */
public class AdministradorDTO extends PersonaDTO {

	private static final long serialVersionUID = 1L;
	
	private String cargo;
	
	/**
	 * Constructor vacio. Establece el rol como ADMIN.
	 */
	public AdministradorDTO() {
		this.setRole(PersonaDTO.Role.ADMIN);
	}

	/**
	 * Constructor solo con cargo.
	 * @param cargo cargo del administrador
	 */
	public AdministradorDTO(String cargo) {
		super();
		this.cargo = cargo;
		this.setRole(PersonaDTO.Role.ADMIN);
	}

	/**
	 * Constructor completo con todos los datos del administrador.
	 * @param nombre nombre del administrador
	 * @param email correo electronico
	 * @param telefono numero de telefono
	 * @param contrasena contraseña
	 * @param cargo cargo del administrador
	 */
	public AdministradorDTO(String nombre, String email, String telefono, String contrasena, String cargo) {
		super(nombre, email, telefono, contrasena);
		this.cargo = cargo;
		this.setRole(PersonaDTO.Role.ADMIN);
	}

	/**
	 * Constructor con datos basicos sin cargo.
	 * @param nombre nombre del administrador
	 * @param email correo electronico
	 * @param telefono numero de telefono
	 * @param contrasena contraseña
	 */
	public AdministradorDTO(String nombre, String email, String telefono, String contrasena) {
		super(nombre, email, telefono, contrasena);
		this.setRole(PersonaDTO.Role.ADMIN);
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
