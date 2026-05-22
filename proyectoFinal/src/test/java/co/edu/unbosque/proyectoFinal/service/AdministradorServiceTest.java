package co.edu.unbosque.proyectoFinal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.proyectoFinal.dto.AdministradorDTO;
import co.edu.unbosque.proyectoFinal.entity.Administrador;
import co.edu.unbosque.proyectoFinal.exception.BadRequestException;
import co.edu.unbosque.proyectoFinal.exception.ResourceNotFoundException;
import co.edu.unbosque.proyectoFinal.repository.AdministradorRepository;

@ExtendWith(MockitoExtension.class)
class AdministradorServiceTest {

	@Mock
	private AdministradorRepository administradorRepository;

	@Mock
	private ModelMapper mapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AdministradorService administradorService;

	private AdministradorDTO validDTO;
	private Administrador validEntity;

	@BeforeEach
	void setUp() {
		validDTO = new AdministradorDTO("admin1", "admin@test.com", "3001234567", "Password1*", "Gerente");
		validEntity = new Administrador("admin1", "admin@test.com", "3001234567", "encoded", "Gerente");
	}

	@Test
	void testCreateSuccess() {
		when(mapper.map(validDTO, Administrador.class)).thenReturn(validEntity);
		when(passwordEncoder.encode(anyString())).thenReturn("encoded");
		when(administradorRepository.findAll()).thenReturn(new ArrayList<>());

		int result = administradorService.create(validDTO);

		assertEquals(0, result);
		verify(administradorRepository).save(validEntity);
	}

	@Test
	void testCreateDuplicateNombre() {
		Administrador existing = new Administrador("admin1", "other@test.com", "3009999999", "pass", "Otro");
		when(administradorRepository.findAll()).thenReturn(List.of(existing));

		assertThrows(BadRequestException.class, () -> administradorService.create(validDTO));
	}

	@Test
	void testCreateDuplicateEmail() {
		when(administradorRepository.findAll()).thenReturn(new ArrayList<>());
		Administrador existing = new Administrador("other", "admin@test.com", "3009999999", "pass", "Otro");
		when(administradorRepository.findAll()).thenReturn(List.of(existing));

		assertThrows(BadRequestException.class, () -> administradorService.create(validDTO));
	}

	@Test
	void testCreateDuplicateTelefono() {
		when(administradorRepository.findAll()).thenReturn(new ArrayList<>());
		Administrador existing = new Administrador("other", "other@test.com", "3001234567", "pass", "Otro");
		when(administradorRepository.findAll()).thenReturn(List.of(existing));

		assertThrows(BadRequestException.class, () -> administradorService.create(validDTO));
	}

	@Test
	void testGetAllSuccess() {
		when(administradorRepository.findAll()).thenReturn(List.of(validEntity));
		when(mapper.map(validEntity, AdministradorDTO.class)).thenReturn(validDTO);

		List<AdministradorDTO> result = administradorService.getAll();

		assertEquals(1, result.size());
		assertEquals("admin1", result.get(0).getNombre());
	}

	@Test
	void testGetAllEmptyThrowsException() {
		when(administradorRepository.findAll()).thenReturn(new ArrayList<>());

		assertThrows(BadRequestException.class, () -> administradorService.getAll());
	}

	@Test
	void testDeleteByIDSuccess() {
		when(administradorRepository.findById(1L)).thenReturn(Optional.of(validEntity));

		int result = administradorService.deleteByID(1L);

		assertEquals(0, result);
		verify(administradorRepository).deleteById(1L);
	}

	@Test
	void testDeleteByIDNotFound() {
		when(administradorRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> administradorService.deleteByID(1L));
	}

	@Test
	void testDeleteByIDInvalid() {
		assertThrows(BadRequestException.class, () -> administradorService.deleteByID(-1L));
	}

	@Test
	void testUpdateByIDSuccess() {
		when(administradorRepository.findById(1L)).thenReturn(Optional.of(validEntity));
		when(mapper.map(validEntity, AdministradorDTO.class)).thenReturn(validDTO);
		when(mapper.map(any(AdministradorDTO.class), eq(Administrador.class))).thenReturn(validEntity);
		when(passwordEncoder.encode(anyString())).thenReturn("newEncoded");

		AdministradorDTO updateDTO = new AdministradorDTO("admin2", "new@test.com", "3009876543", "NewPass1*", "Director");
		int result = administradorService.updateByID(1L, updateDTO);

		assertEquals(0, result);
		verify(administradorRepository).save(validEntity);
	}

	@Test
	void testUpdateByIDNotFound() {
		when(administradorRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> administradorService.updateByID(1L, validDTO));
	}

	@Test
	void testUpdateByIDInvalid() {
		assertThrows(BadRequestException.class, () -> administradorService.updateByID(0L, validDTO));
	}

	@Test
	void testCount() {
		when(administradorRepository.count()).thenReturn(3L);

		long result = administradorService.count();

		assertEquals(3L, result);
	}

	@Test
	void testExist() {
		when(administradorRepository.existsById(1L)).thenReturn(true);

		assertTrue(administradorService.exist(1L));
	}
}
