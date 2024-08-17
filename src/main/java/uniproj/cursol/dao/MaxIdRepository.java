package uniproj.cursol.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uniproj.cursol.entity.ExRateMaxId;
import uniproj.cursol.entity.ExchangeRate;

public interface MaxIdRepository extends JpaRepository<ExRateMaxId, Long> {

    @Query("SELECT e.maxId FROM ExRateMaxId e WHERE e.id = (SELECT MAX(e2.id) FROM ExRateMaxId e2)")
    Long getExRateMaxId();

}
