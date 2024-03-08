package utility.microservices.transferservice;

import java.math.BigDecimal;

public class TransferFiatValueDto {

    private String currency;

    private BigDecimal fromQuantity;

    private BigDecimal toQuantity;

    private String fromAccount;

    private String toAccount;

    private TransferFiatValueDto(){

    }

    public TransferFiatValueDto(String currency, BigDecimal fromQuantity, BigDecimal toQuantity, String fromAccount, String toAccount) {
        this.currency = currency;
        this.fromQuantity = fromQuantity;
        this.toQuantity = toQuantity;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
}
