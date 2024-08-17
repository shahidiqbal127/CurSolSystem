package uniproj.cursol.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uniproj.cursol.entity.Platform;

@Repository
public interface PlatformRepo extends JpaRepository<Platform, Integer> {

    Platform findByPlatformName(String platformName);

}
