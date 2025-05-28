package com.tourism.booking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateResponse(String userMessage) {
        String prompt = "Bạn là trợ lý AI chuyên tư vấn du lịch tại Việt Nam. " +
                "Hãy trả lời câu hỏi của người dùng một cách ngắn gọn, rõ ràng và đúng trọng tâm. " +
                "Nếu người dùng hỏi về lịch trình, hãy chia theo từng ngày (ví dụ: Ngày 1, Ngày 2...), mỗi hoạt động bắt đầu bằng dấu '-'. " +
                "Nếu người dùng hỏi về khách sạn, điểm tham quan, nhà hàng hoặc món ăn, hãy liệt kê từ 3 đến 5 lựa chọn nổi bật và phù hợp nhất, mỗi dòng bắt đầu bằng dấu '-'. " +
                "Khi gợi ý món ăn, hãy ghi rõ tên món và địa chỉ quán hoặc khu vực nên ăn. " +
                "Không cần viết lời chào, không giải thích lan man. Trả lời theo định dạng dễ đọc:\n- Tên địa điểm/món ăn: mô tả ngắn\n\n" +
                userMessage;


        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();

            Map<String, Object> contents = new HashMap<>();
            contents.put("role", "user");

            List<Map<String, Object>> parts = new ArrayList<>();
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", prompt);
            parts.add(textPart);

            contents.put("parts", parts);

            List<Map<String, Object>> contentsList = new ArrayList<>();
            contentsList.add(contents);

            requestBody.put("contents", contentsList);

            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.7);
            generationConfig.put("maxOutputTokens", 1024);
            generationConfig.put("topP", 0.8);
            generationConfig.put("topK", 40);
            requestBody.put("generationConfig", generationConfig);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map responseBody = responseEntity.getBody();
            if (responseBody != null) {
                try {
                    Map<String, Object> candidates = (Map<String, Object>) ((List<?>) responseBody.get("candidates")).get(0);
                    Map<String, Object> content = (Map<String, Object>) candidates.get("content");
                    List<Map<String, Object>> responseParts = (List<Map<String, Object>>) content.get("parts");
                    String responseText = (String) responseParts.get(0).get("text");
                    return responseText;
                } catch (Exception e) {
                    System.out.println("Error parsing response: " + responseBody);
                    e.printStackTrace();
                    return "Xin lỗi, tôi không thể xử lý phản hồi từ AI. Vui lòng thử lại sau.";
                }
            } else {
                return "Không nhận được phản hồi từ AI. Vui lòng thử lại sau.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Xin lỗi, tôi đang gặp sự cố kỹ thuật. Vui lòng thử lại sau. Lỗi: " + e.getMessage();
        }
    }
}