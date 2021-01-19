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
 * Конвертация запроса от  MinFin в рабочий класс Trades
 */
@Component
public class Json2Trade02Converter implements Converter<String, Trades> {
    private static final Logger log = LoggerFactory.getLogger(Json2Trade02Converter.class);
    private final JsonStr2ObjMap jsonStr2ObjMap;

    public Json2Trade02Converter(JsonStr2ObjMap jsonStr2ObjMap) {
        this.jsonStr2ObjMap = jsonStr2ObjMap;
    }

    @Override
    public Trades convert(String cTrade) {

        Trades oTrades = new Trades(); // список курсов банка
        String[] aTTrade = oTrades.cTrade2aTrade(cTrade);

        int j = 0; // для быстрого вход после поиска данных

        for (String s : aTTrade) {

            Map<String, Object> mapTrade = jsonStr2ObjMap.getStringObjectMap(s);

            if (mapTrade != null) {
                AO_trade oTrade = new AO_trade();

                oTrade.setDate_trade(LocalDate.now());
                oTrade.setId_bank(2);
                oTrade.setName_bank("MinFin");

                oTrade.setRateBuy(new BigDecimal((String) mapTrade.get("bid")));
                oTrade.setRateSell(new BigDecimal((String) mapTrade.get("ask")));

                switch ((String) mapTrade.get("currency")) {
                    case "rub":
                        oTrade.setId_currency(3);
                        oTrade.setName_currency("rub");
                        j++;
                        break;
                    case "eur":
                        oTrade.setId_currency(2);
                        oTrade.setName_currency("eur");
                        j++;
                        break;
                    case "usd":
                        oTrade.setId_currency(1);
                        oTrade.setName_currency("usd");
                        j++;
                        break;
                }

                if (oTrade.getId_currency() != 0) {
                    oTrades.addTrade(oTrade);
                }
            }


            if (j == 3) {
                break;
            }
        }
        return oTrades;
    }
}

