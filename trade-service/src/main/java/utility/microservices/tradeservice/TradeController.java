package utility.microservices.tradeservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utility.microservices.tradeservice.exceptions.CustomExceptions;
import utility.microservices.tradeservice.proxy.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/trade-service")
public class TradeController {
    @Autowired
    private TradeRepository repo;

    @Autowired
    private BankAccountProxy accountProxy;

    @Autowired
    private CryptoWalletProxy walletProxy;

    @Autowired
    private CurrencyConversionProxy conversionProxy;

    @Autowired
    private CurrencyExchangeProxy exchangeProxy;

    private List<String> fiatCurrency = Arrays.asList("EUR", "USD", "CHF", "GBP", "RSD");

    private List<String> cryptoCurrency = Arrays.asList("BTC", "ETH", "LUNA");

    @PostMapping()
    public ResponseEntity<?> tradingValues(@RequestParam String from,
                                           @RequestParam String to,
                                           @RequestParam BigDecimal quantity,
                                           HttpServletRequest request) throws Exception{
        String email = request.getHeader("X-User-Email");
        if(email == null){
            throw new CustomExceptions.NoContentFoundException("Requested email doesn't exist");
        }

        BankAccountDto userBankAccount = accountProxy.getBankAccount(email).getBody();
        CryptoWalletDto userCryptoWallet = walletProxy.getCryptoWallet(email).getBody();


        Trade trade = repo.findByFromAndTo(from, to);
        BigDecimal conversionMultiple = trade.getConversionMultiple();


        if(fiatCurrency.contains(from) && cryptoCurrency.contains(to)) {
            switch (from){
                case "EUR":
                    userBankAccount.setEUR_amount(userBankAccount.getEUR_amount().subtract(quantity));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    break;
                case "USD":
                    userBankAccount.setUSD_amount(userBankAccount.getUSD_amount().subtract(quantity));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    break;

                case "CHF":
                    conversionProxy.getConversion("CHF", "EUR", quantity);
                    userBankAccount.setEUR_amount(userBankAccount.getEUR_amount().subtract(quantity));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    break;
                //needs changing
                case "GBP":
                    ResponseEntity<CurrencyConversionDto> GBPtoEUR = conversionProxy.getConversion(from, to, quantity);
                    userBankAccount.setGBP_amount(userBankAccount.getGBP_amount().subtract(GBPtoEUR.getBody().getConversionMultiple()));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    break;
                case "RSD":
                    ResponseEntity<CurrencyConversionDto> RSDtoEUR = conversionProxy.getConversion(from, to, quantity);
                    userBankAccount.setRSD_amount(userBankAccount.getRSD_amount().subtract(RSDtoEUR.getBody().getConversionMultiple()));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    break;
                default:
                    throw new CustomExceptions.YouCantDoThatException(from + " is not supported fiat value.");
            }

            switch (to){
                case "BTC":
                    userCryptoWallet.setBTC_amount(userCryptoWallet.getBTC_amount().add(quantity.multiply(conversionMultiple)));
                    walletProxy.updateCryptoWallet(email, userCryptoWallet);
                    break;
                case "ETH":
                    userCryptoWallet.setETH_amount(userCryptoWallet.getETH_amount().add(quantity.multiply(conversionMultiple)));
                    walletProxy.updateCryptoWallet(email, userCryptoWallet);
                    break;
                case "LUNA":
                    userCryptoWallet.setLUNA_amount(userCryptoWallet.getLUNA_amount().add(quantity.multiply(conversionMultiple)));
                    walletProxy.updateCryptoWallet(email, userCryptoWallet);
                    break;
                default:
                    throw new CustomExceptions.YouCantDoThatException(to + " is not supported crypto value.");
            }

            CryptoWalletDto cryptoWalletTraded = walletProxy.getCryptoWallet(email).getBody();
            TradeCryptoDto cryptoResponse = new TradeCryptoDto();
            cryptoResponse.setId(cryptoWalletTraded.getId());
            cryptoResponse.setEmail(cryptoWalletTraded.getEmail());
            cryptoResponse.setBTC_amount(cryptoWalletTraded.getBTC_amount());
            cryptoResponse.setETH_amount(cryptoWalletTraded.getETH_amount());
            cryptoResponse.setLUNA_amount(cryptoWalletTraded.getLUNA_amount());
            cryptoResponse.setReport("TRANSACTION SUCCESSFUL");
            return new ResponseEntity<>(cryptoResponse, HttpStatus.OK);

        } else if (cryptoCurrency.contains(from) && fiatCurrency.contains(to)) {
            switch (from) {
                case "BTC":
                    userCryptoWallet.setBTC_amount(userCryptoWallet.getBTC_amount().subtract(quantity));
                    walletProxy.updateCryptoWallet(email, userCryptoWallet);
                    break;
                case "ETH":
                    userCryptoWallet.setETH_amount(userCryptoWallet.getETH_amount().subtract(quantity));
                    walletProxy.updateCryptoWallet(email, userCryptoWallet);
                    break;
                case "LUNA":
                    userCryptoWallet.setLUNA_amount(userCryptoWallet.getLUNA_amount().subtract(quantity));
                    walletProxy.updateCryptoWallet(email, userCryptoWallet);
                    break;
                default:
                    throw new CustomExceptions.YouCantDoThatException(from + " is not supported crypto value.");
            }

            switch (to) {
                case "EUR":
                    userBankAccount.setEUR_amount(userBankAccount.getEUR_amount().add(quantity.multiply(conversionMultiple)));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    break;
                case "USD":
                    userBankAccount.setEUR_amount(userBankAccount.getUSD_amount().add(quantity.multiply(conversionMultiple)));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    break;

                case "CHF":
                    userBankAccount.setEUR_amount(userBankAccount.getEUR_amount().add(quantity.multiply(conversionMultiple)));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    conversionProxy.getConversion("EUR", "CHF", quantity.multiply(conversionMultiple));
                    break;
                case "GBP":
                    userBankAccount.setEUR_amount(userBankAccount.getEUR_amount().add(quantity.multiply(conversionMultiple)));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    conversionProxy.getConversion("EUR", "GBP", quantity.multiply(conversionMultiple));
                    break;
                case "RSD":
                    userBankAccount.setEUR_amount(userBankAccount.getEUR_amount().add(quantity.multiply(conversionMultiple)));
                    accountProxy.updateBankAccount(email, userBankAccount);
                    conversionProxy.getConversion("EUR", "RSD", quantity.multiply(conversionMultiple));
                    break;
                default:
                    throw new CustomExceptions.YouCantDoThatException(to + " is not supported fiat value.");
            }

            BankAccountDto bankAccountTraded = accountProxy.getBankAccount(email).getBody();
            TradeFiatDto fiatResponse = new TradeFiatDto();
            fiatResponse.setId(bankAccountTraded.getId());
            fiatResponse.setEmail(bankAccountTraded.getEmail());
            fiatResponse.setEUR_amount(bankAccountTraded.getEUR_amount());
            fiatResponse.setUSD_amount(bankAccountTraded.getUSD_amount());
            fiatResponse.setCHF_amount(bankAccountTraded.getCHF_amount());
            fiatResponse.setGBP_amount(bankAccountTraded.getGBP_amount());
            fiatResponse.setRSD_amount(bankAccountTraded.getRSD_amount());
            fiatResponse.setReport("TRANSACTION SUCCESSFUL");
            return new ResponseEntity<>(fiatResponse, HttpStatus.OK);
        } else {
            throw new CustomExceptions.YouCantDoThatException("You can only convert from crypto to fiat values or vice versa.");
        }
    }
}
