package com.service.web.app.busqueda.models;

import java.util.List;

public class ProcessingResult {
    private List<Object> documents; // JSON representation of documents
    private List<String> errorFiles; // JSON representation of error files

    public ProcessingResult(List<Object> documents, List<String> errorFiles) {

        this.documents = documents;
        this.errorFiles = errorFiles;
    }

    public List<Object> getDocuments() {

        return documents;
    }

    public List<String> getErrorFiles() {

        return errorFiles;
    }
}
