package com.lista.avgcursbank.repository;

import com.lista.avgcursbank.model.AO_trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AO_tradeRepository extends JpaRepository<AO_trade, Integer> {
    @Query("select t from AO_trade t WHERE t.date_trade = ?1 and t.id_bank = ?2")
    List<AO_trade> findByDateTradeIdBank(LocalDate date_trade, Integer id_bank);
}
