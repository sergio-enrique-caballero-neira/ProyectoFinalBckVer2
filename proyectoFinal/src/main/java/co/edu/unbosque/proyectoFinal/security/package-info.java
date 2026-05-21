/**
 * Paquete de seguridad de la aplicación.
 *
 * <p>Implementa la autenticación y autorización basada en
 * <strong>JWT (JSON Web Token)</strong> sobre Spring Security.
 * La arquitectura es <em>stateless</em>: no se crean sesiones HTTP en el
 * servidor; cada petición debe transportar su propio token.</p>
 *
 * <p><strong>Clases del paquete:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.security.JwtUtil} –
 *       Componente utilitario para generar, validar y extraer información de los
 *       tokens JWT. Firma los tokens con HMAC-SHA256 usando la clave definida en
 *       la propiedad {@code jwt.secret}. La vigencia del token es de 24 horas.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.security.JwtAuthenticationFilter} –
 *       Filtro de Spring Security ({@code OncePerRequestFilter}) que intercepta
 *       cada solicitud HTTP, extrae el token del encabezado
 *       {@code Authorization: Bearer <token>}, lo valida y establece el contexto
 *       de autenticación si el token es válido.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.security.UserDetailsServiceImpl} –
 *       Implementación de {@code UserDetailsService} que carga el usuario
 *       (primero busca en {@code usuarios}, luego en {@code administradores})
 *       para ser usado durante la autenticación.</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.security.SecurityConfig} –
 *       Configuración central de Spring Security. Define las reglas de
 *       autorización por rol y ruta, registra el filtro JWT, configura el
 *       {@code AuthenticationProvider} con BCrypt y establece la política de
 *       sesión {@code STATELESS}.</li>
 * </ul>
 *
 * <p><strong>Matriz de permisos de rutas:</strong></p>
 * <pre>
 * /auth/**                       → Público
 * /swagger-ui/**, /v3/api-docs/** → Público
 * /usuario/crear                 → Público
 * /usuario/mostrartodo           → USUARIO, ADMIN
 * /usuario/getIdByUsername       → USUARIO, ADMIN
 * /usuario/getHistorialById      → USUARIO, ADMIN
 * /usuario/**                    → ADMIN
 * /administrador/**              → ADMIN
 * /virustotal/**                 → USUARIO, ADMIN
 * </pre>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.security;
