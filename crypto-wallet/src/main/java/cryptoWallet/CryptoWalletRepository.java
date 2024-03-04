package cryptoWallet;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long> {
    CryptoWallet findByEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM CRYPTO_WALLET cw WHERE cw.email = :email")
    void deleteCryptoWalletByEmail(String email);
}
