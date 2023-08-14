package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class provides a utility for interacting with the OpenAI GPT-3.5 Turbo chat API.
 */
public class ChatGpt {


    private String chatGptApiKey = "chatgpt-api-goes-here";

    /**
     * Generates a response using the OpenAI GPT-3.5 Turbo chat API.
     *
     * @param input The user's input message.
     * @return The generated response from the chat API.
     * @throws IOException If there's an issue with I/O operations.
     * @throws URISyntaxException If there's an issue with the URI syntax.
     */
    public String generateResponse(String input) throws IOException, URISyntaxException
    {
        
        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-3.5-turbo"; // current model of chatgpt api

        
        // Create the HTTP POST request
        URL obj = new URI(url).toURL();
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + this.getApiKey());
        con.setRequestProperty("Content-Type", "application/json");


        con.setDoOutput(true); // Enable sending request body

        // Construct the JSON payload
        String jsonPayload = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + input + "\"}]}";


        con.getOutputStream().write(jsonPayload.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        


        try 
        {
            return extractContentFromJson(output);
        } catch(Exception e) 
        { 
            e.printStackTrace();
        }

        return "Failed to generate response";
        
    }

    /**
     * Extracts the content (answer) from the JSON response received from the chat API.
     *
     * @param json_data The JSON response from the chat API.
     * @return The extracted content from the JSON response.
     * @throws Exception If there's an issue with parsing the JSON data.
     */
    private String extractContentFromJson(String json_data)  throws Exception
    {

        // Parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataNode = objectMapper.readTree(json_data);

        // Access "content" within the "message" object
        String content = dataNode.get("choices").get(0).get("message").get("content").asText();

        return content;

    }


    /**
     * Gets the API key for authentication with the OpenAI chat API.
     *
     * @return The API key used for authentication.
     */
    public String getApiKey() 
    {
        return this.chatGptApiKey;

    }
    
}
