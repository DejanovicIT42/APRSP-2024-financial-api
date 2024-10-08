package usersService.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {

    @PostMapping("/crypto-wallet/{email}")
    ResponseEntity<CryptoWalletDto> createCryptoWallet(@PathVariable String email);

    @PutMapping("/crypto-wallet/from{oldEmail}to{newEmail}")
    ResponseEntity<CryptoWalletDto> updateCryptoWallet(@PathVariable String oldEmail, @PathVariable String newEmail);

    @DeleteMapping("/crypto-wallet/{email}")
    ResponseEntity<CryptoWalletDto> deleteCryptoWallet(@PathVariable String email);
}
