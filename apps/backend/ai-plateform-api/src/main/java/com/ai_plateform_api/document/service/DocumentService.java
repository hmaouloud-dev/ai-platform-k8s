package com.ai_plateform_api.document.service;

import com.ai_plateform_api.document.client.AnthropicClient;
import com.ai_plateform_api.document.dto.AnthropicRequest;
import com.ai_plateform_api.document.dto.AnthropicResponse;
import com.ai_plateform_api.document.dto.SummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final TextExtractorService textExtractorService;
    private final AnthropicClient anthropicClient;

    @Value("${anthropic.model:claude-sonnet-4-6}")
    private String model;

    public SummaryResponse summarize(MultipartFile file) {
        log.info("Résumé du fichier : {}", file.getOriginalFilename());

        String text = textExtractorService.extract(file);

        AnthropicRequest request = new AnthropicRequest(
                model,
                2048,
                List.of(new AnthropicRequest.Message(
                        "user",
                        "Résume le document suivant de manière claire et concise en français :\n\n" + text
                ))
        );

        AnthropicResponse response = anthropicClient.createMessage(request);

        String summary = response.content().stream()
                .filter(block -> "text".equals(block.type()))
                .map(AnthropicResponse.ContentBlock::text)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun résumé retourné par l'IA."));

        log.info("Résumé généré avec succès pour : {}", file.getOriginalFilename());
        return new SummaryResponse(summary);
    }
}