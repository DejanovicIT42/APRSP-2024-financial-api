package bankAccount;

import bankAccount.dtos.UserDto;
import bankAccount.dtos.UserProxy;
import bankAccount.exceptions.CustomExceptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-account")
public class BankAccountController {

    @Autowired
    private BankAccountRepository repo;

    @Autowired
    private Environment environment;

    @Autowired
    private UserProxy userProxy;

    @GetMapping("/get/{email}")
    public ResponseEntity<BankAccount> getBankAccount(@PathVariable String email) throws Exception {
        BankAccount bankAccount = repo.findByEmail(email);

        if (bankAccount == null) {
            throw new CustomExceptions.AccountNotFoundException("This bank account doesn't exist.");
        }

        bankAccount.setEnvironment(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody BankAccount account) throws Exception {
        if (repo.findByEmail(account.getEmail()) != null) {
            throw new CustomExceptions.OnlyOneBankAccountPerUserException("There can be only one bank account per user.");
        }


        ResponseEntity<UserDto> proxyResponse = userProxy.getUserByEmail(account.getEmail());
        if (proxyResponse.getStatusCode() != HttpStatus.OK) {
            throw new CustomExceptions.AccountNotFoundException("Cannot find user.");
        }
        UserDto user = proxyResponse.getBody();
        //ako nema body ili ako je Role != null
        if (user == null || user.getRole() != Role.USER) {
            throw new CustomExceptions.UnauthorizedAccountException("ADMIN can create bank account only for USER role.");
        }

        BankAccount createdAccount = repo.save(account);
        createdAccount.setEnvironment(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }

    @PutMapping("/{email}")
    public ResponseEntity<BankAccount> updateBankAccount(@PathVariable String email, @RequestBody BankAccount account, HttpServletRequest request) throws Exception {
        BankAccount checkUser = repo.findByEmail(email);
        if (checkUser == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        account.setEmail(email);

        ResponseEntity<UserDto> proxyResponse = userProxy.getUserByEmail(account.getEmail());
        if (proxyResponse.getStatusCode() != HttpStatus.OK) {
            throw new CustomExceptions.AccountNotFoundException("Cannot find user.");
        }
        UserDto user = proxyResponse.getBody();
        if (user == null || user.getRole() != Role.USER) {
            throw new CustomExceptions.UnauthorizedAccountException("ADMIN can update bank account only for USER role.");
        }

        BankAccount updatedAccount = repo.save(account);
        updatedAccount.setEnvironment(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable String email) throws Exception {
        BankAccount deleteAccount = repo.findByEmail(email);

        if (deleteAccount == null) {
            throw new CustomExceptions.AccountNotFoundException("This bank account does not exist.");
        }


        repo.deleteBankAccountByEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
