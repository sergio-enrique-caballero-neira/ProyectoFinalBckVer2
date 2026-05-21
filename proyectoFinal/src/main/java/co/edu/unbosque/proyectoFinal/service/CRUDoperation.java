package co.edu.unbosque.proyectoFinal.service;

import java.util.List;

/**
 * Interfaz genérica que define el contrato base de operaciones CRUD para los
 * servicios de la aplicación.
 *
 * <p>Parametrizada con el tipo {@code T} del DTO que opera cada servicio, garantiza
 * un contrato uniforme entre todos los servicios de negocio. Cada servicio que la
 * implementa debe proporcionar comportamiento concreto para las seis operaciones
 * declaradas.</p>
 *
 * <p><strong>Servicios que la implementan:</strong></p>
 * <ul>
 *   <li>{@link UsuarioService}       – con {@code T = UsuarioDTO}</li>
 *   <li>{@link AdministradorService} – con {@code T = AdministradorDTO}</li>
 * </ul>
 *
 * @param <T> tipo del DTO sobre el que operan los métodos CRUD
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
public interface CRUDoperation<T> {

    /**
     * Crea y persiste un nuevo registro a partir del DTO proporcionado.
     *
     * @param dato DTO con los datos del nuevo registro; no puede ser {@code null}
     */
    void create(T dato);

    /**
     * Retorna la lista de todos los registros persistidos.
     *
     * @return lista de DTOs; nunca {@code null}, puede estar vacía
     */
    List<T> getAll();

    /**
     * Elimina el registro identificado por {@code id}.
     *
     * @param id identificador único del registro a eliminar
     */
    void deleteByID(long id);

    /**
     * Actualiza el registro identificado por {@code id} con los datos del DTO.
     *
     * @param id   identificador único del registro a actualizar
     * @param dato DTO con los nuevos valores; no puede ser {@code null}
     */
    void updateByID(long id, T dato);

    /**
     * Retorna el número total de registros persistidos.
     *
     * @return cantidad de registros en la base de datos
     */
    int count();

    /**
     * Comprueba si existe un registro con el identificador indicado.
     *
     * @param id identificador único a comprobar
     * @return {@code true} si existe un registro con ese ID; {@code false} en caso contrario
     */
    boolean exist(long id);
}
