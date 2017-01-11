package se.tain.service;

import se.tain.model.CurrencyRates;
import se.tain.util.CurrencyRatesConverter;

import java.util.HashMap;
import java.util.Map;

public class CurrencyRatesService {

    /**
     * this supposed to be some storage: db, messaging service, etc
     */
    private Map<String, Map<String, CurrencyRates.Quote>> latestRates = new HashMap<>();

    public void save(CurrencyRates currencyRates) {
        Map.Entry<String, Map<String, CurrencyRates.Quote>> newEntry = CurrencyRatesConverter.toEntry(currencyRates);
        Map<String, CurrencyRates.Quote> oldEntry = latestRates.get(currencyRates.getBaseCurrency());
        if (oldEntry != null) {
            newEntry.getValue().putAll(oldEntry);
        }

        latestRates.put(newEntry.getKey(), newEntry.getValue());
    }
}
