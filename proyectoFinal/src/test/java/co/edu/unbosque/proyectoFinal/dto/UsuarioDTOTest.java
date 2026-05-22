package co.edu.unbosque.proyectoFinal.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class UsuarioDTOTest {

	@Test
	void testDefaultConstructorSetsRole() {
		UsuarioDTO dto = new UsuarioDTO();

		assertEquals(PersonaDTO.Role.USUARIO, dto.getRole());
	}

	@Test
	void testConstructorWithHistorial() {
		List<VirusTotalUploadResponseDTO> historial = new ArrayList<>();
		UsuarioDTO dto = new UsuarioDTO(historial);

		assertEquals(PersonaDTO.Role.USUARIO, dto.getRole());
		assertEquals(historial, dto.getHistorial());
	}

	@Test
	void testConstructorWithNameEmailTelefonoContrasena() {
		UsuarioDTO dto = new UsuarioDTO("usuario1", "test@test.com", "3001234567", "password");

		assertEquals("usuario1", dto.getNombre());
		assertEquals("test@test.com", dto.getEmail());
		assertEquals("3001234567", dto.getTelefono());
		assertEquals("password", dto.getContrasena());
		assertEquals(PersonaDTO.Role.USUARIO, dto.getRole());
	}

	@Test
	void testFullConstructor() {
		List<VirusTotalUploadResponseDTO> historial = new ArrayList<>();
		UsuarioDTO dto = new UsuarioDTO("usuario1", "test@test.com", "3001234567", "password", historial);

		assertEquals("usuario1", dto.getNombre());
		assertEquals(PersonaDTO.Role.USUARIO, dto.getRole());
		assertEquals(historial, dto.getHistorial());
	}

	@Test
	void testGetAuthorities() {
		UsuarioDTO dto = new UsuarioDTO();

		var authorities = dto.getAuthorities();

		assertEquals(1, authorities.size());
		assertEquals("ROLE_USUARIO", authorities.iterator().next().getAuthority());
	}

	@Test
	void testGetPassword() {
		UsuarioDTO dto = new UsuarioDTO("usuario1", "test@test.com", "3001234567", "secret");

		assertEquals("secret", dto.getPassword());
	}

	@Test
	void testGetUsername() {
		UsuarioDTO dto = new UsuarioDTO("usuario1", "test@test.com", "3001234567", "secret");

		assertEquals("usuario1", dto.getUsername());
	}

	@Test
	void testGetAndSetHistorial() {
		UsuarioDTO dto = new UsuarioDTO();
		List<VirusTotalUploadResponseDTO> historial = new ArrayList<>();
		dto.setHistorial(historial);

		assertEquals(historial, dto.getHistorial());
	}
}
