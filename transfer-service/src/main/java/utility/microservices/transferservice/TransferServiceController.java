package utility.microservices.transferservice;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utility.microservices.transferservice.exceptions.CustomExceptions;
import utility.microservices.transferservice.proxy.BankAccountProxy;
import utility.microservices.transferservice.proxy.CryptoWalletProxy;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transfer-service")
public class TransferServiceController {

    @Autowired
    private CryptoWalletProxy walletProxy;

    @Autowired
    private BankAccountProxy accountProxy;

    @PostMapping()
    private ResponseEntity<?> transferBalance(@RequestParam String currency,
                                              @RequestParam String to,
                                              @RequestParam BigDecimal quantity,
                                              HttpServletRequest request) throws Exception{
        String requestEmail = request.getHeader("X-User-Email");

        if(currency.equals("EUR")  || currency.equals("GBP") || currency.equals("RSD") ||
            currency.equals("USD") || currency.equals("CHF")) {

            accountProxy.transferBalance(new TransferFiatValueDto(currency, quantity.multiply(new BigDecimal("0.01")), quantity, requestEmail, to));
            return new ResponseEntity<>("Transferring " + quantity + " " +
                    currency + " from " + requestEmail + " to " + to, HttpStatus.OK);

        }

        if (currency.equals("LUNA") || currency.equals("BTC") || currency.equals("ETH")) {
            walletProxy.transferCryptoWallet(new TransferCryptoValueDto(currency, quantity.multiply(new BigDecimal("0.01")), quantity, requestEmail, to));
            return new ResponseEntity<>("Transferring " + quantity + " " +
                    currency + " from " + requestEmail + " to " + to, HttpStatus.OK);
        }

        throw new CustomExceptions.YouCantDoThatException("This currency is not supported.");
    }
}
