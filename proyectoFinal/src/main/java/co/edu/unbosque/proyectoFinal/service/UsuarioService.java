package co.edu.unbosque.proyectoFinal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectoFinal.dto.AttributesDTO;
import co.edu.unbosque.proyectoFinal.dto.DataDTO;
import co.edu.unbosque.proyectoFinal.dto.EngineResultDTO;
import co.edu.unbosque.proyectoFinal.dto.UsuarioDTO;
import co.edu.unbosque.proyectoFinal.dto.VirusTotalUploadResponseDTO;
import co.edu.unbosque.proyectoFinal.entity.Attributes;
import co.edu.unbosque.proyectoFinal.entity.Data;
import co.edu.unbosque.proyectoFinal.entity.EngineResult;
import co.edu.unbosque.proyectoFinal.entity.Usuario;
import co.edu.unbosque.proyectoFinal.entity.VirusTotalUploadResponse;
import co.edu.unbosque.proyectoFinal.exception.BadRequestException;
import co.edu.unbosque.proyectoFinal.exception.ResourceNotFoundException;
import co.edu.unbosque.proyectoFinal.repository.UsuarioRepository;

/**
 * Servicio de lógica de negocio para la gestión de usuarios normales del sistema.
 *
 * <p>Implementa {@link CRUDoperation}{@code <UsuarioDTO>} y añade operaciones
 * específicas para el manejo del historial de análisis VirusTotal asociado a
 * cada usuario. Utiliza {@link ModelMapper} para conversión entre entidades y DTOs.</p>
 *
 * <p><strong>Validaciones aplicadas en {@code create} y {@code updateByID}:</strong></p>
 * <ul>
 *   <li>El DTO no puede ser {@code null}.</li>
 *   <li><strong>Nombre</strong>: no nulo, 6-50 caracteres alfanuméricos, único en la BD.</li>
 *   <li><strong>Email</strong>: no nulo, formato válido con {@code @} y dominio, único en la BD.</li>
 *   <li><strong>Teléfono</strong>: no nulo, exactamente 10 dígitos, único en la BD.</li>
 *   <li><strong>Contraseña</strong>: 8-64 caracteres con al menos una mayúscula, una minúscula,
 *       un dígito y un carácter especial.</li>
 * </ul>
 *
 * <p><strong>Gestión del historial VirusTotal:</strong></p>
 * <ul>
 *   <li>{@link #agregarDatoHistorial(long, VirusTotalUploadResponseDTO)} – Agrega una nueva
 *       entrada al historial del usuario (respuesta preliminar tras subir el archivo).</li>
 *   <li>{@link #actulizarDatoHistorial(long, VirusTotalUploadResponseDTO, String)} – Actualiza
 *       una entrada existente con los resultados completos del análisis.</li>
 *   <li>{@link #getHistorialById(long)} – Retorna el historial completo de un usuario.</li>
 *   <li>{@link #getIdByUsername(String)} – Retorna el ID numérico dado el nombre de usuario.</li>
 * </ul>
 *
 * <p>Las contraseñas se almacenan siempre cifradas con BCrypt.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see CRUDoperation
 * @see UsuarioRepository
 */
@Service
public class UsuarioService implements CRUDoperation<UsuarioDTO> {

    /** Repositorio de acceso a datos de usuarios. */
    @Autowired
    private UsuarioRepository usuarioRepo;

    /** Mapeador de objetos para conversión entre entidades y DTOs. */
    @Autowired
    private ModelMapper modelMapper;

    /** Codificador BCrypt para cifrar contraseñas antes de persistirlas. */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** Constructor vacío requerido por Spring. */
    public UsuarioService() {}

    /**
     * Valida y crea un nuevo usuario en el sistema.
     *
     * <p>Aplica todas las validaciones del dominio, cifra la contraseña con BCrypt
     * e inicializa el historial de análisis como lista vacía antes de persistir.
     * Si cualquier validación falla, lanza {@link BadRequestException}.</p>
     *
     * @param dato DTO con los datos del nuevo usuario
     * @throws BadRequestException si algún campo es inválido o el nombre,
     *         email o teléfono ya están registrados
     */
    @Override
    public void create(UsuarioDTO dato) {
        if (dato == null) throw new BadRequestException("El dato no puede ser nulo");

        validarNombre(dato.getNombre(), null);
        validarEmail(dato.getEmail(), null);
        validarTelefono(dato.getTelefono(), null);
        validarContrasena(dato.getContrasena());

        Usuario usuario = new Usuario(
            dato.getNombre(),
            dato.getEmail(),
            dato.getTelefono(),
            passwordEncoder.encode(dato.getContrasena())
        );
        usuarioRepo.save(usuario);
    }

