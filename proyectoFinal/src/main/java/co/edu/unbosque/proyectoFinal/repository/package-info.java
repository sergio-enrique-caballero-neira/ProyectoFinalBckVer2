/**
 * Paquete de repositorios de acceso a datos.
 *
 * <p>Contiene las interfaces de Spring Data JPA que proveen operaciones CRUD
 * sobre las entidades de la base de datos sin necesidad de implementación
 * manual. Spring genera automáticamente las implementaciones en tiempo de
 * ejecución.</p>
 *
 * <p><strong>Repositorios disponibles:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.repository.UsuarioRepository} –
 *       Repositorio para la entidad {@code Usuario}. Extiende
 *       {@code JpaRepository<Usuario, Long>} y agrega el método de consulta
 *       derivado {@code findByNombre(String)} para búsqueda por nombre de
 *       usuario, utilizado en la autenticación JWT.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.repository.AdministradorRepository} –
 *       Repositorio para la entidad {@code Administrador}. Extiende
 *       {@code JpaRepository<Administrador, Long>} y agrega el método
 *       {@code findByNombre(String)} para búsqueda por nombre de administrador.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.repository;
