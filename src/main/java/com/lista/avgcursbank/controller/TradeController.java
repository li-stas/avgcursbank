package com.lista.avgcursbank.controller;

import com.lista.avgcursbank.model.Trades;
import com.lista.avgcursbank.model.services.ScheduledTasksCron;
import com.lista.avgcursbank.model.services.TradeAvgAll;
import com.lista.avgcursbank.model.services.TradeAvgPeriod;
import com.lista.avgcursbank.repository.AO_tradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// /tacbank/v1/tradeadd

@RestController
@EnableSwagger2
@RequestMapping("/tacbank/v1")
public class TradeController {

    private final ScheduledTasksCron scheduledTasksCron;
    private final TradeAvgAll tradeAvgAll;
    private final TradeAvgPeriod tradeAvgPeriod;
    @Autowired
    private AO_tradeRepository tradeRepository;

    public TradeController(ScheduledTasksCron scheduledTasksCron, TradeAvgAll tradeAvgAll
            , TradeAvgPeriod tradeAvgPeriod) {

        this.scheduledTasksCron = scheduledTasksCron;
        this.tradeAvgAll = tradeAvgAll;
        this.tradeAvgPeriod = tradeAvgPeriod;
    }

    /**
     * 2) Запрос на выдачу списка средних курсов по всем источникам за      * период
     *
     * @param cDt1 в формате ГГГГ-ММ-ДД
     * @param cDt2 в формате ГГГГ-ММ-ДД
     * @return
     */
    // GET method
    @GetMapping("/tradavgperiod")
    public ResponseEntity<Trades> TradAvgPeriod(
            @RequestParam(value = "date1", required = false, defaultValue = "2021-01-06") String cDt1,
            @RequestParam(value = "date2", required = false, defaultValue = "2021-01-06") String cDt2
    ) {
        Trades findAvgPeriod = null;
        findAvgPeriod = tradeAvgPeriod.eval(cDt1, cDt2);

        return ResponseEntity.ok().body(findAvgPeriod);
    }

    /**
     * 1) Запрос на список курсов по всем источникам, с величинами  средних курсов по рынку
     *
     * @return
     */
    // GET method
    @GetMapping("/tradavgrate")
    public ResponseEntity<Trades> tradAvgRate() {
        Trades findAvgRate = null;
        findAvgRate = tradeAvgAll.eval();
        return ResponseEntity.ok().body(findAvgRate);
    }

    /**
     * загрузк чз запрос данных за текущий день
     *
     * @return
     */
    // GET method to create a phone
    @GetMapping("/tradeadd")
    public ResponseEntity<Trades> addTrade() {
        Trades trades = scheduledTasksCron.scheduleTaskWithCronExpression();
        return ResponseEntity.ok().body(trades);
    }
/*
    // GET method to fetch all phones
    @GetMapping("/tradelist")
    public List<AO_trade> getAllPhones() {
        return tradeRepository.findAll();
    }

    // GET method to fetch phone by Id
    @GetMapping("/tradeget/{id}")
    public ResponseEntity<AO_trade> getTradeById(@PathVariable(value = "id") Integer idTrade)
            throws Exception {
        AO_trade trade = tradeRepository.findById(idTrade)
                .orElseThrow(() -> new Exception("trade " + idTrade + " not found"));
        return ResponseEntity.ok().body(trade);
    }

    // POST method to create a phone
    @PostMapping("/tradeone")
    public AO_trade createTrade(@Valid @RequestBody AO_trade trade) {
        return tradeRepository.save(trade);
    }

    // PUT method to update a phone's details
    @PutMapping("/tradeupdate/{id}")
    public ResponseEntity<AO_trade> updateTrade(
            @PathVariable(value = "id") Integer idTrade, @Valid @RequestBody AO_trade tradeDetails
    ) throws Exception {
        AO_trade trade = tradeRepository.findById(idTrade)
                .orElseThrow(() -> new Exception("trade " + idTrade + " not found"));

        trade.setDate_trade(tradeDetails.getDate_trade());
        trade.setId_bank(tradeDetails.getId_bank());
        trade.setName_bank(tradeDetails.getName_bank());
        trade.setId_currency(tradeDetails.getId_currency());
        trade.setName_currency(tradeDetails.getName_currency());
        trade.setRateBuy(tradeDetails.getRateBuy());
        trade.setRateSell(tradeDetails.getRateSell());

        final AO_trade updatedPhone = tradeRepository.save(trade);
        return ResponseEntity.ok(updatedPhone);
    }

    // DELETE method to delete a phone
    @DeleteMapping("/tradedele/{id}")
    public Map<String, Boolean> deleteTrade(@PathVariable(value = "id") Integer idTrade) throws Exception {
        AO_trade trade = tradeRepository.findById(idTrade)
                .orElseThrow(() -> new Exception("trade " + idTrade + " not found"));

        tradeRepository.delete(trade);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }*/
}
