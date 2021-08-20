package de.nanoimaging.esp32_http_restapi;

import org.json.JSONObject;

public class APIRequest {

    String endpoint;  // Create a class attribute
    JSONObject jsonObject;

    public APIRequest(String endpoint, JSONObject jsonObject) {
        this.endpoint = endpoint;
        this.jsonObject = jsonObject;
    }
}