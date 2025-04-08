package com.service.web.app.busqueda.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertPathValidatorException.BasicReason;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.EmbeddedDocumentExtractor;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Office;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BasicContentHandlerFactory;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

//import org.xml.sax.ContentHandler;
//import org.xml.sax.SAXException;
//import org.xml.sax.SAXException;

import com.service.web.app.busqueda.models.Document;
import com.service.web.app.busqueda.utils.FileEmbeddedDocumentExtractor;

import ch.qos.logback.classic.pattern.DateConverter;
import ch.qos.logback.core.pattern.parser.Parser;

public class DocumentServiceImpl {

	public List<Object> extractText(List<MultipartFile> files, Map<String, String> modif)
			throws IOException, TikaException, SAXException {

		System.out.println("ENTRO EN TIKA IMPL");

		List<Object> list = new ArrayList<>();

		for (MultipartFile file : files) {

			Document doc = new Document();
			String[] name = file.getOriginalFilename().split(".docx");
			doc.setName(name[0]);

			if (file.getOriginalFilename().contains("doc") || file.getOriginalFilename().contains("docx")
					|| file.getOriginalFilename().contains("pdf")) {
				BasicContentHandlerFactory basicContent = new BasicContentHandlerFactory(
						BasicContentHandlerFactory.HANDLER_TYPE.TEXT, -1);
				AutoDetectParser parser = new AutoDetectParser();
				ContentHandler handler = basicContent.getNewContentHandler();
				Metadata metadata = new Metadata();

				Tika tika = new Tika();
				ParseContext context = new ParseContext();
				context.set(EmbeddedDocumentExtractor.class, new FileEmbeddedDocumentExtractor(tika.getDetector()));
				// context.set(Parser.class, parser);

				InputStream stream = file.getInputStream();
				parser.parse(stream, handler, metadata, context);
				doc.setText(handler.toString());
				//

				// Obtener fecha de creación
				// Date date=metadata.getDate(Office.CREATION_DATE);
				String date = metadata.get("meta:creation-date");

				doc.setDateCreation(date);

				list.add(doc);
			}

			// Extraer texto
		}

		return list;
	}

}
