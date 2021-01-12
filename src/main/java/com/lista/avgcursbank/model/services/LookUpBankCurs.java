package com.lista.avgcursbank.model.services;

import com.lista.avgcursbank.config.SpringBootConfiguration;
import com.lista.avgcursbank.model.Trades;

public interface LookUpBankCurs {
    public Trades eval(int nId_Bank, String cUrl, SpringBootConfiguration config);
}
