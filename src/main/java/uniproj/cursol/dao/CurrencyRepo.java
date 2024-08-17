package uniproj.cursol.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import uniproj.cursol.entity.DummyCurrency;

@Repository
public interface CurrencyRepo extends JpaRepository<DummyCurrency, Integer> {

    @Transactional
    @Query("SELECT c.currencyCode FROM DummyCurrency c") // entites
    List<String> findAllCurrencyCodes();

}
