package co.edu.unbosque.proyectoFinal.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class UsuarioTest {

	@Test
	void testDefaultConstructorSetsRole() {
		Usuario usuario = new Usuario();

		assertEquals(Persona.Role.USUARIO, usuario.getRole());
	}

	@Test
	void testConstructorWithHistorial() {
		List<VirusTotalUploadResponse> historial = new ArrayList<>();
		Usuario usuario = new Usuario(historial);

		assertEquals(Persona.Role.USUARIO, usuario.getRole());
		assertEquals(historial, usuario.getHistorial());
	}

	@Test
	void testConstructorWithNameEmailTelefonoContrasena() {
		Usuario usuario = new Usuario("usuario1", "test@test.com", "3001234567", "password");

		assertEquals("usuario1", usuario.getNombre());
		assertEquals("test@test.com", usuario.getEmail());
		assertEquals("3001234567", usuario.getTelefono());
		assertEquals("password", usuario.getContrasena());
		assertEquals(Persona.Role.USUARIO, usuario.getRole());
		assertNotNull(usuario.getHistorial());
	}

	@Test
	void testFullConstructor() {
		List<VirusTotalUploadResponse> historial = new ArrayList<>();
		Usuario usuario = new Usuario("usuario1", "test@test.com", "3001234567", "password", historial);

		assertEquals("usuario1", usuario.getNombre());
		assertEquals(Persona.Role.USUARIO, usuario.getRole());
		assertEquals(historial, usuario.getHistorial());
	}

	@Test
	void testGetAuthorities() {
		Usuario usuario = new Usuario();

		var authorities = usuario.getAuthorities();

		assertEquals(1, authorities.size());
		assertEquals("ROLE_USUARIO", authorities.iterator().next().getAuthority());
	}

	@Test
	void testGetPassword() {
		Usuario usuario = new Usuario("usuario1", "test@test.com", "3001234567", "secret");

		assertEquals("secret", usuario.getPassword());
	}

	@Test
	void testGetUsername() {
		Usuario usuario = new Usuario("usuario1", "test@test.com", "3001234567", "secret");

		assertEquals("usuario1", usuario.getUsername());
	}

	@Test
	void testGetAndSetHistorial() {
		Usuario usuario = new Usuario();
		List<VirusTotalUploadResponse> historial = new ArrayList<>();
		usuario.setHistorial(historial);

		assertEquals(historial, usuario.getHistorial());
	}
}
