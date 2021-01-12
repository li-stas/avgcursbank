package com.lista.avgcursbank.model.services;

import com.lista.avgcursbank.config.SpringBootConfiguration;
import com.lista.avgcursbank.model.Trades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * сервис преварительной подготовки для получения данных чз API
 * Future<Trades> result подготавливаню в этом класск для возможной
 * реалицции многопоточных запросов
 */
@Service
public class LookUpBankCursImp implements LookUpBankCurs {
    private static final Logger log = LoggerFactory.getLogger(LookUpBankCursImp.class);
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;
    private static final int PAUSE_CHCHECK = 10;

    private final BankLookupService bankLookupService;

    public LookUpBankCursImp(BankLookupService bankLookupService) {
        this.bankLookupService = bankLookupService;
    }

    public Trades eval(int nId_Bank, String cUrl, SpringBootConfiguration config) {
        Trades oTrade01 = getNewTrade();
        String cUrlTypeSeek = "";
        String cSeekId = "";
        String cApiKey = "";
        Future<Trades> result = bankLookupService.findPage(nId_Bank, cUrl, cUrlTypeSeek, cApiKey, cSeekId);
        try {
            int timeOut = waitIsDone(result, config);
            if (timeOut > 0) {
                log.info("  // запись в экзепляр класса время чтения, с "
                        + ((config.getTimeOutForSingleRequests() * 1000) - timeOut)/1000 );
                oTrade01 = result.get();
            } else {
                log.info("Время ожидания превышено timeOutForSingleRequests, с" + config.getTimeOutForSingleRequests() );
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage() + " InterruptedException", e);
        } catch (ExecutionException e) {
            log.error(e.getMessage() + "ExecutionException", e);
        }

        checkStatusTrade(cUrl, oTrade01);
        return oTrade01;
    }

    private Trades getNewTrade() {
        Trades oTrade = new Trades(SUCCESS_STATUS, CODE_SUCCESS);
        oTrade.setStatus(ERROR_STATUS);
        oTrade.setCode(AUTH_FAILURE);
        return oTrade;
    }

    private int waitIsDone(Future<Trades> result, SpringBootConfiguration config) throws InterruptedException {
        int timeOut = config.getTimeOutForSingleRequests() * 1000; // значение в секундах
        while (!result.isDone()) {
            Thread.sleep(PAUSE_CHCHECK); //millisecond pause between each check
            timeOut -= PAUSE_CHCHECK;
            if (timeOut <= 0) {
                break;
            }
        }
        return timeOut;
    }

    private void checkStatusTrade(String cUrl, Trades oTrade01) {
        String cStatus = cUrl;
        if (oTrade01.getCode() == 102) {
            cStatus = "  Ошибвка поиска URL=" + cUrl;
            log.info(cStatus + "  cTrade01:" + oTrade01.getStatus());
        } else {
            log.info("oTrade01:" + oTrade01);
        }
        oTrade01.setStatus(oTrade01.getStatus() + " " + cStatus);
    }
}
