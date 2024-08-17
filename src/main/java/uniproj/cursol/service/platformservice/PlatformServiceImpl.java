package uniproj.cursol.service.platformservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uniproj.cursol.dao.PlatformRepo;
import uniproj.cursol.dto.exchangerateDTOs.ProviderDTO;
import uniproj.cursol.dto.exchangerateDTOs.RootDTO;
import uniproj.cursol.entity.Platform;

@Service
public class PlatformServiceImpl implements PlatformService {

     @Autowired
    private PlatformRepo platformRepo;


    @Override
    public void storingPlatformData(RootDTO rootDTO) {
         for (ProviderDTO providerDTO : rootDTO.providers) {
            // Save platform data if not already saved
            Platform platform = platformRepo.findByPlatformName(providerDTO.name);
            if (platform == null) {
                platform = new Platform();
                platform.setPlatformName(providerDTO.name);
                platform.setPlatformImg(providerDTO.logos != null ? providerDTO.logos.svgUrl : null);
                platformRepo.save(platform);
            }
    }
    }

}
