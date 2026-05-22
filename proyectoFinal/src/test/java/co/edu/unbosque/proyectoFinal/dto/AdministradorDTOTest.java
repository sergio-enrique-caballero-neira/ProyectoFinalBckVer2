package co.edu.unbosque.proyectoFinal.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AdministradorDTOTest {

	@Test
	void testDefaultConstructorSetsRole() {
		AdministradorDTO dto = new AdministradorDTO();

		assertEquals(PersonaDTO.Role.ADMIN, dto.getRole());
	}

	@Test
	void testConstructorWithCargo() {
		AdministradorDTO dto = new AdministradorDTO("Gerente");

		assertEquals("Gerente", dto.getCargo());
		assertEquals(PersonaDTO.Role.ADMIN, dto.getRole());
	}

	@Test
	void testConstructorWithNameEmailTelefonoContrasena() {
		AdministradorDTO dto = new AdministradorDTO("admin1", "admin@test.com", "3001234567", "password");

		assertEquals("admin1", dto.getNombre());
		assertEquals("admin@test.com", dto.getEmail());
		assertEquals("3001234567", dto.getTelefono());
		assertEquals("password", dto.getContrasena());
		assertEquals(PersonaDTO.Role.ADMIN, dto.getRole());
	}

	@Test
	void testFullConstructor() {
		AdministradorDTO dto = new AdministradorDTO("admin1", "admin@test.com", "3001234567", "password", "Director");

		assertEquals("admin1", dto.getNombre());
		assertEquals("Director", dto.getCargo());
		assertEquals(PersonaDTO.Role.ADMIN, dto.getRole());
	}

	@Test
	void testGetAuthorities() {
		AdministradorDTO dto = new AdministradorDTO();

		var authorities = dto.getAuthorities();

		assertEquals(1, authorities.size());
		assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
	}

	@Test
	void testGetPassword() {
		AdministradorDTO dto = new AdministradorDTO("admin1", "admin@test.com", "3001234567", "secret");

		assertEquals("secret", dto.getPassword());
	}

	@Test
	void testGetUsername() {
		AdministradorDTO dto = new AdministradorDTO("admin1", "admin@test.com", "3001234567", "secret");

		assertEquals("admin1", dto.getUsername());
	}

	@Test
	void testGetAndSetCargo() {
		AdministradorDTO dto = new AdministradorDTO();
		dto.setCargo("Supervisor");

		assertEquals("Supervisor", dto.getCargo());
	}
}
