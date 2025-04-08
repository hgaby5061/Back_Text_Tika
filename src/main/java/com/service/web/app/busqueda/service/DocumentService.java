package com.service.web.app.busqueda.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.service.web.app.busqueda.models.Document;

public interface DocumentService {

	public String extractText(List<MultipartFile> ruta, Map<String, String> modi, Map<String, String> author)
			throws IOException, SAXException, TikaException;

}
