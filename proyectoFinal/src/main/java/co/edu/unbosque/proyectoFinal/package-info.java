/**
 * Paquete raíz de la aplicación <strong>ProyectoFinal</strong>.
 *
 * <p>Contiene la clase principal de arranque de Spring Boot ({@link co.edu.unbosque.proyectoFinal.ProyectoFinalApplication})
 * y el inicializador de servlet para despliegue en contenedores externos
 * ({@link co.edu.unbosque.proyectoFinal.ServletInitializer}).</p>
 *
 * <p>La aplicación expone una API REST para la gestión de usuarios y administradores,
 * con autenticación basada en JWT y análisis de archivos mediante la integración
 * con la API pública de <a href="https://www.virustotal.com">VirusTotal</a>.</p>
 *
 * <p><strong>Paquetes principales:</strong></p>
 * <ul>
 *   <li>{@code configuration} – Configuración de base de datos y documentación OpenAPI.</li>
 *   <li>{@code controller}    – Controladores REST (endpoints HTTP).</li>
 *   <li>{@code dto}           – Objetos de transferencia de datos.</li>
 *   <li>{@code entity}        – Entidades JPA persistidas en base de datos.</li>
 *   <li>{@code exception}     – Excepciones personalizadas y manejador global.</li>
 *   <li>{@code repository}    – Repositorios Spring Data JPA.</li>
 *   <li>{@code security}      – Configuración de seguridad, filtros JWT y carga de usuarios.</li>
 *   <li>{@code service}       – Lógica de negocio y operaciones CRUD.</li>
 *   <li>{@code util}          – Utilidades transversales (cifrado AES, hashing).</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal;
