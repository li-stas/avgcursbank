package com.lista.avgcursbank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * считывание конфигурационных данных
 */

@ConfigurationProperties(prefix = "ini.api")
@Component
public class SpringBootConfiguration {
    private String apikey = "######";
    private int timeOutForSingleRequests = 1;

    private String apibank01 = "api.monobank.ua/bank/currency";
    private String apibank02 = "api.minfin.com.ua/mb/676a622c03525d39cdae284f01d05d2b1f01602f/";
    private String apibank03 = "api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    public String getApibank01() {
        return apibank01;
    }

    public void setApibank01(String apibank01) {
        this.apibank01 = apibank01;
    }

    public String getApibank02() {
        return apibank02;
    }

    public void setApibank02(String apibank02) {
        this.apibank02 = apibank02;
    }

    public String getApibank03() {
        return apibank03;
    }

    public void setApibank03(String apibank03) {
        this.apibank03 = apibank03;
    }

    public int getTimeOutForSingleRequests() {
        return timeOutForSingleRequests;
    }
    public void setTimeOutForSingleRequests(int timeOutForSingleRequests) {
        this.timeOutForSingleRequests = timeOutForSingleRequests;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public String toString() {
        return "SpringBootConfiguration{"
                + ", timeOutForSingleRequests=" + timeOutForSingleRequests
                + ", apibank01='" + apibank01 + '\''
                + ", apibank02='" + apibank02 + '\''
                + ", apibank03='" + apibank03 + '\''
                + '}';
    }
}
