package se.tain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import se.tain.model.CurrencyRates;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class CurrencyRatesServiceTest {

    @InjectMocks
    private CurrencyRatesService service;

    @Before
    public void beforeTest() throws Exception {
        Map<String, CurrencyRates> testRates = new HashMap<>();

        URL url = Resources.getResource("rates_response_sample.json");
        String json = Resources.toString(url, Charsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        CurrencyRates currencyRates = mapper.readValue(json, CurrencyRates.class);
        testRates.put(currencyRates.getBaseCurrency(), currencyRates);

        ReflectionTestUtils.setField(service, "latestRates", testRates);
    }

    @Test
    public void save() throws Exception {
        URL url = Resources.getResource("rates_response_sample2.json");
        String json = Resources.toString(url, Charsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        CurrencyRates currencyRates = mapper.readValue(json, CurrencyRates.class);

        service.save(currencyRates);
        CurrencyRates saved = service.find("PLN");
        assertEquals(currencyRates, saved);
    }

    @Test
    public void findByBaseCurrency() throws Exception {
        URL url = Resources.getResource("rates_response_sample.json");
        String json = Resources.toString(url, Charsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        CurrencyRates currencyRates = mapper.readValue(json, CurrencyRates.class);

        CurrencyRates found = service.find("EUR");
        assertEquals(currencyRates, found);
    }

    @Test
    public void findBySourceAndTargetCurrency() throws Exception {
        CurrencyRates currencyRates = service.find("EUR", "UAH");
        assertNotNull(currencyRates);
    }

}