package com.lista.avgcursbank.model.converters;

import com.lista.avgcursbank.model.AO_trade;
import com.lista.avgcursbank.model.Trades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Конвертация запроса от  Приват банка в рабочий класс Trades
 */
@Component
public class Json2Trade03Converter implements Converter<String, Trades> {
    private static final Logger log = LoggerFactory.getLogger(Json2Trade03Converter.class);
    private final JsonStr2ObjMap jsonStr2ObjMap;

    public Json2Trade03Converter(JsonStr2ObjMap jsonStr2ObjMap) {
        this.jsonStr2ObjMap = jsonStr2ObjMap;
    }


    @Override
    public Trades convert(String cTrade) {

        Trades oTrades = new Trades(); // список курсов банка
        String[] aTTrade = oTrades.cTrade2aTrade(cTrade);

        for (String s : aTTrade) {

            Map<String, Object> mapTrade = jsonStr2ObjMap.getStringObjectMap(s);

            if (mapTrade != null) {

                AO_trade oTrade = new AO_trade();

                oTrade.setDate_trade(LocalDate.now());
                oTrade.setId_bank(3);
                oTrade.setName_bank("Privat");

                oTrade.setRateBuy(new BigDecimal((String) mapTrade.get("buy")));
                oTrade.setRateSell(new BigDecimal((String) mapTrade.get("sale")));

                switch ((String) mapTrade.get("ccy")) {
                    case "USD":
                        oTrade.setId_currency(3);
                        oTrade.setName_currency("usd");
                        break;
                    case "EUR":
                        oTrade.setId_currency(2);
                        oTrade.setName_currency("eur");
                        break;
                    case "RUR":
                        oTrade.setId_currency(1);
                        oTrade.setName_currency("rub");
                        break;
                }

                if (oTrade.getId_currency() != 0) {
                    oTrades.addTrade(oTrade);
                }
            }

        }
        return oTrades;
    }
}
