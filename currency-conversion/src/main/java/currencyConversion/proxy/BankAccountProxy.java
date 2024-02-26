package currencyConversion.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {
    @GetMapping("/bank-account/[email}")
    ResponseEntity<BankAccountDto> getBankAccount(@PathVariable String email);

    @PutMapping("/bank-account/{email}/decrease/{quantityFrom}/from{currencyFrom}/increase/{quantityTo}/from{currencyTo}")
    ResponseEntity<BankAccountDto> updateAccountBalance(@PathVariable String email,
                                                        @PathVariable BigDecimal quantityFrom,
                                                        @PathVariable String currencyFrom,
                                                        @PathVariable BigDecimal quantityTo,
                                                        @PathVariable String currencyTo);
}
