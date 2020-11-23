package com.altafjava.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ALTAF
 *
 */

public class CommonUtil {

	public static void storeInputFile(MultipartFile multipartFile) {
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

	public static String getContentType(Path path) {
		String contentType = null;
		try {
			contentType = Files.probeContentType(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentType == null ? "application/octet-stream" : contentType;
	}
}
