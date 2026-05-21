package co.edu.unbosque.proyectoFinal;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Inicializador del servlet para despliegue como archivo WAR en un contenedor externo.
 * Extiende SpringBootServletInitializer para configurar la aplicacion cuando se ejecuta
 * en un servidor Tomcat embebido o externo.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Configura el builder de la aplicacion Spring para despliegue WAR.
	 * @param application builder de la aplicacion Spring
	 * @return builder configurado con la clase principal de la aplicacion
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ProyectoFinalApplication.class);
	}

}
