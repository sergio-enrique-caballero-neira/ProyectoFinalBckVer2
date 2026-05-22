package co.edu.unbosque.proyectoFinal.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AESUtilTest {

	@Test
	void testEncryptAndDecryptWithKey() {
		String key = "llavede16carater";
		String iv = "programacioncomp";
		String texto = "mensaje secreto";

		String encrypted = AESUtil.encrypt(key, iv, texto);
		String decrypted = AESUtil.decrypt(key, iv, encrypted);

		assertEquals(texto, decrypted);
	}

	@Test
	void testEncryptAndDecryptWithDefaults() {
		String texto = "mensaje secreto";

		String encrypted = AESUtil.encrypt(texto);
		String decrypted = AESUtil.decrypt(encrypted);

		assertEquals(texto, decrypted);
	}

	@Test
	void testHashingToMD5() {
		String content = "test";
		String hash = AESUtil.hashingToMD5(content);

		assertNotNull(hash);
		assertEquals(32, hash.length());
	}

	@Test
	void testHashingToSHA1() {
		String content = "test";
		String hash = AESUtil.hashingToSHA1(content);

		assertNotNull(hash);
		assertEquals(40, hash.length());
	}

	@Test
	void testHashingToSHA256() {
		String content = "test";
		String hash = AESUtil.hashingToSHA256(content);

		assertNotNull(hash);
		assertEquals(64, hash.length());
	}

	@Test
	void testHashingToSHA384() {
		String content = "test";
		String hash = AESUtil.hashingToSHA384(content);

		assertNotNull(hash);
		assertEquals(96, hash.length());
	}

	@Test
	void testHashingToSHA512() {
		String content = "test";
		String hash = AESUtil.hashingToSHA512(content);

		assertNotNull(hash);
		assertEquals(128, hash.length());
	}

	@Test
	void testDifferentInputsProduceDifferentHashes() {
		String hash1 = AESUtil.hashingToSHA256("input1");
		String hash2 = AESUtil.hashingToSHA256("input2");

		assertNotEquals(hash1, hash2);
	}
}