    /**
     * Retorna la lista de todos los usuarios normales registrados en el sistema.
     *
     * @return lista de {@link UsuarioDTO}; nunca {@code null}
     * @throws BadRequestException si no hay ningún usuario registrado
     */
    @Override
    public List<UsuarioDTO> getAll() {
        List<Usuario> usuarios = usuarioRepo.findAll();
        if (usuarios.isEmpty()) throw new BadRequestException("No hay usuarios registrados");
        List<UsuarioDTO> dtos = new ArrayList<>();
        for (Usuario u : usuarios) {
            dtos.add(modelMapper.map(u, UsuarioDTO.class));
        }
        return dtos;
    }

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * <p>La eliminación es en cascada: también se eliminan todos los registros
     * del historial VirusTotal asociados al usuario.</p>
     *
     * @param id identificador único del usuario a eliminar
     * @throws BadRequestException       si el ID es nulo o negativo
     * @throws ResourceNotFoundException si no existe ningún usuario con ese ID
     */
    @Override
    public void deleteByID(long id) {
        if (id <= 0) throw new BadRequestException("El ID no puede ser nulo o negativo");
        if (!usuarioRepo.existsById(id))
            throw new ResourceNotFoundException("No existe usuario con ID: " + id);
        usuarioRepo.deleteById(id);
    }

    /**
     * Actualiza todos los campos de un usuario existente identificado por su ID.
     *
     * <p>Aplica las mismas validaciones que {@link #create(UsuarioDTO)}, excluyendo
     * de la validación de unicidad el propio registro. Cifra la nueva contraseña con BCrypt.
     * El historial de análisis no se modifica en esta operación.</p>
     *
     * @param id   identificador único del usuario a actualizar
     * @param dato DTO con los nuevos valores
     * @throws BadRequestException       si el ID es nulo/negativo o algún campo es inválido
     * @throws ResourceNotFoundException si no existe ningún usuario con ese ID
     */
    @Override
    public void updateByID(long id, UsuarioDTO dato) {
        if (id <= 0) throw new BadRequestException("El ID no puede ser nulo o negativo");
        Optional<Usuario> opt = usuarioRepo.findById(id);
        if (opt.isEmpty())
            throw new ResourceNotFoundException("No existe usuario con ID: " + id);

        Usuario existing = opt.get();
        validarNombre(dato.getNombre(), existing.getNombre());
        validarEmail(dato.getEmail(), existing.getEmail());
        validarTelefono(dato.getTelefono(), existing.getTelefono());
        validarContrasena(dato.getContrasena());

        existing.setNombre(dato.getNombre());
        existing.setEmail(dato.getEmail());
        existing.setTelefono(dato.getTelefono());
        existing.setContrasena(passwordEncoder.encode(dato.getContrasena()));
        usuarioRepo.save(existing);
    }

    /**
     * Retorna el número total de usuarios registrados.
     *
     * @return cantidad de usuarios en la base de datos
     */
    @Override
    public int count() {
        return (int) usuarioRepo.count();
    }

    /**
     * Comprueba si existe un usuario con el ID indicado.
     *
     * @param id identificador único a comprobar
     * @return {@code true} si existe; {@code false} en caso contrario
     */
    @Override
    public boolean exist(long id) {
        return usuarioRepo.existsById(id);
    }

    // ─── Operaciones del historial VirusTotal ────────────────────────────────

    /**
     * Retorna el ID numérico de un usuario dado su nombre de usuario.
     *
     * <p>Utilizado por el frontend para obtener el ID del usuario autenticado
     * inmediatamente después del login, antes de realizar operaciones de historial.</p>
     *
     * @param nombre nombre de usuario a buscar
     * @return ID numérico del usuario
     * @throws ResourceNotFoundException si no existe ningún usuario con ese nombre
     */
    public Long getIdByUsername(String nombre) {
        Optional<Usuario> opt = usuarioRepo.findByNombre(nombre);
        if (opt.isEmpty())
            throw new ResourceNotFoundException("No existe usuario con nombre: " + nombre);
        return opt.get().getId();
    }

    /**
     * Agrega una nueva entrada al historial de análisis VirusTotal del usuario.
     *
     * <p>Mapea el DTO recibido ({@link VirusTotalUploadResponseDTO}) a su entidad
     * JPA equivalente ({@link VirusTotalUploadResponse}) y lo añade al historial
     * del usuario antes de persistir. Esta operación se llama justo después de
     * subir un archivo a VirusTotal, cuando el análisis aún no tiene resultados.</p>
     *
     * @param id       identificador único del usuario al que añadir el historial
     * @param response DTO con la respuesta preliminar de VirusTotal
     * @throws ResourceNotFoundException si no existe ningún usuario con ese ID
     */
    public void agregarDatoHistorial(long id, VirusTotalUploadResponseDTO response) {
        Optional<Usuario> opt = usuarioRepo.findById(id);
        if (opt.isEmpty())
            throw new ResourceNotFoundException("No existe usuario con ID: " + id);

        Usuario usuario = opt.get();
        VirusTotalUploadResponse entity = mapResponseDtoToEntity(response);
        usuario.getHistorial().add(entity);
        usuarioRepo.save(usuario);
    }

