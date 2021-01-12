package com.lista.avgcursbank.config;

import com.lista.avgcursbank.model.converters.Json2Trade03Converter;
import com.lista.avgcursbank.model.converters.JsonStr2ObjMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);
    private final JsonStr2ObjMap jsonStr2ObjMap;

    public WebConfig(JsonStr2ObjMap jsonStr2ObjMap) {
        this.jsonStr2ObjMap = jsonStr2ObjMap;
    }

    /**
     * конвертор данных
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Json2Trade03Converter(jsonStr2ObjMap));

    }

    /**
     * swagger-ui Для HTTP-запроса сопоставление не найдено
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/configuration/ui", "/swagger-resources/configuration/ui");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * https://www.javainuse.com/spring/spring-boot-content-negotiation
     * 1. это добавлено
     * 2. class  @RequestMapping(val ... ответ констуируется чз set-теры
     * 3. @XmlRootElement !!!
     * public class Querying {     *
     * @param configurer -
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        log.info("config.contains(\"Using Path Extension\")");
        // установить расширение пути в true
        configurer.favorPathExtension(true).
        // установить параметр предпочтения в false
        favorParameter(false).
        // игнорируем заголовки принятия
        ignoreAcceptHeader(true).
        defaultContentType(MediaType.APPLICATION_JSON).
        mediaType("xml", MediaType.APPLICATION_XML).
        mediaType("json", MediaType.APPLICATION_JSON);
    }
}
