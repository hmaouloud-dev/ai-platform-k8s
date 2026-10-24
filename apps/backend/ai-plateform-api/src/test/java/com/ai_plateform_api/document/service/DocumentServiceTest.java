package com.ai_plateform_api.document.service;

import com.ai_plateform_api.document.client.AnthropicClient;
import com.ai_plateform_api.document.dto.AnthropicRequest;
import com.ai_plateform_api.document.dto.AnthropicResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private TextExtractorService textExtractorService;

    @Mock
    private AnthropicClient anthropicClient;

    @InjectMocks
    private DocumentService documentService;

    private final MockMultipartFile file =
            new MockMultipartFile("file", "doc.txt", "text/plain", "contenu".getBytes());

    @Test
    void returnsSummaryFromAnthropicResponse() {
        when(textExtractorService.extract(file)).thenReturn("contenu du document");
        when(anthropicClient.createMessage(any())).thenReturn(response(
                new AnthropicResponse.ContentBlock("text", "Un résumé concis.")));

        assertThat(documentService.summarize(file).summary()).isEqualTo("Un résumé concis.");

        ArgumentCaptor<AnthropicRequest> captor = ArgumentCaptor.forClass(AnthropicRequest.class);
        verify(anthropicClient).createMessage(captor.capture());
        assertThat(captor.getValue().messages().get(0).content()).endsWith("contenu du document");
    }

    @Test
    void failsWhenResponseHasNoTextBlock() {
        when(textExtractorService.extract(file)).thenReturn("contenu");
        when(anthropicClient.createMessage(any())).thenReturn(response());

        assertThatThrownBy(() -> documentService.summarize(file))
                .hasMessageContaining("Aucun résumé");
    }

    private static AnthropicResponse response(AnthropicResponse.ContentBlock... blocks) {
        return new AnthropicResponse("msg_1", List.of(blocks), "claude", "end_turn");
    }
}
