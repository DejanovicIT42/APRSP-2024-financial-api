package utility.microservices.tradeservice.proxy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.HeaderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "currency-conversion")
public interface CurrencyConversionProxy {

    @PostMapping("/currency-conversion")
    ResponseEntity<CurrencyConversionDto> getConversion(@RequestParam String from,
                                    @RequestParam String to,
                                    @RequestParam BigDecimal quantity,
                                    @HeaderParam("X-User-Email") String email);
}
