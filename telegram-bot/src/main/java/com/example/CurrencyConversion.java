

package com.example;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This class provides a utility for performing currency conversion using an external API.
 */
public class CurrencyConversion 
{
    private String apiKey = "converter-api-key-goes-here";


    /**
     * Performs currency conversion using an external API.
     *
     * @param input The input in the format "FROM_CURRENCY/TO_CURRENCY".
     * @return The conversion rate from the base currency to the main currency.
     * @throws Exception If an error occurs during the conversion process.
     * @throws IllegalArgumentException If the input format is invalid.
     */
    public double makeConversion(String input) throws Exception 
    {
        // Make string uppercase, because API gives data that way
    input = input.toUpperCase();

    // Split the string using "/" as the delimiter
    String[] currencies = input.split("/");
    String mainCurrency;
    String baseCurrency;

    // Ensure the array has exactly two elements
    if (currencies.length == 2) {
        mainCurrency = currencies[0];
        baseCurrency = currencies[1];
    } else {
        throw new IllegalArgumentException("Invalid input format"); // Throwing IllegalArgumentException
    }

    String apiUrl = "https://v6.exchangerate-api.com/v6/" + this.apiKey + "/latest/" + baseCurrency;

    HttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(apiUrl);

    try {
        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        return extractContentFromJson(responseBody, mainCurrency);
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("An error occurred while making the API request"); // Throwing RuntimeException
    }
    }


    /**
     * Extracts the conversion rate from the JSON response received from the API.
     *
     * @param json_data The JSON response from the API.
     * @param mainCurrency The main currency for conversion.
     * @return The conversion rate for the main currency.
     * @throws Exception If there's an issue with parsing the JSON data.
     */
    private double extractContentFromJson(String json_data, String mainCurrency)  throws Exception
    {

        // Parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataNode = objectMapper.readTree(json_data);

        // Access the value associated with "USD" in the "conversion_rates" object
        double conversion = dataNode.get("conversion_rates").get(mainCurrency).asDouble();

        return conversion;

    }



}