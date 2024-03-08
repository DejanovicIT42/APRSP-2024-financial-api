package utility.microservices.tradeservice;

import java.math.BigDecimal;

public class TradeCryptoDto {

    private long id;

    private String email;

    private BigDecimal BTC_amount;

    private BigDecimal ETH_amount;

    private BigDecimal LUNA_amount;

    private String environment;

    private String report;

    public TradeCryptoDto() {

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

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
