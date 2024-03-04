package usersService.proxy;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public class CryptoWalletDto {

    private long id;

    private String email;

    private BigDecimal BTC_amount;

    private BigDecimal ETH_amount;

    private BigDecimal LUNA_amount;

    private String environment;

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

    public BigDecimal getLUNA_amount() {
        return LUNA_amount;
    }

    public void setLUNA_amount(BigDecimal LUNA_amount) {
        this.LUNA_amount = LUNA_amount;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public CryptoWalletDto(long id, String email, BigDecimal BTC_amount, BigDecimal ETH_amount, BigDecimal LUNA_amount, String environment) {
        this.id = id;
        this.email = email;
        this.BTC_amount = BTC_amount;
        this.ETH_amount = ETH_amount;
        this.LUNA_amount = LUNA_amount;
        this.environment = environment;
    }
}
