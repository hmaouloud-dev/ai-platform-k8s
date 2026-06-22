package com.ai_plateform_api.document.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AnthropicResponse(
        String id,
        List<ContentBlock> content,
        String model,
        @JsonProperty("stop_reason") String stopReason
) {
    public record ContentBlock(String type, String text) {}
}