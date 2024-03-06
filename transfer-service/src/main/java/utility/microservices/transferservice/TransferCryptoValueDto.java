package utility.microservices.transferservice;

import java.math.BigDecimal;

public class TransferCryptoValueDto {

    private String cryptoValue;

    private BigDecimal fromQuantity;

    private BigDecimal toQuantity;

    private String fromWallet;

    private String toWallet;

    private TransferCryptoValueDto() {

    }

    public TransferCryptoValueDto(String fiatValue, BigDecimal fromQuantity, BigDecimal toQuantity, String fromWallet, String toWallet) {
        this.cryptoValue = fiatValue;
        this.fromQuantity = fromQuantity;
        this.toQuantity = toQuantity;
        this.fromWallet = fromWallet;
        this.toWallet = toWallet;
    }

    public String getFiatValue() {
        return cryptoValue;
    }

    public void setFiatValue(String fiatValue) {
        this.cryptoValue = fiatValue;
    }

    public BigDecimal getFromQuantity() {
        return fromQuantity;
    }

    public void setFromQuantity(BigDecimal fromQuantity) {
        this.fromQuantity = fromQuantity;
    }

    public BigDecimal getToQuantity() {
        return toQuantity;
    }

    public void setToQuantity(BigDecimal toQuantity) {
        this.toQuantity = toQuantity;
    }

    public String getFromWallet() {
        return fromWallet;
    }

    public void setFromWallet(String fromWallet) {
        this.fromWallet = fromWallet;
    }

    public String getToWallet() {
        return toWallet;
    }

    public void setToWallet(String toWallet) {
        this.toWallet = toWallet;
    }
}
