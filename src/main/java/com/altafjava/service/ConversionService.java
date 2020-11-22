package com.altafjava.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ConversionService {

	ResponseEntity<?> convert(MultipartFile multipartFile, String inputFormat, String outputFormat);

	ResponseEntity<Resource> download(String fileName);
}
