package co.edu.unbosque.proyectoFinal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectoFinal.dto.AdministradorDTO;
import co.edu.unbosque.proyectoFinal.entity.Administrador;
import co.edu.unbosque.proyectoFinal.exception.BadRequestException;
import co.edu.unbosque.proyectoFinal.exception.ResourceNotFoundException;
import co.edu.unbosque.proyectoFinal.repository.AdministradorRepository;

/**
 * Servicio de lógica de negocio para la gestión de administradores del sistema.
 *
 * <p>Implementa {@link CRUDoperation}{@code <AdministradorDTO>} y añade las
 * validaciones específicas del dominio antes de persistir o actualizar un
 * administrador. Utiliza {@link ModelMapper} para la conversión bidireccional
 * entre entidades {@link Administrador} y DTOs {@link AdministradorDTO}.</p>
 *
 * <p><strong>Validaciones aplicadas en {@code create} y {@code updateByID}:</strong></p>
 * <ul>
 *   <li>El DTO no puede ser {@code null}.</li>
 *   <li><strong>Nombre</strong>: no nulo, 6-50 caracteres alfanuméricos
 *       ({@code [a-zA-Z0-9]+}), no duplicado en la base de datos.</li>
 *   <li><strong>Email</strong>: no nulo, formato válido con {@code @} y dominio,
 *       no duplicado en la base de datos.</li>
 *   <li><strong>Teléfono</strong>: no nulo, exactamente 10 dígitos numéricos,
 *       no duplicado en la base de datos.</li>
 *   <li><strong>Contraseña</strong>: 8-64 caracteres, al menos una mayúscula,
 *       una minúscula, un dígito y un carácter especial
 *       ({@code (?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&...])}).</li>
 *   <li><strong>Cargo</strong>: no nulo, 3-50 caracteres.</li>
 * </ul>
 *
 * <p>Las contraseñas se almacenan siempre cifradas con BCrypt.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see CRUDoperation
 * @see AdministradorRepository
 */
@Service
public class AdministradorService implements CRUDoperation<AdministradorDTO> {

    /** Repositorio de acceso a datos de administradores. */
    @Autowired
    private AdministradorRepository administradorRepo;

    /** Mapeador de objetos para convertir entre entidades y DTOs. */
    @Autowired
    private ModelMapper modelMapper;

    /** Codificador BCrypt para cifrar las contraseñas antes de persistirlas. */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** Constructor vacío requerido por Spring. */
    public AdministradorService() {}

    /**
     * Valida y crea un nuevo administrador en el sistema.
     *
     * <p>Aplica todas las validaciones del dominio y cifra la contraseña con
     * BCrypt antes de persistir. Si cualquier validación falla, lanza
     * {@link BadRequestException} con un mensaje descriptivo.</p>
     *
     * @param dato DTO con los datos del nuevo administrador
     * @throws BadRequestException si algún campo es inválido o el nombre,
     *         email o teléfono ya están registrados
     */
    @Override
    public void create(AdministradorDTO dato) {
        if (dato == null) throw new BadRequestException("El dato no puede ser nulo");

        validarNombre(dato.getNombre(), null);
        validarEmail(dato.getEmail(), null);
        validarTelefono(dato.getTelefono(), null);
        validarContrasena(dato.getContrasena());
        validarCargo(dato.getCargo());

        Administrador admin = modelMapper.map(dato, Administrador.class);
        admin.setContrasena(passwordEncoder.encode(dato.getContrasena()));
        admin.setRole(co.edu.unbosque.proyectoFinal.entity.Persona.Role.ADMIN);
        administradorRepo.save(admin);
    }

    /**
     * Retorna la lista de todos los administradores registrados en el sistema.
     *
     * @return lista de {@link AdministradorDTO}; nunca {@code null}
     * @throws BadRequestException si no hay ningún administrador registrado
     */
    @Override
    public List<AdministradorDTO> getAll() {
        List<Administrador> admins = administradorRepo.findAll();
        if (admins.isEmpty()) throw new BadRequestException("No hay administradores registrados");
        List<AdministradorDTO> dtos = new ArrayList<>();
        for (Administrador a : admins) {
            dtos.add(modelMapper.map(a, AdministradorDTO.class));
        }
        return dtos;
    }

    /**
     * Elimina un administrador del sistema por su ID.
     *
     * @param id identificador único del administrador a eliminar
     * @throws BadRequestException       si el ID es nulo o negativo
     * @throws ResourceNotFoundException si no existe ningún administrador con ese ID
     */
    @Override
    public void deleteByID(long id) {
        if (id <= 0) throw new BadRequestException("El ID no puede ser nulo o negativo");
        if (!administradorRepo.existsById(id))
            throw new ResourceNotFoundException("No existe administrador con ID: " + id);
        administradorRepo.deleteById(id);
    }

