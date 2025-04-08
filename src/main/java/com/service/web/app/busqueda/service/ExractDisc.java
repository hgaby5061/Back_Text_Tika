package com.service.web.app.busqueda.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.Tika;

import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.EmbeddedDocumentExtractor;

import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.service.web.app.busqueda.models.Discurs;
import com.service.web.app.busqueda.models.ProcessingResult;
import com.service.web.app.busqueda.utils.DateExtractor;
import com.service.web.app.busqueda.utils.FileEmbeddedDocumentExtractor;

@Service
public class ExractDisc implements DocumentService {

	public String extractText(List<MultipartFile> files, Map<String, String> modif, Map<String, String> author)
			throws IOException, TikaException, SAXException {

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("es", "ES"));
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
				.withZone(ZoneOffset.UTC);

		List<Object> list = new ArrayList<>();
		List<String> errorFiles = new ArrayList<>(); // List to collect files with errors

		int batchSize = 200; // Define the batch size
		for (int i = 0; i < files.size(); i += batchSize) {
			int end = Math.min(i + batchSize, files.size());
			List<MultipartFile> batch = files.subList(i, end);
			for (MultipartFile file : batch) {

				try (InputStream stream = file.getInputStream()) {
					Discurs d = new Discurs();

					// Extraer texto y metadatos
					ContentHandler handler = new BodyContentHandler(-1);
					AutoDetectParser parser = new AutoDetectParser();
					Metadata metadata = new Metadata();
					Tika tika = new Tika();

					ParseContext context = new ParseContext();
					context.set(EmbeddedDocumentExtractor.class, new FileEmbeddedDocumentExtractor(tika.getDetector()));

					parser.parse(stream, handler, metadata, context);

					d.setId(metadata.get("xmpMM:DocumentID"));
					d.setId(metadata.get("custom:ICV"));

					String pattern = "^(.*?\\.(html|pdf|xml|doc|docx))\\d+\\.tmp$";
					Pattern r = Pattern.compile(pattern);
					Matcher m = r.matcher(file.getOriginalFilename());

					if (m.find()) {
						String realFilename = m.group(1);
						d.setName(realFilename);
						if (d.getId() == null)
							d.setId(realFilename + "_" + modif.get("modified"));
					} else {
						errorFiles.add(file.getOriginalFilename()); // Add to error list
						System.out.println("No match found for file: " + file.getOriginalFilename());
						continue; // Skip to the next file
					}

					d.setTitle(metadata.get("dc:title"));
					d.setDate(metadata.get("dcterms:created"));
					d.setAuthor(author.get("author"));

					if (d.getTitle() != null && d.getDate() == null) {
						String localDateS = DateExtractor.convertDate(d.getTitle());
						if (localDateS != null) {
							LocalDate localDate = LocalDate.parse(localDateS, inputFormatter);
							String formattedDate = outputFormatter.format(localDate.atStartOfDay(ZoneOffset.UTC));
							d.setDate(formattedDate);
						}
					}

					d.setText(handler.toString());

					String lang = detectLanguage(handler.toString());
					if (lang != null)
						d.setLang(lang);

					list.add(d);
				} catch (Exception e) {
					errorFiles.add(file.getOriginalFilename());
					System.err.println("Error processing file: " + file.getOriginalFilename() + " - " + e.getMessage());
				}
			}
		}

		/*
		 * // Convert the list of documents to JSON
		 * String documentsJson = new Gson().toJson(list);
		 * // Convert the list of error files to JSON
		 * String errorFilesJson = new Gson().toJson(errorFiles);
		 */

		return new Gson().toJson(new ProcessingResult(list, errorFiles)); // Return both JSON strings

	}

	private static String detectLanguage(String text) {
		try {
			OptimaizeLangDetector lang = (OptimaizeLangDetector) new OptimaizeLangDetector().loadModels();
			lang.addText(text);
			String langString = lang.detect().getLanguage();
			return langString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
