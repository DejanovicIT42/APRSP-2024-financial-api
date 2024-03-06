package utility.microservices.transferservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utility.microservices.transferservice.TransferFiatValueDto;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {
    @GetMapping("/bank-account/[email}")
    ResponseEntity<BankAccountDto> getBankAccount(@PathVariable String email);

    @PutMapping("/bank-account/transfer")
    ResponseEntity<BankAccountDto> transferBalance(@RequestBody TransferFiatValueDto transferDto);
}
