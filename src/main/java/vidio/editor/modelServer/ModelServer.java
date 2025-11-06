package vidio.editor.modelServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;


import java.io.File;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelServer {

    @Value("${modelapi.url}")
    private String modelApiUrl;
    private final ObjectMapper objectMapper;

    public String sendVideoToFastApi(Map<String, Object> meta, Path filePath) {
        try {
            String metaJson = objectMapper.writeValueAsString(meta);

            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("meta", metaJson);
            formData.add("file", new FileSystemResource(new File(filePath.toString())));

            WebClient webClient = WebClient.create(modelApiUrl); //RestTemplete의 경우 Deprecated 예정 및 트래픽을 고려한 WebClient 사용

            String response = webClient.post()
                    .uri("/process")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // 동기 방식 (필요시 async로 전환 가능)

            log.info("Response: {}", response);
            return response;

        } catch (Exception e) {
            log.error("Failed to send : {}", e.getMessage(), e);
            return e.getMessage();
        }
    }
}
