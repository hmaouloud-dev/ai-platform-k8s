package com.ai_plateform_api.document.controller;

import com.ai_plateform_api.document.dto.SummaryResponse;
import com.ai_plateform_api.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/summarize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SummaryResponse> summarize(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(documentService.summarize(file));
    }
}