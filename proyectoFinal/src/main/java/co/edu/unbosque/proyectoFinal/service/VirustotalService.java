package co.edu.unbosque.proyectoFinal.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import co.edu.unbosque.proyectoFinal.dto.VirusTotalUploadResponseDTO;
import co.edu.unbosque.proyectoFinal.exception.AlreadySubmittedException;
import co.edu.unbosque.proyectoFinal.exception.FileEmpyException;
import co.edu.unbosque.proyectoFinal.exception.QueueException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Servicio de integración con la API REST v3 de VirusTotal.
 *
 * <p>Gestiona la comunicación HTTP con VirusTotal usando la biblioteca
 * {@code OkHttp} para las peticiones y {@code Gson} para deserializar
 * las respuestas JSON. La clave de API se inyecta desde la propiedad
 * {@code virustotal.api.key} en {@code application.properties}.</p>
 *
 * <p><strong>Operaciones disponibles:</strong></p>
 * <ul>
 *   <li>{@link #subirArchivo(MultipartFile)} – Envía un archivo como
 *       {@code multipart/form-data} al endpoint
 *       {@code POST https://www.virustotal.com/api/v3/files}.
 *       Verifica previamente que el archivo no esté vacío. Si VirusTotal
 *       responde con {@code "AlreadySubmittedError"}, calcula el hash SHA-256
 *       del archivo y lanza {@link AlreadySubmittedException} con el hash para
 *       que el cliente pueda localizar el análisis existente.</li>
 *   <li>{@link #getAnalysis(String)} – Consulta el resultado de un análisis en
 *       {@code GET https://www.virustotal.com/api/v3/analyses/{id}}.
 *       Si el análisis está en estado {@code "queued"}, lanza
 *       {@link QueueException} indicando que debe reintentarse más tarde.</li>
 * </ul>
 *
 * <p><strong>Límites de tamaño:</strong> la propiedad
 * {@code spring.servlet.multipart.max-file-size=32MB} limita los archivos
 * a 32 MB. Los archivos de mayor tamaño generan una
 * {@code MaxUploadSizeExceededException} manejada por
 * {@link co.edu.unbosque.proyectoFinal.exception.GlobalExceptionHandler}.</p>
 *
 * @author Equipo de Desarrollo – Universidad El Bosque
 * @version 2.0
 * @see co.edu.unbosque.proyectoFinal.controller.VirusTotalApiController
 */
@Service
public class VirustotalService {

    /** URL base de la API de VirusTotal v3. */
    private static final String VT_BASE_URL = "https://www.virustotal.com/api/v3";

    /**
     * Clave de la API de VirusTotal. Se inyecta desde la propiedad
     * {@code virustotal.api.key} en {@code application.properties}.
     */
    @Value("${virustotal.api.key}")
    private String apiKey;

    /** Cliente HTTP reutilizable para todas las peticiones a VirusTotal. */
    private final OkHttpClient httpClient = new OkHttpClient();

    /** Deserializador JSON para convertir las respuestas de VirusTotal a DTOs. */
    private final Gson gson = new Gson();

    /** Constructor vacío requerido por Spring. */
    public VirustotalService() {}

    /**
     * Sube un archivo a VirusTotal para su análisis antivirus.
     *
     * <p><strong>Flujo de ejecución:</strong></p>
     * <ol>
     *   <li>Verifica que el archivo no esté vacío ({@link #validarArchivo(MultipartFile)}).</li>
     *   <li>Construye una petición {@code POST} multipart con el archivo y el encabezado
     *       {@code x-apikey}.</li>
     *   <li>Envía la petición y parsea la respuesta JSON con Gson.</li>
     *   <li>Si la respuesta contiene {@code "AlreadySubmittedError"}, calcula el hash
     *       SHA-256 del archivo y lanza {@link AlreadySubmittedException}.</li>
     *   <li>Establece el nombre original del archivo en el DTO de respuesta.</li>
     * </ol>
     *
     * @param file archivo a analizar; no puede estar vacío ni superar 32 MB
     * @return {@link VirusTotalUploadResponseDTO} con el ID del análisis en el campo
     *         {@code data.id}, a usar para consultar los resultados
     * @throws FileEmpyException          si el archivo está vacío
     * @throws AlreadySubmittedException  si el archivo ya fue enviado previamente a VirusTotal
     * @throws IOException                si ocurre un error de red o al leer el archivo
     * @throws Exception                  si la respuesta HTTP no puede parsearse
     */
    public VirusTotalUploadResponseDTO subirArchivo(MultipartFile file)
            throws FileEmpyException, AlreadySubmittedException, IOException, Exception {

        validarArchivo(file);

        RequestBody fileBody = RequestBody.create(
            file.getBytes(),
            MediaType.parse(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
        );

        RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.getOriginalFilename(), fileBody)
            .build();

        Request request = new Request.Builder()
            .url(VT_BASE_URL + "/files")
            .post(requestBody)
            .addHeader("x-apikey", apiKey)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (responseBody.contains("AlreadySubmittedError")) {
                String sha256 = calcularSha256(file.getBytes());
                throw new AlreadySubmittedException(
                    "El archivo ya fue enviado previamente. SHA-256: " + sha256);
            }

            VirusTotalUploadResponseDTO dto = gson.fromJson(responseBody, VirusTotalUploadResponseDTO.class);
            if (dto.getData() != null) {
                dto.getData().setNombreArchivo(file.getOriginalFilename());
            }
            return dto;
        }
    }

    /**
     * Consulta el resultado de un análisis VirusTotal por su ID.
     *
     * <p><strong>Flujo de ejecución:</strong></p>
     * <ol>
     *   <li>Construye una petición {@code GET} al endpoint
     *       {@code /analyses/{analysisId}} con el encabezado {@code x-apikey}.</li>
     *   <li>Parsea la respuesta JSON con Gson.</li>
     *   <li>Si el estado del análisis es {@code "queued"}, lanza {@link QueueException}.</li>
     *   <li>Si el estado es {@code "completed"}, retorna el DTO con todos los resultados.</li>
     * </ol>
     *
     * @param analysisId ID del análisis devuelto previamente por {@link #subirArchivo(MultipartFile)}
     * @return {@link VirusTotalUploadResponseDTO} con los resultados completos del análisis
     * @throws QueueException si el análisis aún no ha sido procesado ({@code status: "queued"})
     * @throws IOException    si ocurre un error de red
     * @throws Exception      si la respuesta no puede parsearse
     */
    public VirusTotalUploadResponseDTO getAnalysis(String analysisId)
            throws QueueException, IOException, Exception {

        Request request = new Request.Builder()
            .url(VT_BASE_URL + "/analyses/" + analysisId)
            .get()
            .addHeader("x-apikey", apiKey)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            VirusTotalUploadResponseDTO dto = gson.fromJson(responseBody, VirusTotalUploadResponseDTO.class);

            if (dto.getData() != null
                    && dto.getData().getAttributes() != null
                    && "queued".equals(dto.getData().getAttributes().getStatus())) {
                throw new QueueException(
                    "El análisis aún está en cola. Vuelve a intentarlo en unos momentos.");
            }

            return dto;
        }
    }

    // ─── Métodos privados de apoyo ────────────────────────────────────────────

    /**
     * Verifica que el archivo no esté vacío.
     *
     * @param file archivo a validar
     * @throws FileEmpyException si el archivo es nulo o está vacío
     */
    private void validarArchivo(MultipartFile file) throws FileEmpyException {
        if (file == null || file.isEmpty()) {
            throw new FileEmpyException("El archivo no puede estar vacío");
        }
    }

    /**
     * Calcula el hash SHA-256 de un arreglo de bytes y lo retorna como cadena hexadecimal.
     *
     * <p>Utilizado para informar al usuario el identificador del archivo cuando
     * VirusTotal rechaza la subida por ser un duplicado reciente.</p>
     *
     * @param data bytes del archivo
     * @return hash SHA-256 en representación hexadecimal (64 caracteres)
     * @throws NoSuchAlgorithmException si el algoritmo {@code SHA-256} no está disponible
     *         (no ocurre en la JVM estándar)
     */
    private String calcularSha256(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
