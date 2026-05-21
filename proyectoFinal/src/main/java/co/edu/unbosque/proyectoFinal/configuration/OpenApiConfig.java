package co.edu.unbosque.proyectoFinal.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la documentación OpenAPI 3 (Swagger) para la aplicación.
 *
 * <p>Esta clase define el bean {@link OpenAPI} que Springdoc utiliza para generar
 * la UI interactiva accesible en {@code /swagger-ui/index.html}. La configuración
 * incluye:</p>
 * <ul>
 *   <li>Información general de la API (título, versión, descripción HTML,
 *       contacto y licencia).</li>
 *   <li>Esquema de seguridad {@code bearerAuth} de tipo HTTP Bearer JWT,
 *       para que Swagger UI permita autenticarse y probar los endpoints
 *       protegidos directamente desde el navegador.</li>
 *   <li>Respuestas de error reutilizables registradas como componentes globales:
 *       {@code UnauthorizedError} (401), {@code ForbiddenError} (403) y
 *       {@code NotFoundError} (404), con ejemplos en JSON.</li>
 * </ul>
 *
 * <p>La ruta {@code /swagger-ui/**} y {@code /v3/api-docs/**} están permitidas
 * sin autenticación en la configuración de seguridad
 * ({@link co.edu.unbosque.proyectoFinal.security.SecurityConfig}).</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see co.edu.unbosque.proyectoFinal.security.SecurityConfig
 */
@Configuration
public class OpenApiConfig {

	/**
	 * Construye y registra el objeto {@link OpenAPI} con toda la metadata de la
	 * documentación, el esquema de seguridad JWT y las respuestas de error comunes.
	 *
	 * @return instancia de {@code OpenAPI} con la configuración completa de la API
	 */
	@Bean
	public OpenAPI customOpenAPI() {

		String mainDescription = "<h2>API REST - Proyecto Final Sem3</h2>"
				+ "<p>API para gestión de usuarios, administradores y análisis de archivos "
				+ "con VirusTotal. Protegida con autenticación JWT.</p>" + "<h3>Conceptos básicos:</h3><ul>"
				+ "    <li><strong>JWT</strong>: Al hacer login recibirás un token que debes "
				+ "        incluir en las cabeceras de tus peticiones.</li>"
				+ "    <li><strong>Autenticación</strong>: Verifica tu identidad con nombre y contraseña.</li>"
				+ "    <li><strong>Autorización</strong>: Determina qué puede hacer cada rol.</li>" + "</ul>"
				+ "<h3>Flujo básico:</h3><ol>"
				+ "    <li>Un ADMINISTRADOR crea usuarios con <code>POST /usuario/crear</code></li>"
				+ "    <li>Inicia sesión en <code>POST /auth/login</code> con tu nombre y contraseña</li>"
				+ "    <li>Copia el token de la respuesta</li>"
				+ "    <li>Inclúyelo en el encabezado: <code>Authorization: Bearer tu_token</code></li>"
				+ "    <li>Usa los endpoints según tu rol</li>" + "</ol>"
				+ "<h3>Roles:</h3><ul>"
				+ "    <li><strong>USUARIO</strong>: Ver lista de usuarios, usar VirusTotal</li>"
				+ "    <li><strong>ADMINISTRADOR</strong>: Todo lo anterior + crear, actualizar "
				+ "        y eliminar usuarios y administradores</li>" + "</ul>"
				+ "<h3>Credenciales de prueba (precargadas al iniciar la app):</h3><ul>"
				+ "    <li>Admin → nombre: <code>admin</code> / contrasena: <code>Admin123!</code></li>"
				+ "    <li>Usuario → nombre: <code>usuario1</code> / contrasena: <code>Usuario1!</code></li>" + "</ul>"
				+ "<h3>Códigos HTTP comunes:</h3><ul>" + "    <li><strong>200/201</strong>: Éxito</li>"
				+ "    <li><strong>400</strong>: Datos incorrectos</li>"
				+ "    <li><strong>401</strong>: Token inválido o expirado</li>"
				+ "    <li><strong>403</strong>: Sin permisos suficientes</li>"
				+ "    <li><strong>404</strong>: Recurso no encontrado</li>"
				+ "    <li><strong>409</strong>: Conflicto (nombre, email o teléfono ya registrado)</li>" + "</ul>";

		String securityDescription = "Autenticación mediante JWT (JSON Web Token)." + "<p>Para autenticarte:</p><ol>"
				+ "    <li>Llama a <code>POST /auth/login</code> con tu nombre y contraseña</li>"
				+ "    <li>Copia el token de la respuesta</li>"
				+ "    <li>Haz clic en \"Authorize\" arriba en esta página</li>"
				+ "    <li>Escribe: <code>Bearer tu_token_jwt</code></li>"
				+ "    <li>Haz clic en \"Authorize\" y luego en \"Close\"</li>" + "</ol>"
				+ "<p>Ahora podrás acceder a los endpoints protegidos.</p>";

		// Crear objeto Info con la descripción HTML
		io.swagger.v3.oas.models.info.Info info =
			new io.swagger.v3.oas.models.info.Info()
				.title("API de Primera Aplicación Spring")
				.version("1.0")
				.description(mainDescription)
				.contact(
					new io.swagger.v3.oas.models.info.Contact()
						.name("Equipo de Desarrollo")
						.email("soporte@ejemplo.com")
						.url("https://github.com/tu-usuario/SpringFirstAppJWT"))
				.license(
					new io.swagger.v3.oas.models.info.License()
						.name("Licencia MIT")
						.url("https://opensource.org/licenses/MIT"));

		// Crear el esquema de seguridad Bearer JWT
		io.swagger.v3.oas.models.security.SecurityScheme securityScheme =
			new io.swagger.v3.oas.models.security.SecurityScheme()
				.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
				.description(securityDescription);

		return new OpenAPI()
				.info(info)
				.components(
					new Components()
						.addSecuritySchemes("bearerAuth", securityScheme)
						.addResponses(
							"UnauthorizedError",
							new ApiResponse()
								.description("No autenticado - Token JWT inválido o expirado")
								.content(
									new Content()
										.addMediaType(
											"application/json",
											new MediaType()
												.addExamples(
													"error",
													new Example()
														.value(
															"{\"error\": \"No autorizado\", \"mensaje\":"
																+ " \"Token inválido o expirado\"}")))))
						.addResponses(
							"ForbiddenError",
							new ApiResponse()
								.description("Acceso prohibido - No tienes permisos suficientes")
								.content(
									new Content()
										.addMediaType(
											"application/json",
											new MediaType()
												.addExamples(
													"error",
													new Example()
														.value(
															"{\"error\": \"Acceso prohibido\", \"mensaje\":"
																+ " \"No tienes permisos para esta"
																+ " operación\"}")))))
						.addResponses(
							"NotFoundError",
							new ApiResponse()
								.description("Recurso no encontrado")
								.content(
									new Content()
										.addMediaType(
											"application/json",
											new MediaType()
												.addExamples(
													"error",
													new Example()
														.value(
															"{\"error\": \"No encontrado\", \"mensaje\":"
																+ " \"El recurso solicitado no"
																+ " existe\"}"))))));
	}
}
