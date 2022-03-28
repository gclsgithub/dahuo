package com.hytc.webmanage.common.jackson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.hytc.webmanage.common.util.FwDateTimeFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.log4j.Log4j2;

/**
 * Jockson の  Java8 の日付クラス対応<br>
 *
 * @see https://docs.spring.io/spring-boot/docs/current/reference/html/howto-spring-mvc.html#howto-customize-the-jackson-objectmapper
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer.configureMessageConverters(List<HttpMessageConverter<?>>)
 */
@Log4j2
@Configuration
@ConditionalOnClass({ObjectMapper.class, Jackson2ObjectMapperBuilder.class})
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
        log.info("Jackson2ObjectMapperBuilderCustomizer");
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder jmb) {
                // Java8 LocalDate
                {
                    final DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern(FwDateTimeFormat.yyyyMMdd.getValue());
                    jmb.serializerByType(LocalDate.class, new LocalDateSerializer(ofPattern));
                    jmb.deserializerByType(LocalDate.class, new LocalDateDeserializer(ofPattern));
                }
                // Java8 LocalDateTime
                {
                    final DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern(FwDateTimeFormat.yyyyMMddHHmmss.getValue());
                    jmb.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(ofPattern));
                    jmb.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern));
                }
            }
        };
    }

}
