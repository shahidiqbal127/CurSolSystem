package uniproj.cursol.dto.platformDTOs;

public class PlatformDTO {

    private Integer platformId;

    private String platformName;

    private String platformImg;

    public PlatformDTO() {
    }

    public PlatformDTO(Integer platformId, String platformName, String platformImg) {
        this.platformId = platformId;
        this.platformName = platformName;
        this.platformImg = platformImg;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformImg() {
        return platformImg;
    }

    public void setPlatformImg(String platformImg) {
        this.platformImg = platformImg;
    }

}
