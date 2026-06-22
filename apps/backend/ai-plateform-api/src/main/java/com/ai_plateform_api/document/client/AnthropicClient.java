package com.ai_plateform_api.document.client;

import com.ai_plateform_api.document.dto.AnthropicRequest;
import com.ai_plateform_api.document.dto.AnthropicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "anthropic",
        url = "${anthropic.api.url:https://api.anthropic.com}",
        configuration = AnthropicFeignConfig.class
)
public interface AnthropicClient {

    @PostMapping("/v1/messages")
    AnthropicResponse createMessage(@RequestBody AnthropicRequest request);
}