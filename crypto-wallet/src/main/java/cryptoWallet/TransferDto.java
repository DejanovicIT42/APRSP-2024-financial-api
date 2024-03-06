package cryptoWallet;

import java.math.BigDecimal;

public class TransferDto {

    private String fiatValue;

    private BigDecimal fromQuantity;

    private BigDecimal toQuantity;

    private String fromWallet;

    private String toWallet;

    public TransferDto(){

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

    public String getFiatValue() {
        return fiatValue;
    }

    public void setFiatValue(String fiatValue) {
        this.fiatValue = fiatValue;
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
