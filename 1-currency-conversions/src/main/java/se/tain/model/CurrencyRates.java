package se.tain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CurrencyRates {
    @JsonProperty("base_currency")
    private String baseCurrency;
    private Metadata meta;
    private Map<String, Quote> quotes;

    @Data
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
}