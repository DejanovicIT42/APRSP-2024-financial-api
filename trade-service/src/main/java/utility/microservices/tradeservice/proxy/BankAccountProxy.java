package utility.microservices.tradeservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {

    @GetMapping("/bank-account/get/{email}")
    ResponseEntity<BankAccountDto> getBankAccount(@PathVariable String email);

    @PutMapping("/bank-account/{email}")
    ResponseEntity<BankAccountDto> updateBankAccount(@PathVariable String email,
                                                     @RequestBody BankAccountDto account);
}
