package com.hytc.webmanage.common.jackson;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
@AutoConfigureAfter(JacksonConfiguration.class)
public final class FwJacksonConverter {

    final private ObjectMapper objectMapper;

    /**
     * ObjectからJSONへ変換
     *
     * @param obj
     * @return JSON String
     */
    public String objToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Object not convert to json.", e);
        }
        return null;
    }

    public byte[] objToByte(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            log.warn("Object not convert to json.", e);
        }
        return null;
    }

    /**
     * JSON文字列からObjectへ変換
     *
     * @param sJson
     * @param cls
     * @return T
     */
    public <T> T jsonToObj(String jsonString, Class<T> cls) {
        Assert.notNull(jsonString, "jsonString is null.");
        try {
            return objectMapper.readValue(jsonString, cls);
        } catch (Exception e) {
            log.warn(jsonString + " is not json.", e);
        }
        return null;
    }

    /**
     * JSON文字列からObjectへ変換
     *
     * @param sJson
     * @param valueType
     * @return T
     */
    public <T> T jsonToObj(String jsonString, JavaType valueType) {
        Assert.notNull(jsonString, "jsonString is null.");
        try {
            return objectMapper.readValue(jsonString, valueType);
        } catch (Exception e) {
            log.warn(jsonString + " is not json.", e);
        }
        return null;
    }

    public <T> T byteToObj(byte[] jsonString, Class<T> cls) {
        Assert.notNull(jsonString, "jsonString is null.");
        try {
            return objectMapper.readValue(jsonString, cls);
        } catch (Exception e) {
            log.warn(jsonString + " is not json.", e);
        }
        return null;
    }

}
