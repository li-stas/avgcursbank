package com.lista.avgcursbank.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * рабочий класс для обработки данных и обслуживание сервисов
 */
public class Trades {
    private static final transient String ERROR_STATUS = "error";
    private static final transient int AUTH_FAILURE = 102;

    private List<AO_trade> trades;
    private String status;
    private int code;

    public Trades() {
        this.status = ERROR_STATUS;
        this.code = AUTH_FAILURE;
        trades = new ArrayList<>();
    }

    public Trades(String status, int code) {
        this.status = status;
        this.code = code;
        trades = new ArrayList<>();
    }

    /**
     *  приведение json ответа массиву отдельных json
     * @param cTrade
     * @return
     */
    public String[] cTrade2aTrade(String cTrade) {
        cTrade = cTrade.replace("[", "");
        cTrade = cTrade.replace("]", ",");
        String[] aTTrade = cTrade.split("},");
        for (int i = 0; i < aTTrade.length; i++) {
            aTTrade[i] += "}";
        }
        return aTTrade;
    }

    public List<AO_trade> getTrades() {
        return trades;
    }

    public void addTrade(AO_trade trade) {
        trades.add(trade);
    }
    public void removeTrade(AO_trade trade) {
        trades.remove(trade);
    }

    public void setTrades(List<AO_trade> trades) {
        this.trades = trades;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trades trades = (Trades) o;
        return code == trades.code
                && Objects.equals(trades, trades.trades)
                && Objects.equals(status, trades.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trades, status, code);
    }

    @Override
    public String toString() {
        return "TradeBank{"
                + "trades=" + trades
                + ", status='" + status + '\''
                + ", code=" + code
                + '}';
    }
}
