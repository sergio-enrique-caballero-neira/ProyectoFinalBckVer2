/**
 * Paquete de entidades JPA de la aplicación.
 *
 * <p>Contiene las clases que mapean directamente a tablas de la base de datos
 * relacional mediante anotaciones de Jakarta Persistence (JPA). Spring Data JPA
 * gestiona el ciclo de vida de estas entidades.</p>
 *
 * <p><strong>Jerarquía de entidades de persona:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.entity.Persona} – Superclase mapeada
 *       ({@code @MappedSuperclass}) que implementa {@code UserDetails}. Define
 *       los campos comunes: id, nombre, email, teléfono, contraseña y rol.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.entity.Usuario} – Entidad persistida en
 *       la tabla {@code usuarios}. Contiene el historial de análisis VirusTotal
 *       asociado al usuario ({@code @OneToMany}).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.entity.Administrador} – Entidad persistida
 *       en la tabla {@code administradores}. Agrega el campo {@code cargo}.</li>
 * </ul>
 *
 * <p><strong>Entidades de respuesta VirusTotal:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.entity.VirusTotalUploadResponse} – Raíz
 *       de la respuesta JSON de VirusTotal, relacionada con {@code Data}.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.entity.Data} – Contiene el ID del análisis,
 *       el tipo y los atributos del escaneo.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.entity.Attributes} – Agrupa el estado del
 *       análisis, las estadísticas embebidas y los resultados por motor antivirus.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.entity.Stats} – Clase embebible
 *       ({@code @Embeddable}) con los contadores de detección.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.entity.EngineResult} – Resultado individual
 *       de un motor antivirus (nombre, categoría y veredicto).</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.entity;
