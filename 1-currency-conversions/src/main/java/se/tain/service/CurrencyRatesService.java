package se.tain.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.tain.model.CurrencyRates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyRatesService {

    /**
     * this supposed to be some storage: db, messaging system, file storage, etc
     */
    private Map<String, CurrencyRates> latestRates = new HashMap<>();

    public void save(CurrencyRates currencyRates) {
        latestRates.put(currencyRates.getBaseCurrency(), currencyRates);
        processMissingCurrencyRates(currencyRates);
    }

    public CurrencyRates find(String baseCurrency) {
        CurrencyRates currencyRates = latestRates.get(baseCurrency);
        if (currencyRates != null) {
            currencyRates = currencyRates.copy();
        }
        return currencyRates;
    }

    public CurrencyRates find(String sourceCurrency, String targetCurrency) {
        CurrencyRates currencyRates = find(sourceCurrency);
        if (currencyRates == null
                || !currencyRates.getMeta().getParams().getQuoteCurrencies().contains(targetCurrency)
                || !currencyRates.getQuotes().containsKey(targetCurrency)) {
            return null;
        }
        currencyRates.getMeta().getParams().setQuoteCurrencies(Arrays.asList(targetCurrency));
        Map<String, CurrencyRates.Quote> quotes = currencyRates.getQuotes();
        CurrencyRates.Quote quote = quotes.get(targetCurrency);
        quotes.clear();
        quotes.put(targetCurrency, quote);

        return currencyRates;
    }

    private void processMissingCurrencyRates(CurrencyRates origin) {
        List<String> quoteCurrencies = origin.getMeta().getParams().getQuoteCurrencies();
        for (String currency : quoteCurrencies) {
            List<String> newQouteCurrencies = new ArrayList<>(quoteCurrencies);
            newQouteCurrencies.add(origin.getBaseCurrency());
            newQouteCurrencies.remove(currency);

            CurrencyRates.Metadata.Params quoteParams = new CurrencyRates.Metadata.Params(
                    origin.getMeta().getParams().getDate(),
                    newQouteCurrencies
            );

            CurrencyRates.Metadata meta = new CurrencyRates.Metadata(quoteParams);

            Map<String, CurrencyRates.Quote> newQuotes = new HashMap<>();
            final String from = currency;
            for (String to : newQouteCurrencies) {
                CurrencyRates.Quote newQuote = calculateQoute(from, to, origin);
                String date = from.equals(origin.getBaseCurrency()) ?
                        origin.getQuotes().get(to).getDate() :
                        origin.getQuotes().get(from).getDate();
                newQuote.setDate(date);
                newQuotes.put(to, newQuote);
            }

            latestRates.put(currency, new CurrencyRates(
                    currency,
                    meta,
                    newQuotes
            ));
        }
    }

    private CurrencyRates.Quote calculateQoute(String from, String to, CurrencyRates origin) {
        Double numerator = 1.;
        if (!to.equals(origin.getBaseCurrency())) {
            numerator = origin.getQuotes().get(to).getMidpoint();
        }
        Double denominator = origin.getQuotes().get(from).getMidpoint();;

        return new CurrencyRates.Quote(null, (numerator/denominator));
    }
}
