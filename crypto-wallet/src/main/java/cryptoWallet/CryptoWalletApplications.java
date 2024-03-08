package cryptoWallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CryptoWalletApplications {
    public static void main(String[] args) {
        SpringApplication.run(CryptoWalletApplications.class, args);
    }
}
