package utility.microservices.cryptoconversion.proxy;

import java.math.BigDecimal;

public class CryptoWalletDto {

    private long id;

    private String email;

    private BigDecimal BTC_amount;

    private BigDecimal ETH_amount;

    private BigDecimal LUNA_amount;

    private String environment;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getBTC_amount() {
        return BTC_amount;
    }

    public void setBTC_amount(BigDecimal BTC_amount) {
        this.BTC_amount = BTC_amount;
    }

    public BigDecimal getETH_amount() {
        return ETH_amount;
    }

    public void setETH_amount(BigDecimal ETH_amount) {
        this.ETH_amount = ETH_amount;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public BigDecimal getLUNA_amount() {
        return LUNA_amount;
    }

    public void setLUNA_amount(BigDecimal LUNA_amount) {
        this.LUNA_amount = LUNA_amount;
    }
}
