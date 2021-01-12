package com.lista.avgcursbank.model.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JsonStr2ObjMap {
    private static final Logger log = LoggerFactory.getLogger(JsonStr2ObjMap.class);

    public Map<String, Object> getStringObjectMap(String s) {
        String cTrade;
        cTrade = s;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapTrade = null;
        try {
            mapTrade = mapper.readValue(cTrade, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return mapTrade;
    }
}
