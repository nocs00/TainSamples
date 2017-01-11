package se.tain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.tain.model.CurrencyRates;
import se.tain.service.CurrencyRatesService;

@RestController
public class CurrencyConversionApiController {
    // TODO: implement me
    // - All possible currency rates
    // - Currency rate by source currency and target currency

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
            ResponseEntity.badRequest().body("Failed to upload file: " + e);
        }
        return ResponseEntity.ok("File successfully uploaded");
    }

}
