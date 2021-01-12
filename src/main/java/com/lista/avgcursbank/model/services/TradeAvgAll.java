package com.lista.avgcursbank.model.services;

import com.lista.avgcursbank.model.Trades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 1) Запрос на список курсов по всем источникам, с величинами
 * средних курсов по рынку
 */
@Component
public class TradeAvgAll {
    private static final Logger log = LoggerFactory.getLogger(TradeAvgAll.class);

    private TradeResult2Trades tradeResult2Trades;
    private EntityManager em;

    public TradeAvgAll(TradeResult2Trades tradeResult2Trades, EntityManager em) {
        this.tradeResult2Trades = tradeResult2Trades;
        this.em = em;
    }

    public Trades eval() {
        Trades oTrades; // список курсов банка
        String hql = "SELECT CAST(t.date_trade AS DATE), t.name_currency, "
                + "round(AVG(rate_buy), 4) as rate_buy, ROUND(AVG(rate_sell), 4) as rate_sell "
                + "FROM AO_trade t GROUP BY t.date_trade, t.name_currency";

        Query query = em.createNativeQuery(hql);
        List<Object[]> aResultList = query.getResultList();

        oTrades = tradeResult2Trades.getTrades(aResultList);

        return oTrades;

    }


}

