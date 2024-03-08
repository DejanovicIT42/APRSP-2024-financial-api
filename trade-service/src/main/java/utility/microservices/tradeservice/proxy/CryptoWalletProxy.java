package utility.microservices.tradeservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {

    @GetMapping("/crypto-wallet/{email}")
    ResponseEntity<CryptoWalletDto> getCryptoWallet(@PathVariable String email);

    @PutMapping("/crypto-wallet/{email}")
    ResponseEntity<CryptoWalletDto> updateCryptoWallet(@PathVariable String email,
                                                       @RequestBody CryptoWalletDto wallet);
}
