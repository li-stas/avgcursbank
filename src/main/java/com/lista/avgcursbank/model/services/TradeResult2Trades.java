package com.lista.avgcursbank.model.services;

import com.lista.avgcursbank.model.AO_trade;
import com.lista.avgcursbank.model.Trades;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class TradeResult2Trades {
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;

    public Trades getTrades(List<Object[]> aResultList) {
        Trades oTrades = new Trades(SUCCESS_STATUS, CODE_SUCCESS); // список курсов банка
        for (Object[] a : aResultList) {
            //System.out.println("Result " + a[0]  + " "  + a[1]);
            AO_trade oTrade = new AO_trade();

            oTrade.setId_bank(999);
            oTrade.setName_bank("AvgRate");

            Date dateToConvert = (Date) a[0];
            LocalDate dt = new java.sql.Date(dateToConvert.getTime()).toLocalDate();
            oTrade.setDate_trade(dt);

            oTrade.setName_currency((String) a[1]);
            oTrade.setRateBuy((BigDecimal) a[2]);
            oTrade.setRateSell((BigDecimal) a[3]);
            oTrades.addTrade(oTrade);
        }
        return oTrades;
    }
}