    /**
     * Actualiza una entrada existente del historial de un usuario con los resultados
     * completos del análisis VirusTotal.
     *
     * <p>Busca en el historial del usuario la entrada cuyo {@code data.id} coincide
     * con {@code analysisId} y reemplaza su objeto {@code data} con los datos
     * actualizados del DTO. Si no se encuentra la entrada, no realiza ninguna acción.</p>
     *
     * @param id         identificador único del usuario dueño del historial
     * @param response   DTO con los resultados completos del análisis
     * @param analysisId ID del análisis en VirusTotal a actualizar
     * @throws ResourceNotFoundException si no existe ningún usuario con ese ID
     */
    public void actulizarDatoHistorial(long id, VirusTotalUploadResponseDTO response, String analysisId) {
        Optional<Usuario> opt = usuarioRepo.findById(id);
        if (opt.isEmpty())
            throw new ResourceNotFoundException("No existe usuario con ID: " + id);

        Usuario usuario = opt.get();
        for (VirusTotalUploadResponse entry : usuario.getHistorial()) {
            if (entry.getData() != null && analysisId.equals(entry.getData().getId())) {
                entry.setData(mapDataDtoToEntity(response.getData()));
                break;
            }
        }
        usuarioRepo.save(usuario);
    }

    /**
     * Retorna el historial completo de análisis VirusTotal de un usuario.
     *
     * @param id identificador único del usuario
     * @return lista de {@link VirusTotalUploadResponseDTO} con el historial del usuario
     * @throws ResourceNotFoundException si no existe ningún usuario con ese ID
     */
    public List<VirusTotalUploadResponseDTO> getHistorialById(long id) {
        Optional<Usuario> opt = usuarioRepo.findById(id);
        if (opt.isEmpty())
            throw new ResourceNotFoundException("No existe usuario con ID: " + id);

        List<VirusTotalUploadResponseDTO> result = new ArrayList<>();
        for (VirusTotalUploadResponse entry : opt.get().getHistorial()) {
            result.add(mapResponseEntityToDto(entry));
        }
        return result;
    }

    // ─── Métodos privados de mapeo DTO ↔ Entidad ────────────────────────────

    /**
     * Convierte un {@link VirusTotalUploadResponseDTO} a su entidad JPA equivalente.
     *
     * @param dto DTO a convertir
     * @return entidad {@link VirusTotalUploadResponse} lista para persistir
     */
    private VirusTotalUploadResponse mapResponseDtoToEntity(VirusTotalUploadResponseDTO dto) {
        VirusTotalUploadResponse entity = new VirusTotalUploadResponse();
        if (dto.getData() != null) {
            entity.setData(mapDataDtoToEntity(dto.getData()));
        }
        return entity;
    }

    /**
     * Convierte un {@link DataDTO} a su entidad JPA {@link Data}.
     *
     * @param dto DTO a convertir
     * @return entidad {@link Data} lista para persistir
     */
    private Data mapDataDtoToEntity(DataDTO dto) {
        Data data = new Data();
        data.setId(dto.getId());
        data.setType(dto.getType());
        data.setNombreArchivo(dto.getNombreArchivo());
        if (dto.getAttributes() != null) {
            data.setAttributes(mapAttributesDtoToEntity(dto.getAttributes()));
        }
        return data;
    }

    /**
     * Convierte un {@link AttributesDTO} a su entidad JPA {@link Attributes}.
     *
     * @param dto DTO a convertir
     * @return entidad {@link Attributes} lista para persistir
     */
    private Attributes mapAttributesDtoToEntity(AttributesDTO dto) {
        Attributes attrs = new Attributes();
        attrs.setStatus(dto.getStatus());
        if (dto.getStats() != null) {
            attrs.setStats(modelMapper.map(dto.getStats(),
                co.edu.unbosque.proyectoFinal.entity.Stats.class));
        }
        if (dto.getResults() != null) {
            java.util.Map<String, EngineResult> resultsMap = new java.util.HashMap<>();
            for (java.util.Map.Entry<String, EngineResultDTO> entry : dto.getResults().entrySet()) {
                resultsMap.put(entry.getKey(), modelMapper.map(entry.getValue(), EngineResult.class));
            }
            attrs.setResults(resultsMap);
        }
        return attrs;
    }

