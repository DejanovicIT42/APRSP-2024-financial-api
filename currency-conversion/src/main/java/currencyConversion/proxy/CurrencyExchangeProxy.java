package currencyConversion.proxy;

import currencyConversion.CurrencyConversion;
import jakarta.ws.rs.HeaderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    ResponseEntity<CurrencyConversion> getExchange(@PathVariable String from,
                                                   @PathVariable String to);
}

