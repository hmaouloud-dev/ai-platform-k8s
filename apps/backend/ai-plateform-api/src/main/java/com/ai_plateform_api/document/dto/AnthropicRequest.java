package com.ai_plateform_api.document.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AnthropicRequest(
        String model,
        @JsonProperty("max_tokens") int maxTokens,
        List<Message> messages
) {
    public record Message(String role, String content) {}
}