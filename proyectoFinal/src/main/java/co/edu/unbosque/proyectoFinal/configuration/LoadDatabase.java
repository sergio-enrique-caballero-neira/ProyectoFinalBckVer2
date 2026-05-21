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
 * Configuración de carga inicial de datos en la base de datos.
 *
 * <p>Esta clase se ejecuta automáticamente al arrancar la aplicación gracias a
 * la anotación {@code @Configuration} y al bean de tipo {@link CommandLineRunner}.
 * Su único propósito es garantizar que existan al menos un administrador y un
 * usuario normal de prueba en la base de datos para poder realizar pruebas
 * inmediatamente después del despliegue.</p>
 *
 * <p>La lógica es idempotente: antes de crear cada registro verifica si ya
 * existe por nombre para evitar duplicados en reinicios sucesivos.</p>
 *
 * <p><strong>Credenciales precargadas:</strong></p>
 * <ul>
 *   <li>Administrador → nombre: {@code admin} / contraseña: {@code Admin123!}</li>
 *   <li>Usuario → nombre: {@code usuario1} / contraseña: {@code Usuario1*}</li>
 * </ul>
 *
 * <p>Las contraseñas se almacenan cifradas con BCrypt mediante el
 * {@link PasswordEncoder} inyectado.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see AdministradorRepository
 * @see UsuarioRepository
 */
@Configuration
public class LoadDatabase {

	/** Logger SLF4J para registrar el resultado de la inicialización. */
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	/**
	 * Bean {@link CommandLineRunner} que siembra los datos iniciales al arrancar
	 * la aplicación.
	 *
	 * <p>Primero comprueba si el administrador {@code admin} ya existe; si no,
	 * lo crea con la contraseña cifrada. Luego realiza el mismo proceso para el
	 * usuario {@code usuario1}.</p>
	 *
	 * @param adminRepo       repositorio de administradores
	 * @param usuarioRepo     repositorio de usuarios normales
	 * @param passwordEncoder codificador de contraseñas BCrypt
	 * @return instancia del {@code CommandLineRunner} con la lógica de inicialización
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