    /**
     * Actualiza todos los campos de un administrador existente identificado por su ID.
     *
     * <p>Aplica las mismas validaciones que {@link #create(AdministradorDTO)},
     * excluyendo de la validación de unicidad el propio registro (para permitir
     * mantener los mismos valores). Cifra la nueva contraseña con BCrypt.</p>
     *
     * @param id   identificador único del administrador a actualizar
     * @param dato DTO con los nuevos valores
     * @throws BadRequestException       si el ID es nulo/negativo o algún campo es inválido
     * @throws ResourceNotFoundException si no existe ningún administrador con ese ID
     */
    @Override
    public void updateByID(long id, AdministradorDTO dato) {
        if (id <= 0) throw new BadRequestException("El ID no puede ser nulo o negativo");
        Optional<Administrador> opt = administradorRepo.findById(id);
        if (opt.isEmpty())
            throw new ResourceNotFoundException("No existe administrador con ID: " + id);

        Administrador existing = opt.get();
        validarNombre(dato.getNombre(), existing.getNombre());
        validarEmail(dato.getEmail(), existing.getEmail());
        validarTelefono(dato.getTelefono(), existing.getTelefono());
        validarContrasena(dato.getContrasena());
        validarCargo(dato.getCargo());

        existing.setNombre(dato.getNombre());
        existing.setEmail(dato.getEmail());
        existing.setTelefono(dato.getTelefono());
        existing.setContrasena(passwordEncoder.encode(dato.getContrasena()));
        existing.setCargo(dato.getCargo());
        administradorRepo.save(existing);
    }

    /**
     * Retorna el número total de administradores registrados.
     *
     * @return cantidad de administradores en la base de datos
     */
    @Override
    public int count() {
        return (int) administradorRepo.count();
    }

    /**
     * Comprueba si existe un administrador con el ID indicado.
     *
     * @param id identificador único a comprobar
     * @return {@code true} si existe; {@code false} en caso contrario
     */
    @Override
    public boolean exist(long id) {
        return administradorRepo.existsById(id);
    }

    // ─── Métodos privados de validación ───────────────────────────────────────

    /**
     * Valida el nombre del administrador: no nulo, 6-50 chars alfanuméricos, único en la BD
     * (excepto si coincide con {@code propioNombre}, para permitir actualizaciones sin cambio).
     *
     * @param nombre      nombre a validar
     * @param propioNombre nombre actual del registro (puede ser {@code null} en creación)
     */
    private void validarNombre(String nombre, String propioNombre) {
        if (nombre == null || nombre.isBlank())
            throw new BadRequestException("El nombre no puede ser nulo o vacío");
        if (!nombre.matches("[a-zA-Z0-9]{6,50}"))
            throw new BadRequestException("El nombre debe tener entre 6 y 50 caracteres alfanuméricos");
        for (Administrador a : administradorRepo.findAll()) {
            if (a.getNombre().equals(nombre) && !nombre.equals(propioNombre))
                throw new BadRequestException("El nombre ya está registrado: " + nombre);
        }
    }

    /**
     * Valida el email del administrador: no nulo, formato válido, único en la BD
     * (excepto si coincide con {@code propioEmail}).
     *
     * @param email      email a validar
     * @param propioEmail email actual del registro (puede ser {@code null} en creación)
     */
    private void validarEmail(String email, String propioEmail) {
        if (email == null || email.isBlank())
            throw new BadRequestException("El email no puede ser nulo o vacío");
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new BadRequestException("El email no tiene un formato válido");
        for (Administrador a : administradorRepo.findAll()) {
            if (a.getEmail().equals(email) && !email.equals(propioEmail))
                throw new BadRequestException("El email ya está registrado: " + email);
        }
    }

    /**
     * Valida el teléfono del administrador: no nulo, exactamente 10 dígitos, único en la BD
     * (excepto si coincide con {@code propioTelefono}).
     *
     * @param telefono      teléfono a validar
     * @param propioTelefono teléfono actual del registro (puede ser {@code null} en creación)
     */
    private void validarTelefono(String telefono, String propioTelefono) {
        if (telefono == null || telefono.isBlank())
            throw new BadRequestException("El teléfono no puede ser nulo o vacío");
        if (!telefono.matches("\\d{10}"))
            throw new BadRequestException("El teléfono debe tener exactamente 10 dígitos");
        for (Administrador a : administradorRepo.findAll()) {
            if (a.getTelefono().equals(telefono) && !telefono.equals(propioTelefono))
                throw new BadRequestException("El teléfono ya está registrado: " + telefono);
        }
    }

    /**
     * Valida la contraseña: 8-64 chars, con al menos una mayúscula, una minúscula,
     * un dígito y un carácter especial.
     *
     * @param contrasena contraseña en texto plano a validar
     */
    private void validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.isBlank())
            throw new BadRequestException("La contraseña no puede ser nula o vacía");
        if (!contrasena.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_\\-#])[A-Za-z\\d@$!%*?&_\\-#]{8,64}$"))
            throw new BadRequestException(
                "La contraseña debe tener entre 8 y 64 caracteres, incluir al menos una mayúscula, "
                + "una minúscula, un número y un carácter especial (@$!%*?&_-#)");
    }

    /**
     * Valida el cargo del administrador: no nulo, entre 3 y 50 caracteres.
     *
     * @param cargo cargo a validar
     */
    private void validarCargo(String cargo) {
        if (cargo == null || cargo.isBlank())
            throw new BadRequestException("El cargo no puede ser nulo o vacío");
        if (cargo.length() < 3 || cargo.length() > 50)
            throw new BadRequestException("El cargo debe tener entre 3 y 50 caracteres");
    }
}