    /**
     * Convierte una entidad {@link VirusTotalUploadResponse} a su DTO equivalente.
     *
     * @param entity entidad a convertir
     * @return DTO {@link VirusTotalUploadResponseDTO}
     */
    private VirusTotalUploadResponseDTO mapResponseEntityToDto(VirusTotalUploadResponse entity) {
        VirusTotalUploadResponseDTO dto = new VirusTotalUploadResponseDTO();
        dto.setId_Response(entity.getId_Response());
        if (entity.getData() != null) {
            dto.setData(mapDataEntityToDto(entity.getData()));
        }
        return dto;
    }

    /**
     * Convierte una entidad {@link Data} a su DTO equivalente {@link DataDTO}.
     *
     * @param data entidad a convertir
     * @return DTO {@link DataDTO}
     */
    private DataDTO mapDataEntityToDto(Data data) {
        DataDTO dto = new DataDTO();
        dto.setData_id(data.getData_id());
        dto.setId(data.getId());
        dto.setType(data.getType());
        dto.setNombreArchivo(data.getNombreArchivo());
        if (data.getAttributes() != null) {
            dto.setAttributes(mapAttributesEntityToDto(data.getAttributes()));
        }
        return dto;
    }

    /**
     * Convierte una entidad {@link Attributes} a su DTO equivalente {@link AttributesDTO}.
     *
     * @param attrs entidad a convertir
     * @return DTO {@link AttributesDTO}
     */
    private AttributesDTO mapAttributesEntityToDto(Attributes attrs) {
        AttributesDTO dto = new AttributesDTO();
        dto.setId_Attributes(attrs.getId_Attributes());
        dto.setStatus(attrs.getStatus());
        if (attrs.getStats() != null) {
            dto.setStats(modelMapper.map(attrs.getStats(),
                co.edu.unbosque.proyectoFinal.dto.StatsDTO.class));
        }
        if (attrs.getResults() != null) {
            java.util.Map<String, EngineResultDTO> resultsMap = new java.util.HashMap<>();
            for (java.util.Map.Entry<String, EngineResult> entry : attrs.getResults().entrySet()) {
                resultsMap.put(entry.getKey(), modelMapper.map(entry.getValue(), EngineResultDTO.class));
            }
            dto.setResults(resultsMap);
        }
        return dto;
    }

    // ─── Métodos privados de validación ─────────────────────────────────────

    /**
     * Valida el nombre del usuario: no nulo, 6-50 chars alfanuméricos, único en la BD
     * (salvo que coincida con {@code propioNombre}, para permitir actualizaciones sin cambio).
     *
     * @param nombre      nombre a validar
     * @param propioNombre nombre actual del registro ({@code null} en creación)
     */
    private void validarNombre(String nombre, String propioNombre) {
        if (nombre == null || nombre.isBlank())
            throw new BadRequestException("El nombre no puede ser nulo o vacío");
        if (!nombre.matches("[a-zA-Z0-9]{6,50}"))
            throw new BadRequestException("El nombre debe tener entre 6 y 50 caracteres alfanuméricos");
        for (Usuario u : usuarioRepo.findAll()) {
            if (u.getNombre().equals(nombre) && !nombre.equals(propioNombre))
                throw new BadRequestException("El nombre ya está registrado: " + nombre);
        }
    }

    /**
     * Valida el email del usuario: no nulo, formato válido, único en la BD
     * (salvo que coincida con {@code propioEmail}).
     *
     * @param email      email a validar
     * @param propioEmail email actual del registro ({@code null} en creación)
     */
    private void validarEmail(String email, String propioEmail) {
        if (email == null || email.isBlank())
            throw new BadRequestException("El email no puede ser nulo o vacío");
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new BadRequestException("El email no tiene un formato válido");
        for (Usuario u : usuarioRepo.findAll()) {
            if (u.getEmail().equals(email) && !email.equals(propioEmail))
                throw new BadRequestException("El email ya está registrado: " + email);
        }
    }

    /**
     * Valida el teléfono del usuario: no nulo, exactamente 10 dígitos, único en la BD
     * (salvo que coincida con {@code propioTelefono}).
     *
     * @param telefono      teléfono a validar
     * @param propioTelefono teléfono actual del registro ({@code null} en creación)
     */
    private void validarTelefono(String telefono, String propioTelefono) {
        if (telefono == null || telefono.isBlank())
            throw new BadRequestException("El teléfono no puede ser nulo o vacío");
        if (!telefono.matches("\\d{10}"))
            throw new BadRequestException("El teléfono debe tener exactamente 10 dígitos");
        for (Usuario u : usuarioRepo.findAll()) {
            if (u.getTelefono().equals(telefono) && !telefono.equals(propioTelefono))
                throw new BadRequestException("El teléfono ya está registrado: " + telefono);
        }
    }

    /**
     * Valida la contraseña: 8-64 chars con al menos una mayúscula, una minúscula,
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
}
