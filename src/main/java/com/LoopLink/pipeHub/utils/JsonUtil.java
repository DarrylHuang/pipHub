package com.LoopLink.pipeHub.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode toJson(String s) throws JsonProcessingException {
        return mapper.readTree(s);
    }

}
