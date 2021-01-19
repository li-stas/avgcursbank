package com.lista.avgcursbank.model.converters;

import com.lista.avgcursbank.model.AO_trade;
import com.lista.avgcursbank.model.Trades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

/**
 * Конвертация запроса от  Монобанка в рабочий класс Trades
 */
@Component
public class Json2Trade01Converter implements Converter<String, Trades> {
    private static final Logger log = LoggerFactory.getLogger(Json2Trade01Converter.class);
    private final JsonStr2ObjMap jsonStr2ObjMap;

    public Json2Trade01Converter(JsonStr2ObjMap jsonStr2ObjMap) {
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
                oTrade.setId_bank(1);
                oTrade.setName_bank("MonoBank");

                oTrade.setRateBuy(BigDecimal.valueOf(((Number) mapTrade.get("rateBuy")).doubleValue()));
                oTrade.setRateBuy(BigDecimal.valueOf(((Number) mapTrade.get("rateSell")).doubleValue()));
                /*oTrade.setRateSell(BigDecimal.valueOf((Double) mapTrade.get("rateBuy")));
                oTrade.setRateSell(BigDecimal.valueOf((Double) mapTrade.get("rateSell")));*/

                switch ((Integer) mapTrade.get("currencyCodeA")) {
                    case 840: //"USD":
                        oTrade.setId_currency(3);
                        oTrade.setName_currency("usd");
                        j++;
                        break;
                    case 978: //"EUR":
                        oTrade.setId_currency(2);
                        oTrade.setName_currency("eur");
                        j++;
                        break;
                    case 643: //"RUR":
                        oTrade.setId_currency(1);
                        oTrade.setName_currency("rub");
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

    /*private Map<String, Object> getStringObjectMap(String s) {
        String cTrade;
        cTrade = s;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapTrade = null;
        try {
            mapTrade = mapper.readValue(cTrade, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return mapTrade;
    }*/

    private  BigDecimal getBigDecimal( Object value ) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger ) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                //ret = new BigDecimal( ((Number)value).doubleValue() );
                ret = BigDecimal.valueOf(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }
}

