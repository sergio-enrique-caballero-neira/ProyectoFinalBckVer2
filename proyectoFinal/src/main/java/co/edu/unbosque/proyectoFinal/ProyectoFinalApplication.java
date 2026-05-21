package co.edu.unbosque.proyectoFinal;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Clase principal de arranque de la aplicación Spring Boot <strong>ProyectoFinal</strong>.
 *
 * <p>Esta clase es el punto de entrada de la aplicación. La anotación
 * {@code @SpringBootApplication} combina {@code @Configuration},
 * {@code @EnableAutoConfiguration} y {@code @ComponentScan}, activando
 * la configuración automática de Spring Boot y el escaneo de componentes
 * en el paquete raíz y sus subpaquetes.</p>
 *
 * <p>Además registra el bean global de {@link ModelMapper}, utilizado en
 * la capa de servicio para convertir entre entidades JPA y DTOs.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
@SpringBootApplication
public class ProyectoFinalApplication {

	/**
	 * Método principal que inicia el contenedor de Spring Boot.
	 *
	 * @param args argumentos de línea de comandos (no utilizados)
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalApplication.class, args);
	}

	/**
	 * Registra un bean de {@link ModelMapper} disponible en todo el contexto
	 * de la aplicación para el mapeo automático entre objetos (entity ↔ DTO).
	 *
	 * @return nueva instancia de {@code ModelMapper} con configuración predeterminada
	 */
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
