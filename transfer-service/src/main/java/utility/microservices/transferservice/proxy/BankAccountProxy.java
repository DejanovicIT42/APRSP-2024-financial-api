package utility.microservices.transferservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {
    @GetMapping("/bank-account/[email}")
    ResponseEntity<BankAccountDto> getBankAccount(@PathVariable String email);


    //todo: transfer izmedju bank accounta
}
