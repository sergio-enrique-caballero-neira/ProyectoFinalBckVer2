/**
 * Paquete de Objetos de Transferencia de Datos (DTO).
 *
 * <p>Los DTOs desacoplan la capa de presentación (controladores) de la capa
 * de persistencia (entidades JPA). Son los objetos que viajan entre el cliente
 * HTTP y los servicios de la aplicación.</p>
 *
 * <p><strong>Jerarquía de DTOs de persona:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.dto.PersonaDTO} – Clase abstracta base
 *       que implementa {@code UserDetails} de Spring Security. Contiene los atributos
 *       comunes (id, nombre, email, teléfono, contraseña, rol).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.dto.UsuarioDTO} – Extiende {@code PersonaDTO}
 *       con el historial de análisis VirusTotal del usuario.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.dto.AdministradorDTO} – Extiende {@code PersonaDTO}
 *       con el campo {@code cargo} del administrador.</li>
 * </ul>
 *
 * <p><strong>DTOs de respuesta VirusTotal:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.dto.VirusTotalUploadResponseDTO} – Envuelve
 *       el resultado completo de una operación VirusTotal.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.dto.DataDTO} – Contiene el identificador
 *       del análisis, el tipo y los atributos del archivo.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.dto.AttributesDTO} – Agrupa el estado del
 *       análisis, las estadísticas y los resultados por motor antivirus.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.dto.StatsDTO} – Contadores de detecciones
 *       (malicioso, sospechoso, no detectado, inofensivo).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.dto.EngineResultDTO} – Resultado individual
 *       de un motor antivirus específico.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.dto;
