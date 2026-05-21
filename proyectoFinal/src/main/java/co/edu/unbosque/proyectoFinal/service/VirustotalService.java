package co.edu.unbosque.proyectoFinal.service;

import java.security.MessageDigest;
import java.util.HexFormat;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import co.edu.unbosque.proyectoFinal.dto.VirusTotalUploadResponseDTO;
import co.edu.unbosque.proyectoFinal.entity.VirusTotalUploadResponse;
import co.edu.unbosque.proyectoFinal.exception.*;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Servicio para la integracion con la API de VirusTotal.
 * Permite subir archivos para escaneo y consultar los resultados de analisis.
 */
@Service
public class VirustotalService {

	private OkHttpClient cliente = new OkHttpClient();
	@Autowired
	private Gson gson;
	
	@Autowired
	private ModelMapper mapper;

	/**
	 * Sube un archivo a VirusTotal para escaneo.
	 * @param archivo archivo a subir
	 * @return respuesta de VirusTotal con el ID del analisis
	 * @throws Exception si ocurre un error durante la subida
	 * @throws FileEmpyException si el archivo esta vacio
	 * @throws AlreadySubmittedException si el archivo ya fue subido previamente
	 */
	public VirusTotalUploadResponseDTO subirArchivo(MultipartFile archivo) throws Exception {

		if (archivo.isEmpty()) {
			throw new FileEmpyException("El archivo no puede estar vacio");
		}

		if (archivo.getSize() > 32 * 1024 * 1024) {
			throw new MaxUploadSizeExceededException(archivo.getSize());
		}

		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("file", archivo.getOriginalFilename(), RequestBody.create(archivo.getBytes())).build();

		Request request = new Request.Builder().url("https://www.virustotal.com/api/v3/files")
				.addHeader("x-apikey", "c0fd207ae68f24971c4c8ae61c11139de5c74967f2ec413171f17e910fbd7494")
				.addHeader("content-type", "multipart/form-data").post(requestBody)
				.addHeader("accept", "application/json").build();

		Response response = cliente.newCall(request).execute();

		String body = response.body().string();

		VirusTotalUploadResponse VirusTotalResponse = gson.fromJson(body, VirusTotalUploadResponse.class);

		if (VirusTotalResponse.getData() != null) {
			VirusTotalResponse.getData().setNombreArchivo(archivo.getOriginalFilename());
			return mapper.map(VirusTotalResponse, VirusTotalUploadResponseDTO.class);
		}

		if (body.contains("AlreadySubmittedError")) {
			throw new AlreadySubmittedException("El archivo ya fue subido para escaneo, este es el sha256: " + obtenerHash(archivo));
			
		}
		
		throw new ResourceNotFoundException("No se pudo subir el archivo, intente nuevamente");
		
	}

	/**
	 * Obtiene los resultados de un analisis de VirusTotal por su ID.
	 * @param analysisId identificador del analisis
	 * @return respuesta de VirusTotal con los resultados del analisis
	 * @throws Exception si ocurre un error durante la consulta
	 * @throws QueueException si el analisis aun esta en cola
	 * @throws ResourceNotFoundException si no se encuentra el analisis
	 */
	public VirusTotalUploadResponseDTO getAnalysis(String analysisId) throws Exception {
		
		if (analysisId == null || analysisId.isEmpty()) {
			throw new ResourceNotFoundException("El id del analisis no puede estar vacio");
		}
		
		Request request = new Request.Builder().url("https://www.virustotal.com/api/v3/analyses/" + analysisId)
				.header("x-apikey", "c0fd207ae68f24971c4c8ae61c11139de5c74967f2ec413171f17e910fbd7494").get().build();

		Response response = cliente.newCall(request).execute();

		String body = response.body().string();

		VirusTotalUploadResponse VirusTotalResponse = gson.fromJson(body, VirusTotalUploadResponse.class);

		if (VirusTotalResponse.getData() != null && VirusTotalResponse.getData().getAttributes() != null
				&& VirusTotalResponse.getData().getAttributes().getStatus().equals("queued")) {
			throw new QueueException("El archivo esta en cola de analisis, vuelva a intentarlo en unos minutos");
		}

		if (VirusTotalResponse.getData() != null && VirusTotalResponse.getData().getAttributes() != null
				&& VirusTotalResponse.getData().getAttributes().getStatus().equals("completed")) {
			return mapper.map(VirusTotalResponse, VirusTotalUploadResponseDTO.class);
		}
		
		throw new ResourceNotFoundException("No se encontro el analisis con el id proporcionado: " + analysisId);
	}
	
	/**
	 * Calcula el hash SHA-256 de un archivo.
	 * @param archivo archivo del cual calcular el hash
	 * @return hash SHA-256 en formato hexadecimal
	 * @throws Exception si ocurre un error durante el calculo
	 */
	public String obtenerHash(MultipartFile archivo) throws Exception {
	    byte[] contenido = archivo.getBytes();
	    byte[] hashBytes = MessageDigest.getInstance("SHA-256").digest(contenido);
	    return HexFormat.of().formatHex(hashBytes);
	}

}
