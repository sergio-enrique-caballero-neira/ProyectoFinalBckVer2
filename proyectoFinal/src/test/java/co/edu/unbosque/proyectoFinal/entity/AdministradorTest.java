package co.edu.unbosque.proyectoFinal.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AdministradorTest {

	@Test
	void testDefaultConstructorSetsRole() {
		Administrador admin = new Administrador();

		assertEquals(Persona.Role.ADMIN, admin.getRole());
	}

	@Test
	void testConstructorWithCargo() {
		Administrador admin = new Administrador("Gerente");

		assertEquals("Gerente", admin.getCargo());
		assertEquals(Persona.Role.ADMIN, admin.getRole());
	}

	@Test
	void testConstructorWithNameEmailTelefonoContrasena() {
		Administrador admin = new Administrador("admin1", "admin@test.com", "3001234567", "password");

		assertEquals("admin1", admin.getNombre());
		assertEquals("admin@test.com", admin.getEmail());
		assertEquals("3001234567", admin.getTelefono());
		assertEquals("password", admin.getContrasena());
		assertEquals(Persona.Role.ADMIN, admin.getRole());
	}

	@Test
	void testFullConstructor() {
		Administrador admin = new Administrador("admin1", "admin@test.com", "3001234567", "password", "Director");

		assertEquals("admin1", admin.getNombre());
		assertEquals("Director", admin.getCargo());
		assertEquals(Persona.Role.ADMIN, admin.getRole());
	}

	@Test
	void testGetAuthorities() {
		Administrador admin = new Administrador();

		var authorities = admin.getAuthorities();

		assertEquals(1, authorities.size());
		assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
	}

	@Test
	void testGetPassword() {
		Administrador admin = new Administrador("admin1", "admin@test.com", "3001234567", "secret");

		assertEquals("secret", admin.getPassword());
	}

	@Test
	void testGetUsername() {
		Administrador admin = new Administrador("admin1", "admin@test.com", "3001234567", "secret");

		assertEquals("admin1", admin.getUsername());
	}

	@Test
	void testGetAndSetCargo() {
		Administrador admin = new Administrador();
		admin.setCargo("Supervisor");

		assertEquals("Supervisor", admin.getCargo());
	}
}
