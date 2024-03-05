package currencyConversion;

import currencyConversion.exceptions.CustomExceptions;
import currencyConversion.proxy.BankAccountDto;
import currencyConversion.proxy.BankAccountProxy;
import currencyConversion.proxy.CurrencyExchangeProxy;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy exchangeProxy;

    @Autowired
    private BankAccountProxy bankAccountProxy;

    //localhost:8100/currency-conversion/from/EUR/to/RSD/quantity/100
    @PostMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<BankAccountDto> getConversion(@PathVariable String from,
                                                        @PathVariable String to,
                                                        @PathVariable BigDecimal quantity,
                                                        HttpServletRequest request) throws Exception {

        String requestEmail = request.getHeader("X-User-Email");
        if (requestEmail == null)
            throw new CustomExceptions.NoContentFoundException("Requested email doesn't exist");

        ResponseEntity<CurrencyConversion> response = exchangeProxy.getExchange(
                from,
                to
        );

        CurrencyConversion cc = response.getBody();
        if (cc == null)
            throw new CustomExceptions.NoContentFoundException("Cannot convert null.");

        BigDecimal toQuantity = cc.getConversionMultiple().multiply(quantity);


        ResponseEntity<BankAccountDto> updatedAccount = bankAccountProxy.updateAccountBalance(
                requestEmail, quantity, from, toQuantity, to, "ADMIN"
        );

        updatedAccount.getBody().setMessage("Conversion successful from " + quantity + " " + from + " to " + toQuantity + " " + to);

        return new ResponseEntity<>(updatedAccount.getBody(), HttpStatus.OK);
    }


    //localhost:8100/currency-conversion-feign?from=EUR&to=RSD&quantity=50
    @GetMapping()
    public ResponseEntity<?> getConversionFeign(@RequestParam String from, @RequestParam String to, @RequestParam double quantity) {

        try {
            ResponseEntity<CurrencyConversion> response = exchangeProxy.getExchange(from, to);
            CurrencyConversion responseBody = response.getBody();
            return ResponseEntity.ok(new CurrencyConversion(from, to, responseBody.getConversionMultiple(), responseBody.getEnvironment() + " feign",
                    quantity, responseBody.getConversionMultiple().multiply(BigDecimal.valueOf(quantity))));
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.getMessage());
        }
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String parameter = ex.getParameterName();
        //return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body("Value [" + ex.getParameterType() + "] of parameter [" + parameter + "] has been ommited");
    }

}
