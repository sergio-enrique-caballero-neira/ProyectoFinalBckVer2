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

import co.edu.unbosque.proyectoFinal.dto.UsuarioDTO;
import co.edu.unbosque.proyectoFinal.dto.VirusTotalUploadResponseDTO;
import co.edu.unbosque.proyectoFinal.entity.Usuario;
import co.edu.unbosque.proyectoFinal.entity.VirusTotalUploadResponse;
import co.edu.unbosque.proyectoFinal.exception.BadRequestException;
import co.edu.unbosque.proyectoFinal.exception.ResourceNotFoundException;
import co.edu.unbosque.proyectoFinal.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private ModelMapper mapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UsuarioService usuarioService;

	private UsuarioDTO validDTO;
	private Usuario validEntity;

	@BeforeEach
	void setUp() {
		validDTO = new UsuarioDTO("usuario1", "test@test.com", "3001234567", "Password1*");
		validEntity = new Usuario("usuario1", "test@test.com", "3001234567", "encoded");
	}

	@Test
	void testCreateSuccess() {
		when(mapper.map(validDTO, Usuario.class)).thenReturn(validEntity);
		when(passwordEncoder.encode(anyString())).thenReturn("encoded");
		when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

		int result = usuarioService.create(validDTO);

		assertEquals(0, result);
		verify(usuarioRepository).save(validEntity);
	}

	@Test
	void testCreateDuplicateNombre() {
		Usuario existing = new Usuario("usuario1", "other@test.com", "3009999999", "pass");
		when(usuarioRepository.findAll()).thenReturn(List.of(existing));

		assertThrows(BadRequestException.class, () -> usuarioService.create(validDTO));
	}

	@Test
	void testCreateDuplicateEmail() {
		when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());
		Usuario existing = new Usuario("other", "test@test.com", "3009999999", "pass");
		when(usuarioRepository.findAll()).thenReturn(List.of(existing));

		assertThrows(BadRequestException.class, () -> usuarioService.create(validDTO));
	}

	@Test
	void testCreateDuplicateTelefono() {
		when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());
		Usuario existing = new Usuario("other", "other@test.com", "3001234567", "pass");
		when(usuarioRepository.findAll()).thenReturn(List.of(existing));

		assertThrows(BadRequestException.class, () -> usuarioService.create(validDTO));
	}

	@Test
	void testGetAllSuccess() {
		when(usuarioRepository.findAll()).thenReturn(List.of(validEntity));
		when(mapper.map(validEntity, UsuarioDTO.class)).thenReturn(validDTO);

		List<UsuarioDTO> result = usuarioService.getAll();

		assertEquals(1, result.size());
		assertEquals("usuario1", result.get(0).getNombre());
	}

	@Test
	void testGetAllEmptyThrowsException() {
		when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

		assertThrows(ResourceNotFoundException.class, () -> usuarioService.getAll());
	}

	@Test
	void testDeleteByIDSuccess() {
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(validEntity));

		int result = usuarioService.deleteByID(1L);

		assertEquals(0, result);
		verify(usuarioRepository).deleteById(1L);
	}

	@Test
	void testDeleteByIDNotFound() {
		when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> usuarioService.deleteByID(1L));
	}

	@Test
	void testDeleteByIDInvalid() {
		assertThrows(BadRequestException.class, () -> usuarioService.deleteByID(-1L));
	}

	@Test
	void testUpdateByIDSuccess() {
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(validEntity));
		when(mapper.map(validEntity, UsuarioDTO.class)).thenReturn(validDTO);
		when(mapper.map(any(UsuarioDTO.class), eq(Usuario.class))).thenReturn(validEntity);
		when(passwordEncoder.encode(anyString())).thenReturn("newEncoded");

		UsuarioDTO updateDTO = new UsuarioDTO("usuario2", "new@test.com", "3009876543", "NewPass1*");
		int result = usuarioService.updateByID(1L, updateDTO);

		assertEquals(0, result);
		verify(usuarioRepository).save(validEntity);
	}

	@Test
	void testUpdateByIDNotFound() {
		when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> usuarioService.updateByID(1L, validDTO));
	}

	@Test
	void testUpdateByIDInvalid() {
		assertThrows(BadRequestException.class, () -> usuarioService.updateByID(0L, validDTO));
	}

	@Test
	void testCount() {
		when(usuarioRepository.count()).thenReturn(5L);

		long result = usuarioService.count();

		assertEquals(5L, result);
	}

	@Test
	void testExist() {
		when(usuarioRepository.existsById(1L)).thenReturn(true);

		assertTrue(usuarioService.exist(1L));
	}

	@Test
	void testGetIdByUsername() {
		when(usuarioRepository.findByNombre("usuario1")).thenReturn(Optional.of(validEntity));

		long result = usuarioService.getIdByUsername("usuario1");

		assertEquals(0L, result);
	}

	@Test
	void testGetHistorialByIdSuccess() {
		VirusTotalUploadResponse response = new VirusTotalUploadResponse();
		validEntity.setHistorial(new ArrayList<>(List.of(response)));
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(validEntity));

		List<VirusTotalUploadResponseDTO> result = usuarioService.getHistorialById(1L);

		assertNotNull(result);
	}

	@Test
	void testGetHistorialByIdNotFound() {
		when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> usuarioService.getHistorialById(1L));
	}
}
