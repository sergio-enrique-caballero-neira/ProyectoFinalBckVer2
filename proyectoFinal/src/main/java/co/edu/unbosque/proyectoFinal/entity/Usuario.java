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
 * Entidad JPA que representa un usuario regular del sistema.
 * Se almacena en la tabla "usuarios" y tiene rol USUARIO.
 * Incluye un historial de analisis de VirusTotal asociados.
 */
@Entity
@Table(name = "usuarios")
public class Usuario extends Persona {


	private static final long serialVersionUID = 1L;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<VirusTotalUploadResponse> historial;

	/**
	 * Constructor vacio. Establece el rol como USUARIO.
	 */
	public Usuario() {
		this.setRole(Persona.Role.USUARIO);
	}

	/**
	 * Constructor con historial de analisis.
	 * @param historial lista de respuestas de VirusTotal
	 */
	public Usuario(List<VirusTotalUploadResponse> historial) {
		super();
		this.historial = List.copyOf(historial);
		this.setRole(Persona.Role.USUARIO);
	}

	/**
	 * Constructor completo con todos los datos del usuario.
	 * @param nombre nombre de usuario
	 * @param email correo electronico
	 * @param telefono numero de telefono
	 * @param contrasena contraseña encriptada
	 * @param historial lista de respuestas de VirusTotal
	 */
	public Usuario(String nombre, String email, String telefono, String contrasena,
			List<VirusTotalUploadResponse> historial) {
		super(nombre, email, telefono, contrasena);
		this.historial = List.copyOf(historial);
		this.setRole(Persona.Role.USUARIO);
	}

	/**
	 * Constructor con datos basicos sin historial.
	 * @param nombre nombre de usuario
	 * @param email correo electronico
	 * @param telefono numero de telefono
	 * @param contrasena contraseña encriptada
	 */
	public Usuario(String nombre, String email, String telefono, String contrasena) {
		super(nombre, email, telefono, contrasena);
		this.historial = new ArrayList<>();
		this.setRole(Persona.Role.USUARIO);
	}

	/**
	 * Obtiene el historial de analisis de VirusTotal.
	 * @return lista de respuestas de VirusTotal
	 */
	public List<VirusTotalUploadResponse> getHistorial() {
		return historial;
	}

	/**
	 * Establece el historial de analisis de VirusTotal.
	 * @param historial nueva lista de respuestas
	 */
	public void setHistorial(List<VirusTotalUploadResponse> historial) {
		this.historial = List.copyOf(historial);
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
