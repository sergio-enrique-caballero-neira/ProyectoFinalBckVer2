package co.edu.unbosque.proyectoFinal;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Inicializador de servlet para despliegue de la aplicación en un contenedor
 * externo (Tomcat, Wildfly, etc.) como un archivo WAR.
 *
 * <p>Extiende {@link SpringBootServletInitializer} y sobreescribe el método
 * {@link #configure(SpringApplicationBuilder)} para indicar a Spring Boot
 * cuál es la clase de aplicación principal cuando se ejecuta dentro de un
 * contenedor de servlets externo, en lugar del servidor embebido de Spring Boot.</p>
 *
 * <p>Cuando la aplicación se ejecuta directamente con {@code java -jar},
 * esta clase no interviene; solo es relevante en despliegues tradicionales
 * sobre un servidor de aplicaciones.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see ProyectoFinalApplication
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Configura la fuente de la aplicación Spring Boot para el contenedor externo.
	 *
	 * @param application constructor de la aplicación Spring proporcionado por el contenedor
	 * @return el mismo builder con {@link ProyectoFinalApplication} como fuente principal
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ProyectoFinalApplication.class);
	}

}
