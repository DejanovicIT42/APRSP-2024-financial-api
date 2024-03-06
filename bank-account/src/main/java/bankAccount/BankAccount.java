package bankAccount;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "BANK_ACCOUNT")
public class BankAccount {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String email;


    private BigDecimal EUR_amount;


    private BigDecimal USD_amount;


    private BigDecimal GBP_amount;


    private BigDecimal CHF_amount;


    private BigDecimal RSD_amount;

    @Transient
    private String environment;

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public BankAccount() {

    }

    public BankAccount(String email, String environment) {
        super();
        this.email = email;
        RSD_amount = BigDecimal.valueOf(0);
        EUR_amount = BigDecimal.valueOf(0);
        CHF_amount = BigDecimal.valueOf(0);
        GBP_amount = BigDecimal.valueOf(0);
        USD_amount = BigDecimal.valueOf(0);
    }

    public BankAccount(BigDecimal rsd_amount, BigDecimal eur_amount, BigDecimal usd_amount, BigDecimal gbp_amount,
                       BigDecimal chf_amount) {
        super();
        RSD_amount = rsd_amount;
        EUR_amount = eur_amount;
        USD_amount = usd_amount;
        GBP_amount = gbp_amount;
        CHF_amount = chf_amount;
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

    public BigDecimal getEUR_amount() {
        return EUR_amount;
    }

    public void setEUR_amount(BigDecimal EUR_amount) {
        this.EUR_amount = EUR_amount;
    }

    public BigDecimal getUSD_amount() {
        return USD_amount;
    }

    public void setUSD_amount(BigDecimal USD_amount) {
        this.USD_amount = USD_amount;
    }

    public BigDecimal getGBP_amount() {
        return GBP_amount;
    }

    public void setGBP_amount(BigDecimal GBP_amount) {
        this.GBP_amount = GBP_amount;
    }

    public BigDecimal getCHF_amount() {
        return CHF_amount;
    }

    public void setCHF_amount(BigDecimal CHF_amount) {
        this.CHF_amount = CHF_amount;
    }

    public BigDecimal getRSD_amount() {
        return RSD_amount;
    }

    public void setRSD_amount(BigDecimal RSD_amount) {
        this.RSD_amount = RSD_amount;
    }

    public String getEnvironment() {
        return environment;
    }

}
