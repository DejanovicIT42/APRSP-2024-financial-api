package utility.microservices.cryptoconversion;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utility.microservices.cryptoconversion.exceptions.CustomExceptions;
import utility.microservices.cryptoconversion.proxy.CryptoExchangeProxy;
import utility.microservices.cryptoconversion.proxy.CryptoWalletDto;
import utility.microservices.cryptoconversion.proxy.CryptoWalletProxy;

import java.math.BigDecimal;

@RestController
@RequestMapping("/crypto-conversion")
public class CryptoConversionController {

    @Autowired
    private CryptoExchangeProxy exchangeProxy;

    @Autowired
    private CryptoWalletProxy walletProxy;

    @GetMapping()
    public ResponseEntity<?> getConversionFeign(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal quantity) {

        try {
            ResponseEntity<CryptoConversion> response = exchangeProxy.getExchange(from, to);
            CryptoConversion responseBody = response.getBody();
            return ResponseEntity.ok(new CryptoConversion(from, to, responseBody.getConversionMultiple(), responseBody.getEnvironment() + " feign",
                    quantity, responseBody.getConversionMultiple().multiply(quantity)));
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<CryptoWalletDto> getConversion(@RequestParam String from,
                                                         @RequestParam String to,
                                                         @RequestParam BigDecimal quantity,
                                                         HttpServletRequest request) throws Exception{
        String requestEmail = request.getHeader("X-User-Email");
        if(requestEmail == null)
            throw new CustomExceptions.NoContentFoundException("This email doesn't exist.");

        ResponseEntity<CryptoConversion> response = exchangeProxy.getExchange(from, to);

        CryptoConversion amount = response.getBody();
        if(amount == null)
            throw new CustomExceptions.NoContentFoundException("Cannot convert null.");

        BigDecimal toQuantity = amount.getConversionMultiple().multiply(quantity);

        ResponseEntity<CryptoWalletDto> updatedWallet = walletProxy.updateCryptoBalance(
                requestEmail, quantity, from, toQuantity, to, "ADMIN"
        );

        updatedWallet.getBody().setMessage("Conversion successful from " + quantity + " " + from + " to " + toQuantity + " " + to);

        return new ResponseEntity<>(updatedWallet.getBody(), HttpStatus.OK);
    }


}
