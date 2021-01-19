package com.lista.avgcursbank.model.services;

import com.lista.avgcursbank.config.SpringBootConfiguration;
import com.lista.avgcursbank.model.AO_trade;
import com.lista.avgcursbank.model.Trades;
import com.lista.avgcursbank.repository.AO_tradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * На регулярной основе веб приложение загружает данные из
 * стороннего сервиса во внутреннюю базу данных.
 *
 * 2) Cron job - для реализации синхронизации данных с api
 * провайдеров.
 */
@Component
public class ScheduledTasksCron {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasksCron.class);
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:
    private final LookUpBankCurs lookUpBankCurs;
    private AO_tradeRepository tradeRepository;

    public ScheduledTasksCron(SpringBootConfiguration config, LookUpBankCurs lookUpBankCurs, AO_tradeRepository tradeRepository) {
        this.config = config;
        this.lookUpBankCurs = lookUpBankCurs;
        this.tradeRepository = tradeRepository;
    }

    //@Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "${job.cron.rate}")
    public Trades scheduleTaskWithCronExpression() {
        log.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        Trades trades;
        trades = getTrade(1, config.getApibank01(), config);
        trades = getTrade(2, config.getApibank02(), config);
        trades = getTrade(3, config.getApibank03(), config);
        return trades;
    }

    private Trades getTrade(int nId_Bank, String cUrlApiBank, SpringBootConfiguration config) {
        LocalDate dt = LocalDate.now();
        List<AO_trade> aoTrade = null;
        aoTrade = tradeRepository.findByDateTradeIdBank(dt, nId_Bank);
        Trades trades = new Trades(SUCCESS_STATUS, CODE_SUCCESS);
        if (aoTrade.size() == 0) {
            trades = lookUpBankCurs.eval(nId_Bank, cUrlApiBank, config);
            if (trades != null) {
                List<AO_trade> aoTradeList = trades.getTrades();
                for (AO_trade trade : aoTradeList) {
                    tradeRepository.save(trade);
                }
            }
        }
        return trades;
    }
}
