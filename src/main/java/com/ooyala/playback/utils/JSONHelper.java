package com.ooyala.playback.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.util.List;

public class JSONHelper {
    public static Object toObject(String json, Class cl) throws IOException {
        ObjectMapper mapper = getJsonMapper();
        return mapper.readValue(json,cl);
    }

    public static List<?> toObjectList(String json, Class cl) throws IOException {
        ObjectMapper mapper = getJsonMapper();
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, cl));
    }

    public static String writeToString(Object o) throws JsonProcessingException {
        return getJsonMapper().writeValueAsString(o);
    }

    public static ObjectMapper getJsonMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED,true);
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN,true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(
                PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return mapper;
    }
}
