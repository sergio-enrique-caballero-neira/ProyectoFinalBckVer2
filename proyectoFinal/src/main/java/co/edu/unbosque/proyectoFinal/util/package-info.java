/**
 * Paquete de utilidades transversales de la aplicación.
 *
 * <p>Contiene clases de apoyo con funcionalidad reutilizable que no pertenece
 * a ninguna capa específica del modelo MVC.</p>
 *
 * <p><strong>Clases disponibles:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.util.AESUtil} –
 *       Clase utilitaria estática que provee:
 *       <ul>
 *         <li>Cifrado y descifrado simétrico AES en modo GCM sin padding,
 *             con clave e IV configurables o con valores predeterminados.</li>
 *         <li>Funciones de hashing unidireccional: MD5, SHA-1, SHA-256,
 *             SHA-384 y SHA-512, implementadas sobre Apache Commons Codec.</li>
 *       </ul>
 *       <strong>Nota:</strong> La clave y el IV predeterminados ({@code "llavede16carater"}
 *       y {@code "programacioncomp"}) son valores de ejemplo para entornos de
 *       desarrollo. En producción deben externalizarse a variables de entorno
 *       o a un gestor de secretos.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.util;
