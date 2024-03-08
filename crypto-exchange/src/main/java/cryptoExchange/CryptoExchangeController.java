package cryptoExchange;

import cryptoExchange.exceptions.CustomExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoExchangeController {

    @Autowired
    private CryptoExchangeRepository repo;

    @Autowired
    private Environment environment;

    @GetMapping("/crypto-exchange/from/{from}/to/{to}")
    public ResponseEntity<CryptoExchange> getExchange(@PathVariable String from, @PathVariable String to) throws Exception {
        String portic = environment.getProperty("local.server.port");
        CryptoExchange exchange = repo.findByFromAndToIgnoreCase(from, to);

        if (exchange == null)
            throw new CustomExceptions.NoContentFoundException("No exchange data found.");

        exchange.setEnvironment(portic);

        return ResponseEntity.status(HttpStatus.OK).body(exchange);
    }
}
