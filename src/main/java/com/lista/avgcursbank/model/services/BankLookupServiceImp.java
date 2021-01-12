package com.lista.avgcursbank.model.services;


import com.lista.avgcursbank.model.Trades;
import com.lista.avgcursbank.model.converters.Json2Trade01Converter;
import com.lista.avgcursbank.model.converters.Json2Trade02Converter;
import com.lista.avgcursbank.model.converters.Json2Trade03Converter;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 2. сервис запросов к GitHub для поиска страниц.
 */
@Service //делая его кандидатом для сканирования компонентов в Spring для обнаружения и помещения его в
public class BankLookupServiceImp implements BankLookupService { //implements SiteLookupService
    private static final Logger log = LoggerFactory.getLogger(BankLookupServiceImp.class);

    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;

    // вариант инициализаци без конструктора private RestTemplate restTemplate = new RestTemplate();
    private final RestTemplate restTemplate;
    private final ConversionService conversionService;
    private final Json2Trade01Converter json2Trade01Converter;
    private final Json2Trade02Converter json2Trade02Converter;
    private final Json2Trade03Converter json2Trade03Converter;

    public BankLookupServiceImp(RestTemplateBuilder restTemplateBuilder, ConversionService conversionService
            , Json2Trade01Converter json2Trade01Converter, Json2Trade02Converter json2Trade02Converter
            , Json2Trade03Converter json2Trade03Converter) {
        this.restTemplate = restTemplateBuilder.build();
        this.conversionService = conversionService;
        this.json2Trade01Converter = json2Trade01Converter;
        this.json2Trade02Converter = json2Trade02Converter;
        this.json2Trade03Converter = json2Trade03Converter;
    }

    /**
     * получение данных чз API , конвертация ответа в рабочий класс
     * @param cApiKey -
     * @param cSeekId -
     * @return -
     */
    @Async //будет запущен в отдельном потоке
    public Future<Trades> findPage(int nId_Bank, String cMainUrl, String cUrlTypeSeek, String cApiKey, String cSeekId) {

        String cTrade = "Error\":";
        String url = "";
        // начальный ЮРЛ mainUrl - подстановка чз Map<String, String>
        url = "https://{mainUrl}";
        // URI (URL) parameters
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("mainUrl", cMainUrl);
        // Query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        // Add query parameter
        if (cApiKey.length() !=0 ) {
            builder.queryParam("/apikey", cApiKey);
        }
        if (cSeekId.length() !=0 ) {
            builder.queryParam(cUrlTypeSeek, cSeekId);
        }

        url = builder.buildAndExpand(urlParams).toUri().toString();
        log.info("  Looking up URL " +  url.replace("a625f1bf","ffffffff"));

        if (nId_Bank != 2) {
            cTrade = restTemplate.getForObject(url, String.class);
        } else {
            cTrade = tryme(url);
        }
        Trades oTrade = getPage(nId_Bank, cTrade);

        return new AsyncResult<Trades>(oTrade);  //AsyncResult требование любого асинхронного сервиса
    }

    private Trades getPage(int nId_Bank, String cTrade) {
        Trades oTrade = new Trades(ERROR_STATUS + " " + cTrade, AUTH_FAILURE);
        if (!(cTrade == null || cTrade.contains("Error\":"))  ) {
            log.info("Call conversionService");
            switch (nId_Bank) {
                case 1:
                    oTrade = json2Trade01Converter.convert(cTrade);
                    break;
                case 2:
                    oTrade = json2Trade02Converter.convert(cTrade);
                    break;
                case 3:
                    // oTrade = new Json2Trade03Converter().convert(cTrade);
                    oTrade = conversionService.convert(cTrade, Trades.class);
                    break;
            }
            //oTrade = conversionService.convert(cTrade, Trade.class);
            if (oTrade != null) {
                oTrade.setStatus(SUCCESS_STATUS);
                oTrade.setCode(CODE_SUCCESS);
            }
        }
        return oTrade;
    }

    private String tryme(String urlOverHttps) {

        RestTemplate restTemplate = null;
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            restTemplate = new RestTemplate(requestFactory);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            System.out.println("- Error downloading SSLConnectionSocketFactory");
            e.printStackTrace();
        }

        String responseCode = null;
        try {
            responseCode = restTemplate.getForObject(urlOverHttps, String.class);
        } catch (RestClientException e) {
            log.error(e.getMessage() + " RestClientException ", e);
            //e.printStackTrace();
        }
        return responseCode;
    }

}
