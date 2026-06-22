package com.ai_plateform_api.document.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TextExtractorService {

    private static final int MAX_CHARS = 100_000;

    public String extract(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide.");
        }

        String filename = Optional.ofNullable(file.getOriginalFilename())
                .map(String::toLowerCase)
                .orElseThrow(() -> new IllegalArgumentException("Nom de fichier invalide."));

        String text;
        if (filename.endsWith(".txt")) {
            text = extractTxt(file);
        } else if (filename.endsWith(".pdf")) {
            text = extractPdf(file);
        } else if (filename.endsWith(".docx")) {
            text = extractDocx(file);
        } else {
            throw new IllegalArgumentException("Format non supporté. Utilisez PDF, TXT ou DOCX.");
        }

        if (text.isBlank()) {
            throw new IllegalArgumentException("Le document est vide ou illisible.");
        }

        if (text.length() > MAX_CHARS) {
            log.warn("Document tronqué à {} caractères (taille originale : {})", MAX_CHARS, text.length());
            return text.substring(0, MAX_CHARS);
        }

        return text;
    }

    private String extractTxt(MultipartFile file) {
        try {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier TXT.", e);
        }
    }

    private String extractPdf(MultipartFile file) {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            return new PDFTextStripper().getText(document);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier PDF.", e);
        }
    }

    private String extractDocx(MultipartFile file) {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            return document.getParagraphs().stream()
                    .map(p -> p.getText())
                    .filter(t -> !t.isBlank())
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier DOCX.", e);
        }
    }
}