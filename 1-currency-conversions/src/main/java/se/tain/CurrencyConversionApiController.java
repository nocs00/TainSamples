package se.tain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.tain.model.CurrencyRates;
import se.tain.service.CurrencyRatesService;

@RestController
public class CurrencyConversionApiController {

    @Autowired
    private CurrencyRatesService currencyRatesService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCurrencyRates(
            @RequestParam MultipartFile file
    ) {
        try {
            byte[] bytes = file.getBytes();
            ObjectMapper mapper = new ObjectMapper();
            CurrencyRates currencyRates = mapper.readValue(bytes, CurrencyRates.class);
            currencyRatesService.save(currencyRates);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e);
        }
        return ResponseEntity.ok("File successfully uploaded");
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity getCurrencyRates(
            @PathVariable String code
    ) {
        CurrencyRates currencyRates = currencyRatesService.find(code);
        if (currencyRates == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(currencyRates);
    }

    @GetMapping("/rates/{sourceCurrency}/{targetCurrency}")
    public ResponseEntity getRate(
            @PathVariable String sourceCurrency,
            @PathVariable String targetCurrency
    ) {
        CurrencyRates currencyRates = currencyRatesService.find(sourceCurrency, targetCurrency);
        if (currencyRates == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(currencyRates);
    }

}
