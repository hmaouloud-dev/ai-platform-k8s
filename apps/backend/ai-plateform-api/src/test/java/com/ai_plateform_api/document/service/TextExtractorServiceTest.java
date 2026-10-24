package com.ai_plateform_api.document.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextExtractorServiceTest {

    private final TextExtractorService service = new TextExtractorService();

    @Test
    void extractsTextFromTxtFile() {
        MockMultipartFile file = txt("notes.txt", "Bonjour le monde");

        assertThat(service.extract(file)).isEqualTo("Bonjour le monde");
    }

    @Test
    void rejectsEmptyFile() {
        MockMultipartFile file = txt("vide.txt", "");

        assertThatThrownBy(() -> service.extract(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vide");
    }

    @Test
    void rejectsUnsupportedFormat() {
        MockMultipartFile file = txt("image.png", "xxx");

        assertThatThrownBy(() -> service.extract(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Format non supporté");
    }

    @Test
    void rejectsBlankContent() {
        MockMultipartFile file = txt("blanc.txt", "   \n  ");

        assertThatThrownBy(() -> service.extract(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("illisible");
    }

    @Test
    void truncatesVeryLongDocuments() {
        MockMultipartFile file = txt("long.txt", "a".repeat(150_000));

        assertThat(service.extract(file)).hasSize(100_000);
    }

    private static MockMultipartFile txt(String name, String content) {
        return new MockMultipartFile("file", name, "text/plain", content.getBytes(StandardCharsets.UTF_8));
    }
}
