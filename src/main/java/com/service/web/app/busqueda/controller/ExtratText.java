package com.service.web.app.busqueda.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.service.web.app.busqueda.models.Document;
import com.service.web.app.busqueda.service.DocumentService;
import com.service.web.app.busqueda.service.DocumentServiceImpl;

@RestController
@RequestMapping("text-tika")

public class ExtratText {

	private static final Logger loo = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Autowired
	private DocumentService document;

	@PostMapping("")
	public ResponseEntity<String> prueba(@RequestParam("files") List<MultipartFile> file,
			@RequestParam Map<String, String> modif, @RequestParam Map<String, String> author) {
		System.out.println("entro en tika");
		String documents = null;
		if (file.size() > 0)
			try {
				documents = document.extractText(file, modif, author);
				System.out.println("SALIO DE TIKA");
			} catch (IOException | SAXException | TikaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return ResponseEntity.ok(documents);
	}

}
