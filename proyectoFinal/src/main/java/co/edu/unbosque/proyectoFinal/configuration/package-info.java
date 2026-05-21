/**
 * Paquete de configuración de la aplicación.
 *
 * <p>Contiene las clases que Spring carga durante el arranque para
 * preparar el entorno de ejecución:</p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.configuration.LoadDatabase} –
 *       Siembra datos iniciales en la base de datos (usuario administrador y
 *       usuario normal de prueba) al iniciar la aplicación, siempre que no
 *       existan previamente.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.configuration.OpenApiConfig} –
 *       Configura la documentación interactiva Swagger/OpenAPI 3, incluyendo
 *       el esquema de seguridad Bearer JWT, ejemplos de respuestas de error
 *       reutilizables y la descripción general de la API.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.configuration;
