package com.lista.avgcursbank.model.services;

import com.lista.avgcursbank.model.Trades;

import java.util.concurrent.Future;

public interface BankLookupService {
    Future<Trades> findPage(int nId_Bank, String cMainUrl, String cUrlTypeSeek, String apikey, String cSeekKey);
}
