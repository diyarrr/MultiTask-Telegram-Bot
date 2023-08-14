package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class provides a utility to generate jokes using an external API.
 */
public class Jokes {

    private String apiKey = "joke-api-key-goes-here";

    /**
     * Generates a joke using the external joke API.
     *
     * @return A joke retrieved from the API.
     * @throws URISyntaxException If there's an issue with the URI syntax.
     * @throws IOException If there's an issue with the I/O operations.
     */
    public String generateJoke() throws URISyntaxException, IOException
    {
        String url = "https://api.api-ninjas.com/v1/jokes?limit=1";

        URL obj = new URI(url).toURL();

        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("X-Api-Key", apiKey); // Set the API key

        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseStream);

        return root.get(0).get("joke").asText();
    }
}
