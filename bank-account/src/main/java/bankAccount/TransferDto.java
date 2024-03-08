package bankAccount;

import java.math.BigDecimal;

public class TransferDto {

    private String currency;

    private BigDecimal fromQuantity;

    private BigDecimal toQuantity;

    private String fromAccount;

    private String toAccount;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
