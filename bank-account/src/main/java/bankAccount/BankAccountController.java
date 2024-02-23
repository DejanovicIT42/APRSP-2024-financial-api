package bankAccount;

import bankAccount.exceptions.CustomExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank-account")
public class BankAccountController {

    @Autowired
    private BankAccountRepository repo;

    @Autowired
    private Environment environment;

    @GetMapping("/get/{email}")
    public ResponseEntity<BankAccount> getBankAccount(@PathVariable String email) throws Exception {
        BankAccount bankAccount;

        try {
            bankAccount = repo.findByEmail(email);
        } catch (Exception e) {
            throw new CustomExceptions.AccountNotFoundException("This bank account doesn't exist.");
        }

        bankAccount.setEnvironment(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }
}
