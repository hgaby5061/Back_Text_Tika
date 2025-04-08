package com.service.web.app.busqueda.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.extractor.EmbeddedDocumentExtractor;
import org.apache.tika.metadata.Metadata;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class FileEmbeddedDocumentExtractor implements EmbeddedDocumentExtractor{

	private int count=0;
	private final TikaConfig config=TikaConfig.getDefaultConfig();
	private static Detector detector;
	
	
	public FileEmbeddedDocumentExtractor(Detector detector) {
		this.detector=detector;
	}
	
	@Override
	public boolean shouldParseEmbedded(Metadata metadata) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void parseEmbedded(InputStream stream, ContentHandler handler, Metadata metadata, boolean outputHtml)
			throws SAXException, IOException {
		// TODO Auto-generated method stub
		
	}

}
