/**
 * Paquete de excepciones personalizadas y manejador global de errores.
 *
 * <p>Define las excepciones del dominio de negocio de la aplicación y el
 * componente que las intercepta para producir respuestas HTTP coherentes.</p>
 *
 * <p><strong>Excepciones disponibles:</strong></p>
 * <ul>
 *   <li>{@link co.edu.unbosque.proyectoFinal.exception.BadRequestException} –
 *       Solicitud inválida por parte del cliente (400 Bad Request).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.exception.ResourceNotFoundException} –
 *       Recurso no encontrado en el sistema (404 Not Found).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.exception.AlreadySubmittedException} –
 *       El archivo ya fue enviado previamente a VirusTotal (409 Conflict).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.exception.FileEmpyException} –
 *       El archivo enviado está vacío (400 Bad Request).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.exception.QueueException} –
 *       El análisis de VirusTotal aún está en cola (400 Bad Request).</li>
 *   <li>{@link co.edu.unbosque.proyectoFinal.exception.GlobalExceptionHandler} –
 *       Clase anotada con {@code @RestControllerAdvice} que centraliza el manejo
 *       de todas las excepciones anteriores, además de
 *       {@code MaxUploadSizeExceededException} e {@code IllegalStateException},
 *       devolviendo respuestas HTTP con el código de estado apropiado.</li>
 * </ul>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 */
package co.edu.unbosque.proyectoFinal.exception;
