package se.tain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CurrencyRates {
    @JsonProperty("base_currency")
    private String baseCurrency;
    private Metadata meta;
    private Map<String, Quote> quotes;

    @Data
    @AllArgsConstructor
    public static class Metadata {
        @JsonProperty("effective_params")
        private Params params;

        @Data
        @AllArgsConstructor
        public static class Params {
            private String date;
            @JsonProperty("quote_currencies")
            private List<String> quoteCurrencies;
        }
    }

    @Data
    @AllArgsConstructor
    public static class Quote {
        private String date;
        private Double midpoint;
    }

    public CurrencyRates copy() {
        Metadata.Params params = new Metadata.Params(
                this.meta.params.date,
                new ArrayList<>(this.meta.params.quoteCurrencies)
        );
        Metadata meta = new Metadata(params);

        Map<String, Quote> quotes = new HashMap<>(this.getQuotes());

        return new CurrencyRates(
                this.baseCurrency,
                meta,
                quotes
        );
    }
}