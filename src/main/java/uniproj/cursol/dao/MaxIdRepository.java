package uniproj.cursol.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uniproj.cursol.entity.ExRateMaxId;


public interface MaxIdRepository extends JpaRepository<ExRateMaxId, Long> {

    @Query("SELECT e.maxId FROM ExRateMaxId e WHERE e.id = (SELECT MAX(e2.id) FROM ExRateMaxId e2)")
    Long getExRateMaxId();

}
