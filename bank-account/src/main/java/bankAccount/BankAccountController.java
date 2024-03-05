package bankAccount;

import bankAccount.exceptions.CustomExceptions;
import bankAccount.proxy.UserDto;
import bankAccount.proxy.UserProxy;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

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
        //if no body exists ili ako je Role != null
        if (user == null || user.getRole() != Role.USER) {
            throw new CustomExceptions.UnauthorizedAccountException("ADMIN can create bank account only for USER role.");
        }

        BankAccount createdWallet = repo.save(account);
        createdWallet.setEnvironment(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(createdWallet, HttpStatus.OK);
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

    //amount umesto quantity?
    @PutMapping("/{email}/decrease/{quantityFrom}/from/{currencyFrom}/increase/{quantityTo}/from/{currencyTo}")
    public ResponseEntity<BankAccount> updateAccountBalance(
            @PathVariable String email,
            @PathVariable BigDecimal quantityFrom, @PathVariable String currencyFrom,
            @PathVariable BigDecimal quantityTo, @PathVariable String currencyTo) throws Exception {
        BankAccount account = repo.findByEmail(email);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (Objects.equals(currencyFrom, currencyTo))
            throw new CustomExceptions.YouCantDoThatException("Cannot convert from " + currencyFrom + " to " + currencyTo);

        switch (currencyFrom) {
            case "EUR":
                if (account.getEUR_amount().compareTo(quantityFrom) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setEUR_amount(account.getEUR_amount().subtract(quantityFrom));
                break;
            case "USD":
                if (account.getUSD_amount().compareTo(quantityFrom) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setUSD_amount(account.getUSD_amount().subtract(quantityFrom));
                break;
            case "GBP":
                if (account.getGBP_amount().compareTo(quantityFrom) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setGBP_amount(account.getGBP_amount().subtract(quantityFrom));
                break;
            case "CHF":
                if (account.getCHF_amount().compareTo(quantityFrom) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setCHF_amount(account.getCHF_amount().subtract(quantityFrom));
                break;
            case "RSD":
                if (account.getRSD_amount().compareTo(quantityFrom) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setRSD_amount(account.getRSD_amount().subtract(quantityFrom));
                break;
        }

        switch (currencyTo) {
            case "EUR":
                if (account.getEUR_amount().compareTo(quantityTo) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setEUR_amount(account.getEUR_amount().add(quantityTo));
                break;
            case "USD":
                if (account.getUSD_amount().compareTo(quantityTo) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setUSD_amount(account.getUSD_amount().add(quantityTo));
                break;
            case "GBP":
                if (account.getGBP_amount().compareTo(quantityTo) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setGBP_amount(account.getGBP_amount().add(quantityTo));
                break;
            case "CHF":
                if (account.getCHF_amount().compareTo(quantityTo) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setCHF_amount(account.getCHF_amount().add(quantityTo));
                break;
            case "RSD":
                if (account.getRSD_amount().compareTo(quantityTo) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You broke.");
                account.setRSD_amount(account.getRSD_amount().add(quantityTo));
                break;
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

    @PutMapping("/transfer")
    public ResponseEntity<BankAccount> transferBalance(@RequestBody TransferDto transferDto) throws Exception{

        BankAccount fromAccount = repo.findByEmail(transferDto.getFromAccount());
        BankAccount toAccount = repo.findByEmail(transferDto.getToAccount());

        if(fromAccount == null || toAccount == null) {
            throw new CustomExceptions.AccountNotFoundException("Bank account doesn't exist.");
        }

        switch (transferDto.getCurrency()) {
            case "EUR":
                fromAccount.setEUR_amount(fromAccount.getEUR_amount().subtract(transferDto.getFromQuantity()));
                toAccount.setEUR_amount(toAccount.getEUR_amount().add(transferDto.getToQuantity()));
                break;
            case "USD":
                fromAccount.setUSD_amount(fromAccount.getUSD_amount().subtract(transferDto.getFromQuantity()));
                toAccount.setUSD_amount(toAccount.getUSD_amount().add(transferDto.getToQuantity()));
                break;
            case "GBP":
                fromAccount.setGBP_amount(fromAccount.getGBP_amount().subtract(transferDto.getFromQuantity()));
                toAccount.setGBP_amount(toAccount.getGBP_amount().add(transferDto.getToQuantity()));
                break;
            case "CHF":
                fromAccount.setCHF_amount(fromAccount.getCHF_amount().subtract(transferDto.getFromQuantity()));
                toAccount.setCHF_amount(toAccount.getCHF_amount().add(transferDto.getToQuantity()));
                break;
            case "RSD":
                fromAccount.setRSD_amount(fromAccount.getRSD_amount().subtract(transferDto.getFromQuantity()));
                toAccount.setRSD_amount(toAccount.getRSD_amount().add(transferDto.getToQuantity()));
                break;
            default:
                throw new CustomExceptions.YouCantDoThatException("Currency " + transferDto.getCurrency() + " is not supported");
        }

        repo.save(fromAccount);
        repo.save(toAccount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
