package cryptoWallet;

import cryptoWallet.exceptions.CustomExceptions;
import cryptoWallet.proxy.BankAccountDto;
import cryptoWallet.proxy.BankAccountProxy;
import cryptoWallet.proxy.UserDto;
import cryptoWallet.proxy.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/crypto-wallet")
public class CryptoWalletController {

    @Autowired
    private CryptoWalletRepository repo;

    @Autowired
    private Environment environment;

    @Autowired
    private UserProxy userProxy;

    @Autowired
    private BankAccountProxy accountProxy;

    @GetMapping("/{email}")
    public ResponseEntity<CryptoWallet> getCryptoWallet(@PathVariable String email) throws Exception {
        CryptoWallet cryptoWallet = repo.findByEmail(email);

        if (cryptoWallet == null)
            throw new CustomExceptions.NotFoundException("This wallet doesn't exist");

        cryptoWallet.setEnvironment(environment.getProperty("local.server.port"));
        return new ResponseEntity<>(cryptoWallet, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CryptoWallet> createCryptoWallet(@RequestBody CryptoWallet wallet) throws Exception {
        if (repo.findByEmail(wallet.getEmail()) != null) {
            throw new CustomExceptions.OnlyOneWalletPerUserException("There can only be one wallet per user.");
        }

        ResponseEntity<UserDto> userProxyResponse = userProxy.getUserByEmail(wallet.getEmail());
        if (userProxyResponse.getStatusCode() != HttpStatus.OK)
            throw new CustomExceptions.NotFoundException("This user is not found.");

        try {
            accountProxy.getBankAccountByEmail(wallet.getEmail());
        } catch (Exception e) {
            throw new CustomExceptions.NotFoundException("Cannot create a wallet of user who doesn't own a bank account.");
        }
        CryptoWallet createdWallet = repo.save(wallet);
        createdWallet.setEnvironment(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(createdWallet, HttpStatus.OK);
    }

    @PutMapping("/{email}")
    public ResponseEntity<CryptoWallet> updateCryptoWallet(@PathVariable String email, @RequestBody CryptoWallet wallet) throws Exception {
        CryptoWallet checkUser = repo.findByEmail(email);

        if (checkUser == null)
            throw new CustomExceptions.NotFoundException("This USER does not exist");

        wallet.setEmail(email);

        ResponseEntity<UserDto> proxyResponse = userProxy.getUserByEmail(wallet.getEmail());
        if (proxyResponse.getStatusCode() != HttpStatus.OK) {
            throw new CustomExceptions.NotFoundException("Cannot find user.");
        }

        ResponseEntity<BankAccountDto> account = accountProxy.getBankAccountByEmail(wallet.getEmail());
        if (account.getStatusCode() != HttpStatus.OK)
            throw new CustomExceptions.NotFoundException("Cannot update a wallet of user who doesn't own a bank account.");


        CryptoWallet updatedWallet = repo.save(wallet);
        updatedWallet.setEnvironment(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @PutMapping("/{email}/decrease/{quantityFrom}/from/{cryptoFrom}/increase/{quantityTo}/from/{cryptoTo}")
    public ResponseEntity<CryptoWallet> updateCryptoBalance(
            @PathVariable String email,
            @PathVariable BigDecimal quantityFrom, @PathVariable String cryptoFrom,
            @PathVariable BigDecimal quantityTo, @PathVariable String cryptoTo) throws Exception {
        CryptoWallet wallet = repo.findByEmail(email);

        if (wallet == null)
            throw new CustomExceptions.NotFoundException("This wallet does not exist");

        if (Objects.equals(cryptoFrom, cryptoTo))
            throw new CustomExceptions.YouCantDoThatException("Cannot convert from " + cryptoFrom + " to " + cryptoTo);

        switch (cryptoFrom) {
            case "BTC":
                if (wallet.getBTC_amount().compareTo(quantityFrom) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You don't have enough in your wallet.");
                wallet.setBTC_amount(wallet.getBTC_amount().subtract(quantityFrom));
                break;
            case "ETH":
                if (wallet.getETH_amount().compareTo(quantityFrom) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You don't have enough in your wallet.");
                wallet.setETH_amount(wallet.getETH_amount().subtract(quantityFrom));
                break;
            case "LUNA":
                if (wallet.getLUNA_amount().compareTo(quantityFrom) < 0)
                    throw new CustomExceptions.YouCantDoThatException("You don't have enough in your wallet.");
                wallet.setLUNA_amount(wallet.getLUNA_amount().subtract(quantityFrom));
                break;
            default:
                throw new CustomExceptions.YouCantDoThatException(cryptoFrom + " is not supported,");
        }

        switch (cryptoTo) {
            case "BTC":
                wallet.setBTC_amount(wallet.getBTC_amount().add(quantityTo));
                break;
            case "ETH":
                wallet.setETH_amount(wallet.getETH_amount().add(quantityTo));
                break;
            case "LUNA":
                wallet.setLUNA_amount(wallet.getLUNA_amount().add(quantityTo));
                break;
            default:
                throw new CustomExceptions.YouCantDoThatException(cryptoTo + " is not supported,");
        }

        CryptoWallet updatedWallet = repo.save(wallet);
        updatedWallet.setEnvironment(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteCryptoWallet(@PathVariable String email) throws Exception {
        CryptoWallet wallet = repo.findByEmail(email);

        if (wallet == null)
            throw new CustomExceptions.NotFoundException("This crypto wallet doesn't exist");

        repo.deleteCryptoWalletByEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/transfer")
    public ResponseEntity<CryptoWallet> transferCryptoWallet(@RequestBody TransferDto transferDto) throws Exception{

        CryptoWallet fromWallet = repo.findByEmail(transferDto.getFromWallet());
        CryptoWallet toWallet = repo.findByEmail(transferDto.getToWallet());

        if(fromWallet == null || toWallet == null) {
            throw new CustomExceptions.NotFoundException("This wallet doesn't exist");
        }

        switch (transferDto.getFiatValue()){
            case "BTC":
                if(fromWallet.getBTC_amount().compareTo(transferDto.getFromQuantity()) <= 0)
                    throw new CustomExceptions.YouCantDoThatException("TRANSFER DENIED - Insufficient finds.");
                fromWallet.setBTC_amount(fromWallet.getBTC_amount().subtract(transferDto.getFromQuantity()));
                toWallet.setBTC_amount(toWallet.getBTC_amount().add(transferDto.getToQuantity()));
                break;
            case "ETH":
                if(fromWallet.getETH_amount().compareTo(transferDto.getFromQuantity()) <= 0)
                    throw new CustomExceptions.YouCantDoThatException("TRANSFER DENIED - Insufficient finds.");
                fromWallet.setETH_amount(fromWallet.getETH_amount().subtract(transferDto.getFromQuantity()));
                toWallet.setETH_amount(toWallet.getETH_amount().add(transferDto.getToQuantity()));
                break;
            case "LUNA":
                if(fromWallet.getLUNA_amount().compareTo(transferDto.getFromQuantity()) <= 0)
                    throw new CustomExceptions.YouCantDoThatException("TRANSFER DENIED - Insufficient finds.");
                fromWallet.setLUNA_amount(fromWallet.getLUNA_amount().subtract(transferDto.getFromQuantity()));
                toWallet.setLUNA_amount(toWallet.getLUNA_amount().add(transferDto.getToQuantity()));
                break;
            default:
                throw new CustomExceptions.YouCantDoThatException("This fiat value " + transferDto.getFiatValue() + " is not supported.");
        }

        repo.save(fromWallet);
        repo.save(toWallet);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
