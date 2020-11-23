package com.altafjava.util;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import com.altafjava.enums.FileType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ALTAF
 *
 */

@Slf4j
public class ConversionUtil {

	public static boolean convertJsonToCsv(MultipartFile multipartFile, String outputFormat, File outputFile) {
		boolean isConvertedSuccessfully = false;
		try {
			JsonNode jsonTree = new ObjectMapper().readTree(multipartFile.getInputStream());
			Builder csvSchemaBuilder = CsvSchema.builder();
			JsonNode firstObject = jsonTree.elements().next();
			firstObject.fieldNames().forEachRemaining(fieldName -> {
				csvSchemaBuilder.addColumn(fieldName);
			});
			CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			csvMapper.writerFor(JsonNode.class).with(csvSchema).writeValue(outputFile, jsonTree);
			isConvertedSuccessfully = true;
		} catch (IOException e) {
			log.error("Failed to convert JSON to CSV: " + e);
		}
		return isConvertedSuccessfully;
	}

	public static boolean convertCsvToJson(MultipartFile multipartFile, String outputFormat, File outputFile) {
		boolean isConvertedSuccessfully = false;
		List<Object> csvObjects = Collections.EMPTY_LIST;
		if (outputFormat.equalsIgnoreCase(FileType.JSON.name())) {
			CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
			CsvMapper csvMapper = new CsvMapper();
			try {
				csvObjects = csvMapper.readerFor(Map.class).with(csvSchema).readValues(multipartFile.getInputStream()).readAll();
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, csvObjects);
				isConvertedSuccessfully = true;
			} catch (IOException e) {
				log.error("Failed to convert CSV to JSON: " + e);
			}
		}
		return isConvertedSuccessfully;
	}
}
