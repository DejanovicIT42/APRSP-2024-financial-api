package bankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM BANK_ACCOUNT b WHERE b.email = :email")
    void deleteBankAccountByEmail(String email);
}
