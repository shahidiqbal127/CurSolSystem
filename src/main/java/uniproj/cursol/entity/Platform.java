package uniproj.cursol.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Platform")
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    private Integer platformId;

    @Column(name = "platform_name")
    private String platformName;

    @Column(name = "platform_img_url")
    private String platformImg;

    @Column(name = "platform_site_url")
    private String platformSiteUrl;

    public String getPlatformSiteUrl() {
        return platformSiteUrl;
    }

    public void setPlatformSiteUrl(String platformSiteUrl) {
        this.platformSiteUrl = platformSiteUrl;
    }

    public Platform() {
    }

    public Platform(Integer platformId, String platformName, String platformImg) {
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
