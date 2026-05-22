package co.edu.unbosque.proyectoFinal.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import co.edu.unbosque.proyectoFinal.entity.Persona;
import co.edu.unbosque.proyectoFinal.entity.Usuario;

class JwtUtilTest {

	private JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil();
		// Set secret via reflection since it's injected via @Value
		try {
			java.lang.reflect.Field field = JwtUtil.class.getDeclaredField("secret");
			field.setAccessible(true);
			field.set(jwtUtil, "JWTContrasenaSeguraParaProyectoFinalSERSHEGABMAT");
		} catch (Exception e) {
			fail("Failed to set secret: " + e.getMessage());
		}
	}

	@Test
	void testGenerateAndExtractUsername() {
		User userDetails = new User("testuser", "password", List.of());
		String token = jwtUtil.generateToken(userDetails);

		String username = jwtUtil.extractUsername(token);

		assertEquals("testuser", username);
	}

	@Test
	void testExtractRoleFromPersona() {
		Usuario usuario = new Usuario("testuser", "test@test.com", "3001234567", "password");
		String token = jwtUtil.generateToken(usuario);

		String role = jwtUtil.extractRole(token);

		assertEquals("USUARIO", role);
	}

	@Test
	void testExtractExpiration() {
		User userDetails = new User("testuser", "password", List.of());
		String token = jwtUtil.generateToken(userDetails);

		java.util.Date expiration = jwtUtil.extractExpiration(token);

		assertNotNull(expiration);
		assertTrue(expiration.after(new java.util.Date()));
	}

	@Test
	void testValidateTokenSuccess() {
		User userDetails = new User("testuser", "password", List.of());
		String token = jwtUtil.generateToken(userDetails);

		assertTrue(jwtUtil.validateToken(token, userDetails));
	}

	@Test
	void testValidateTokenWrongUsername() {
		User userDetails = new User("testuser", "password", List.of());
		String token = jwtUtil.generateToken(userDetails);

		User otherUser = new User("otheruser", "password", List.of());

		assertFalse(jwtUtil.validateToken(token, otherUser));
	}

	@Test
	void testExtractClaim() {
		User userDetails = new User("testuser", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
		String token = jwtUtil.generateToken(userDetails);

		@SuppressWarnings("unchecked")
		java.util.List<String> authorities = jwtUtil.extractClaim(token, claims -> claims.get("authorities", java.util.List.class));

		assertNotNull(authorities);
	}
}
