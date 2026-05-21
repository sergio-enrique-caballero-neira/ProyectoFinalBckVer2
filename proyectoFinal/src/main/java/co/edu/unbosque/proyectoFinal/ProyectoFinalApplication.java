package co.edu.unbosque.proyectoFinal;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Clase principal de la aplicacion Spring Boot.
 * Punto de entrada para la ejecucion de la aplicacion y configuracion de beans globales.
 */
@SpringBootApplication
public class ProyectoFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalApplication.class, args);
	}
	
	/**
	 * Configura un bean ModelMapper para la conversion entre entidades y DTOs.
	 * @return instancia de ModelMapper
	 */
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
