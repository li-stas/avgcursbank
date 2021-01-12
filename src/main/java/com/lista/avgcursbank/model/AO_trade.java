package com.lista.avgcursbank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lista.avgcursbank.model.converters.LocalDateAttributeConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * класс ORM для обмена данными между приложение и БД
 */
@Entity
@Table (name = "ao_trade")
@EntityListeners(AuditingEntityListener.class)
public class AO_trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "date_trade")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date_trade;

    @Column(name = "id_bank", nullable = false)
    private int id_bank;
    @Column(name = "name_bank", nullable = false)
    private String name_bank;

    @Column(name = "id_currency", nullable = false)
    private int id_currency;
    @Column(name = "name_currency", nullable = false)
    private String name_currency;

    @Column(name = "rate_buy", nullable = false)
    private BigDecimal rateBuy;

    @Column(name = "rate_sell", nullable = false)
    private BigDecimal rateSell;

    public AO_trade() {
    }

    public AO_trade(int id, LocalDate date_trade, int id_bank, String name_bank, int id_currency,
                    String name_currency, BigDecimal rateBuy, BigDecimal rateSell) {
        this.id = id;
        this.date_trade = date_trade;
        this.id_bank = id_bank;
        this.name_bank = name_bank;
        this.id_currency = id_currency;
        this.name_currency = name_currency;
        this.rateBuy = rateBuy;
        this.rateSell = rateSell;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate_trade() {
        return date_trade;
    }

    public void setDate_trade(LocalDate date_trade) {
        this.date_trade = date_trade;
    }

    public int getId_bank() {
        return id_bank;
    }

    public void setId_bank(int id_bank) {
        this.id_bank = id_bank;
    }

    public String getName_bank() {
        return name_bank;
    }

    public void setName_bank(String name_bank) {
        this.name_bank = name_bank;
    }

    public int getId_currency() {
        return id_currency;
    }

    public void setId_currency(int id_currency) {
        this.id_currency = id_currency;
    }

    public String getName_currency() {
        return name_currency;
    }

    public void setName_currency(String name_currency) {
        this.name_currency = name_currency;
    }

    public BigDecimal getRateBuy() {
        return rateBuy;
    }

    public void setRateBuy(BigDecimal rateBuy) {
        this.rateBuy = rateBuy;
    }

    public BigDecimal getRateSell() {
        return rateSell;
    }

    public void setRateSell(BigDecimal rateSell) {
        this.rateSell = rateSell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AO_trade ao_trade = (AO_trade) o;
        return id == ao_trade.id &&
                id_bank == ao_trade.id_bank &&
                id_currency == ao_trade.id_currency &&
                rateBuy == ao_trade.rateBuy &&
                rateSell == ao_trade.rateSell &&
                Objects.equals(date_trade, ao_trade.date_trade) &&
                Objects.equals(name_bank, ao_trade.name_bank) &&
                Objects.equals(name_currency, ao_trade.name_currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date_trade, id_bank, name_bank, id_currency, name_currency, rateBuy, rateSell);
    }

    @Override
    public String toString() {
        return "AO_trade{" +
                "id=" + id +
                ", date_trade=" + date_trade +
                ", id_bank=" + id_bank +
                ", name_bank='" + name_bank + '\'' +
                ", id_currency=" + id_currency +
                ", name_currency='" + name_currency + '\'' +
                ", rateBuy=" + rateBuy +
                ", rateSell=" + rateSell +
                '}';
    }
}
