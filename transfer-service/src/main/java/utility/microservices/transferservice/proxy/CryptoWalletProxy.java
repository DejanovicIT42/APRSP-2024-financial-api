package utility.microservices.transferservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utility.microservices.transferservice.TransferCryptoValueDto;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {

    @GetMapping("/crypto-wallet/{email}")
    ResponseEntity<CryptoWalletDto> getCryptoWallet(@PathVariable String email);

    @PutMapping("/crypto-wallet/transfer")
    ResponseEntity<CryptoWalletDto> transferCryptoWallet(@RequestBody TransferCryptoValueDto transferDto);
}
