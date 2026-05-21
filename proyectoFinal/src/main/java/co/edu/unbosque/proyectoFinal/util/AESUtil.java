package co.edu.unbosque.proyectoFinal.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Clase utilitaria estática para cifrado simétrico AES y hashing criptográfico.
 *
 * <p>Provee dos grupos de funcionalidades:</p>
 *
 * <h3>1. Cifrado / descifrado AES-GCM</h3>
 * <p>Utiliza el modo <strong>AES/GCM/NoPadding</strong> (Galois/Counter Mode), que
 * proporciona cifrado autenticado: además de confidencialidad, garantiza integridad
 * y autenticidad de los datos sin necesidad de un HMAC adicional.</p>
 * <ul>
 *   <li>Los métodos con parámetros explícitos ({@link #encrypt(String, String, String)} y
 *       {@link #decrypt(String, String, String)}) permiten especificar clave e IV
 *       personalizados.</li>
 *   <li>Los métodos sin parámetros de clave usan los valores predeterminados
 *       {@link #DEFAULT_KEY} y {@link #DEFAULT_IV}, útiles para pruebas rápidas.</li>
 * </ul>
 *
 * <h3>2. Funciones de hashing unidireccional</h3>
 * <p>Implementadas mediante {@link org.apache.commons.codec.digest.DigestUtils}:</p>
 * <ul>
 *   <li>{@link #md5(String)}    – MD5 (128 bits, no recomendado para seguridad)</li>
 *   <li>{@link #sha1(String)}   – SHA-1 (160 bits, deprecado para uso en seguridad)</li>
 *   <li>{@link #sha256(String)} – SHA-256 (256 bits, recomendado)</li>
 *   <li>{@link #sha384(String)} – SHA-384 (384 bits)</li>
 *   <li>{@link #sha512(String)} – SHA-512 (512 bits)</li>
 * </ul>
 *
 * <p><strong>⚠️ Aviso de seguridad:</strong> los valores predeterminados de clave e IV
 * ({@code "llavede16carater"} y {@code "programacioncomp"}) son públicos y solo deben
 * usarse en entornos de desarrollo o pruebas. En producción, externalice estos valores
 * a variables de entorno o a un gestor de secretos (p. ej. Vault, AWS Secrets Manager).</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
public final class AESUtil {

    /**
     * Clave AES predeterminada de 16 caracteres (128 bits).
     * <strong>Solo para uso en desarrollo/pruebas.</strong>
     */
    private static final String DEFAULT_KEY = "llavede16carater";

    /**
     * Vector de inicialización (IV) predeterminado de 16 caracteres (128 bits).
     * <strong>Solo para uso en desarrollo/pruebas.</strong>
     */
    private static final String DEFAULT_IV  = "programacioncomp";

    /** Algoritmo de cifrado: AES en modo GCM sin padding. */
    private static final String ALGORITHM   = "AES/GCM/NoPadding";

    /** Longitud del tag GCM en bits (128 bits es el máximo, recomendado por NIST). */
    private static final int GCM_TAG_LENGTH = 128;

    /** Constructor privado para prevenir instanciación de esta clase utilitaria. */
    private AESUtil() {}

    // ─── Cifrado AES-GCM ─────────────────────────────────────────────────────

    /**
     * Cifra un texto plano con AES-GCM usando la clave e IV proporcionados.
     *
     * @param plainText texto plano a cifrar; no puede ser {@code null}
     * @param key       clave AES de 16, 24 o 32 bytes (128, 192 o 256 bits)
     * @param iv        vector de inicialización de 12 o 16 bytes
     * @return texto cifrado codificado en Base64
     * @throws Exception si ocurre un error en el proceso de cifrado
     */
    public static String encrypt(String plainText, String key, String iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        GCMParameterSpec paramSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv.getBytes());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Descifra un texto cifrado con AES-GCM usando la clave e IV proporcionados.
     *
     * @param cipherText texto cifrado codificado en Base64
     * @param key        clave AES usada durante el cifrado
     * @param iv         vector de inicialización usado durante el cifrado
     * @return texto plano descifrado
     * @throws Exception si la clave o IV son incorrectos, o el texto está corrupto
     */
    public static String decrypt(String cipherText, String key, String iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        GCMParameterSpec paramSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv.getBytes());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decryptedBytes);
    }

    /**
     * Cifra un texto plano con AES-GCM usando la clave e IV predeterminados.
     *
     * <p><strong>Solo para uso en desarrollo/pruebas.</strong></p>
     *
     * @param plainText texto plano a cifrar
     * @return texto cifrado codificado en Base64
     * @throws Exception si ocurre un error en el proceso de cifrado
     * @see #DEFAULT_KEY
     * @see #DEFAULT_IV
     */
    public static String encrypt(String plainText) throws Exception {
        return encrypt(plainText, DEFAULT_KEY, DEFAULT_IV);
    }

    /**
     * Descifra un texto con AES-GCM usando la clave e IV predeterminados.
     *
     * <p><strong>Solo para uso en desarrollo/pruebas.</strong></p>
     *
     * @param cipherText texto cifrado codificado en Base64
     * @return texto plano descifrado
     * @throws Exception si el texto está corrupto
     * @see #DEFAULT_KEY
     * @see #DEFAULT_IV
     */
    public static String decrypt(String cipherText) throws Exception {
        return decrypt(cipherText, DEFAULT_KEY, DEFAULT_IV);
    }

    // ─── Funciones de hashing ─────────────────────────────────────────────────

    /**
     * Calcula el hash MD5 del texto proporcionado.
     *
     * <p><strong>Nota:</strong> MD5 no debe usarse para almacenamiento de contraseñas
     * ni para verificación de integridad en contextos de seguridad. Use SHA-256 o superior.</p>
     *
     * @param input texto de entrada
     * @return hash MD5 en representación hexadecimal (32 caracteres)
     */
    public static String md5(String input) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(input);
    }

    /**
     * Calcula el hash SHA-1 del texto proporcionado.
     *
     * <p><strong>Nota:</strong> SHA-1 está deprecado para uso en seguridad. Use SHA-256.</p>
     *
     * @param input texto de entrada
     * @return hash SHA-1 en representación hexadecimal (40 caracteres)
     */
    public static String sha1(String input) {
        return org.apache.commons.codec.digest.DigestUtils.sha1Hex(input);
    }

    /**
     * Calcula el hash SHA-256 del texto proporcionado.
     *
     * <p>Algoritmo recomendado para verificación de integridad y uso general.</p>
     *
     * @param input texto de entrada
     * @return hash SHA-256 en representación hexadecimal (64 caracteres)
     */
    public static String sha256(String input) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
    }

    /**
     * Calcula el hash SHA-384 del texto proporcionado.
     *
     * @param input texto de entrada
     * @return hash SHA-384 en representación hexadecimal (96 caracteres)
     */
    public static String sha384(String input) {
        return org.apache.commons.codec.digest.DigestUtils.sha384Hex(input);
    }

    /**
     * Calcula el hash SHA-512 del texto proporcionado.
     *
     * @param input texto de entrada
     * @return hash SHA-512 en representación hexadecimal (128 caracteres)
     */
    public static String sha512(String input) {
        return org.apache.commons.codec.digest.DigestUtils.sha512Hex(input);
    }
}
