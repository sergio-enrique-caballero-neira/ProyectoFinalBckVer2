/**
 * Paquete de servicios de lógica de negocio.
 *
 * <p>Contiene la capa de servicio de la aplicación, responsable de orquestar
 * las operaciones de negocio, aplicar validaciones, cifrar contraseñas y
 * comunicarse con los repositorios y APIs externas.</p>
 *
 * <p><strong>Interfaz base:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.service.CRUDoperation} – Interfaz
 *       genérica parametrizada que define los seis métodos estándar de negocio:
 *       {@code create}, {@code getAll}, {@code deleteByID}, {@code updateByID},
 *       {@code count} y {@code exist}. Garantiza un contrato uniforme entre
 *       todos los servicios.</li>
 * </ul>
 *
 * <p><strong>Servicios disponibles:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.service.UsuarioService} –
 *       Gestión completa de usuarios normales. Implementa {@code CRUDoperation<UsuarioDTO>}
 *       y añade operaciones para el historial de análisis VirusTotal:
 *       {@code agregarDatoHistorial}, {@code actulizarDatoHistorial} y
 *       {@code getHistorialById}. Valida nombre (alfanumérico, 6-50 chars),
 *       contraseña (mayúsculas, minúsculas, números y símbolo especial, 8-64 chars),
 *       email y teléfono (10 dígitos) antes de persistir.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.service.AdministradorService} –
 *       Gestión completa de administradores. Implementa
 *       {@code CRUDoperation<AdministradorDTO>} con las mismas validaciones que
 *       {@code UsuarioService} más la validación del campo {@code cargo}
 *       (3-50 caracteres).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.service.VirustotalService} –
 *       Integración con la API REST de VirusTotal v3. Permite subir archivos
 *       (hasta 32 MB) para análisis antivirus y consultar el resultado usando
 *       el ID del análisis. Calcula el hash SHA-256 del archivo para informar
 *       al usuario si el archivo ya fue enviado previamente.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.service;
