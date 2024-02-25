package usersService.dtos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {
    @PostMapping("/bank-account/{email}")
    public ResponseEntity<BankAccountDto> createBankAccount(@PathVariable String email);

    @PutMapping("/bank-account/from{oldEmail}to{newEmail}")
    public ResponseEntity<BankAccountDto> updateBankAccount(@PathVariable String oldEmail, @PathVariable String newEmail);

    @DeleteMapping("/bank-account/{email}")
    public ResponseEntity<BankAccountDto> deleteBankAccount(@PathVariable String email);
}
