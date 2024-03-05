package utility.microservices.cryptoconversion;

import java.math.BigDecimal;

public class CryptoConversion {
    private String from;
    private String to;
    private BigDecimal conversionMultiple;
    private String environment;

    private BigDecimal conversionTotal;
    private BigDecimal quantity;

    public CryptoConversion(){

    }
    public CryptoConversion(String from, String to, BigDecimal conversionMultiple, String environment, BigDecimal conversionTotal, BigDecimal quantity) {
        this.from = from;
        this.to = to;
        this.conversionMultiple = conversionMultiple;
        this.environment = environment;
        this.conversionTotal = conversionTotal;
        this.quantity = quantity;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getConversionMultiple() {
        return conversionMultiple;
    }

    public void setConversionMultiple(BigDecimal conversionMultiple) {
        this.conversionMultiple = conversionMultiple;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public BigDecimal getConversionTotal() {
        return conversionTotal;
    }

    public void setConversionTotal(BigDecimal conversionTotal) {
        this.conversionTotal = conversionTotal;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
