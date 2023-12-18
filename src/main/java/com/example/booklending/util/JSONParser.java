package com.example.booklending.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode parseJson(String jsonString) {
        try {
            // Parse the JSON response
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringValue(String jsonString, String fieldName) {
        try {
            // Parse the JSON response
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Retrieve the value of the specified field
            return jsonNode.get(fieldName).asText();
        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            return null;
        }
    }
}
