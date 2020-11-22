package com.altafjava.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.altafjava.beans.UploadFileResponse;
import com.altafjava.enums.FileType;
import com.altafjava.service.ConversionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * @author ALTAF
 *
 */
@Service
public class ConversionServiceImpl implements ConversionService {

	@Override
	public ResponseEntity<?> convert(MultipartFile multipartFile, String inputFormat, String outputFormat) {
		storeInputFile(multipartFile);
		if (inputFormat.equalsIgnoreCase(FileType.CSV.name()) && outputFormat.equalsIgnoreCase(FileType.JSON.name())) {
			return new ResponseEntity<>(convertCsvToJson(multipartFile, outputFormat), HttpStatus.OK);
		}
		return new ResponseEntity<>("Sorry, currently we are not converting ." + inputFormat + " to ." + outputFormat, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Resource> download(String fileName) {
		String outputDirName = "output";
		Path outputPath = Paths.get(outputDirName).toAbsolutePath().normalize();
		Path filePath = outputPath.resolve(fileName).normalize();
		String contentType = getContentType(filePath);
		Resource resource = null;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").header("Accept", "*/*").body(resource);
	}

	public UploadFileResponse convertCsvToJson(MultipartFile multipartFile, String outputFormat) {
		String outputDirName = "output";
		Path outputPath = Paths.get(outputDirName);
		if (!Files.exists(outputPath)) {
			try {
				Files.createDirectory(outputPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String originalFileName = multipartFile.getOriginalFilename();
		String targetFileName = originalFileName.replaceFirst("[.][^.]+$", "");
		String targetFileNameWithExtension = targetFileName + "_" + System.currentTimeMillis() + ".json";
		File output = new File(outputDirName + "/" + targetFileNameWithExtension);
		List<Object> csvObjects = Collections.EMPTY_LIST;
		if (outputFormat.equalsIgnoreCase(FileType.JSON.name())) {
			CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
			CsvMapper csvMapper = new CsvMapper();
			try {
				csvObjects = csvMapper.readerFor(Map.class).with(csvSchema).readValues(multipartFile.getInputStream()).readAll();
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.writerWithDefaultPrettyPrinter().writeValue(output, csvObjects);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(targetFileNameWithExtension).toUriString();
		Path outputFilePath = outputPath.resolve(Paths.get(targetFileNameWithExtension)).normalize().toAbsolutePath();
		String contentType = getContentType(outputFilePath);
		return new UploadFileResponse(targetFileNameWithExtension, fileDownloadUri, contentType, output.length());
	}

	private void storeInputFile(MultipartFile multipartFile) {
		Path rootLocation = Paths.get("input");
		try {
			if (!Files.exists(rootLocation)) {
				Files.createDirectory(rootLocation);
			}
			Path destinationFile = rootLocation.resolve(Paths.get(multipartFile.getOriginalFilename())).normalize().toAbsolutePath();
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to store file", e);
		}
	}

	private String getContentType(Path path) {
		String contentType = "application/octet-stream";
		try {
			contentType = Files.probeContentType(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentType;
	}

}
