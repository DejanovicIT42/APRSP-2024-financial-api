package utility.microservices.cryptoconversion.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {

    @GetMapping("/crypto-wallet/{email}")
    ResponseEntity<CryptoWalletDto> getCryptoWallet(@PathVariable String email);

    @PutMapping("/crypto-wallet/{email}/decrease/{quantityFrom}/from/{cryptoFrom}/increase/{quantityTo}/from/{cryptoTo}")
    ResponseEntity<CryptoWalletDto> updateCryptoBalance(@PathVariable String email,
                                                        @PathVariable BigDecimal quantityFrom,
                                                        @PathVariable String cryptoFrom,
                                                        @PathVariable BigDecimal quantityTo,
                                                        @PathVariable String cryptoTo,
                                                        @RequestHeader("X-User-Role") String userRole);
}
