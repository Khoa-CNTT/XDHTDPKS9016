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
        // Tạo context để AI hiểu được nó đang tư vấn về khách sạn
        String prompt = "Bạn là trợ lý AI tư vấn về khách sạn, du lịch và các dịch vụ liên quan tại Việt Nam. " +
                "Hãy trả lời câu hỏi sau một cách chi tiết và hữu ích. " +
                "Chỉ liệt kê một vài lựa chọn nổi tiếng và phù hợp nhất, mỗi lựa chọn bắt đầu bằng dấu gạch đầu dòng (-) và viết trên một dòng riêng biệt, không sử dụng định dạng in đậm hoặc ký tự đặc biệt:\n" +
                userMessage;


        try {
            // URL của API đã được cập nhật theo định dạng mới nhất
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();

            // Cấu trúc yêu cầu đã được cập nhật theo tài liệu mới nhất của Google
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

            // Thêm các tham số tùy chọn để cải thiện kết quả
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

            // Parse phản hồi theo cấu trúc mới nhất của API
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