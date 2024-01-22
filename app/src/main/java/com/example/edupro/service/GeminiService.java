package com.example.edupro.service;

import com.example.edupro.R;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;

public class GeminiService {
    private final GenerativeModelFutures generativeModelFutures;

    public GeminiService(String apiKey, String modelName) {
        GenerativeModel generativeModel = new GenerativeModel(modelName, apiKey);
        this.generativeModelFutures = GenerativeModelFutures.from(generativeModel);
    }

    public GenerativeModelFutures getGenerativeModelFutures() {
        return generativeModelFutures;
    }
}
