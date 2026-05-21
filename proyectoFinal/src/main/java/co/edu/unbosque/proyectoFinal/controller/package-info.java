/**
 * Paquete de controladores REST de la aplicación.
 *
 * <p>Cada controlador define un grupo de endpoints HTTP bajo una ruta base
 * y delega la lógica de negocio a la capa de servicio correspondiente.
 * Todos los controladores protegidos requieren autenticación JWT mediante
 * el encabezado {@code Authorization: Bearer <token>}.</p>
 *
 * <p><strong>Controladores disponibles:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.controller.AuthController} –
 *       {@code /auth} – Inicio de sesión unificado para usuarios y administradores.
 *       Devuelve un token JWT y el rol del usuario autenticado.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.controller.UsuarioController} –
 *       {@code /usuario} – CRUD de usuarios normales y consulta de historial
 *       de análisis VirusTotal. Requiere rol {@code ADMIN} para crear, actualizar
 *       y eliminar; roles {@code USUARIO} o {@code ADMIN} para consultar.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.controller.AdministradorController} –
 *       {@code /administrador} – CRUD de administradores. Exclusivo para rol
 *       {@code ADMIN}.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.controller.VirusTotalApiController} –
 *       {@code /virustotal} – Subida de archivos a VirusTotal y consulta del
 *       resultado del análisis. Accesible para roles {@code USUARIO} y {@code ADMIN}.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.controller;
