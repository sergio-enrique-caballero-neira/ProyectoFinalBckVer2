package co.edu.unbosque.proyectoFinal.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.proyectoFinal.entity.Administrador;
import co.edu.unbosque.proyectoFinal.entity.Usuario;
import co.edu.unbosque.proyectoFinal.repository.AdministradorRepository;
import co.edu.unbosque.proyectoFinal.repository.UsuarioRepository;

/**
 * Configuracion para la precarga de datos iniciales en la base de datos.
 * Crea cuentas predeterminadas para el administrador y un usuario regular
 * al iniciar la aplicacion si no existen previamente.
 */
@Configuration
public class LoadDatabase {

	/** Logger de la clase. */
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	/**
	 * Bean que ejecuta la inicializacion de la base de datos al arranque.
	 * @param adminRepo repositorio de administradores
	 * @param usuarioRepo repositorio de usuarios
	 * @param passwordEncoder codificador de contraseñas
	 * @return CommandLineRunner que ejecuta la logica de precarga
	 */
	@Bean
	CommandLineRunner initDatabase(AdministradorRepository adminRepo, UsuarioRepository usuarioRepo,
			PasswordEncoder passwordEncoder) {
		return args -> {

			boolean adminExiste = false;
			for (Administrador a : adminRepo.findAll()) {
				if (a.getNombre().equals("admin")) {
					adminExiste = true;
					break;
				}
			}

			if (adminExiste) {
				log.info("El administrador ya existe, omitiendo creación...");
			} else {
				Administrador adminUser = new Administrador("admin", "admin@proyecto.com", "3001234567",
						passwordEncoder.encode("Admin123!"), "Administrador General");
				adminRepo.save(adminUser);
				log.info("Administrador creado: nombre: admin | contrasena: Admin123!");
			}

			boolean usuarioExiste = false;
			for (Usuario u : usuarioRepo.findAll()) {
				if (u.getNombre().equals("usuario1")) {
					usuarioExiste = true;
					break;
				}
			}

			if (usuarioExiste) {
				log.info("El usuario normal ya existe, omitiendo creación...");
			} else {
				Usuario normalUser = new Usuario("usuario1", "usuario1@proyecto.com", "3009876543",
						passwordEncoder.encode("Usuario1*"));
				usuarioRepo.save(normalUser);
				log.info("Usuario creado: nombre: usuario1 | contrasena: Usuario1*");
			}
		};
	}
}
