package com.altafjava.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.altafjava.util.CommonUtil;
import com.altafjava.util.ConversionUtil;

/**
 * @author ALTAF
 *
 */
@Service
public class ConversionServiceImpl implements ConversionService {

	@Override
	public ResponseEntity<?> convert(MultipartFile multipartFile, String inputFormat, String outputFormat) {
		CommonUtil.storeInputFile(multipartFile);
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
		String targetFileNameWithExtension = targetFileName + "_" + System.currentTimeMillis() + "." + outputFormat;
		File outputFile = new File(outputDirName + "/" + targetFileNameWithExtension);

		boolean isConvertedSuccessfully = false;
		if (inputFormat.equalsIgnoreCase(FileType.CSV.name()) && outputFormat.equalsIgnoreCase(FileType.JSON.name())) {
			isConvertedSuccessfully = ConversionUtil.convertCsvToJson(multipartFile, outputFormat, outputFile);
		} else if (inputFormat.equalsIgnoreCase(FileType.JSON.name()) && outputFormat.equalsIgnoreCase(FileType.CSV.name())) {
			isConvertedSuccessfully = ConversionUtil.convertJsonToCsv(multipartFile, outputFormat, outputFile);
		}

		if (isConvertedSuccessfully) {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(targetFileNameWithExtension).toUriString();
			Path outputFilePath = outputPath.resolve(Paths.get(targetFileNameWithExtension)).normalize().toAbsolutePath();
			String contentType = CommonUtil.getContentType(outputFilePath);
			return new ResponseEntity<>(new UploadFileResponse(targetFileNameWithExtension, fileDownloadUri, contentType, outputFile.length()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Sorry, currently we are not converting ." + inputFormat + " to ." + outputFormat, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Resource> download(String fileName) {
		String outputDirName = "output";
		Path outputPath = Paths.get(outputDirName).toAbsolutePath().normalize();
		Path filePath = outputPath.resolve(fileName).normalize();
		String contentType = CommonUtil.getContentType(filePath);
		Resource resource = null;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").header("Accept", "*/*").body(resource);
	}

}
