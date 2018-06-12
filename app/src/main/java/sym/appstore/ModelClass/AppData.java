package sym.appstore.ModelClass;

public class AppData {

    String title;
    String description;
    String thumbNailImage;
    String contentUrl;
    String packagName;
    String versionCode;

    public AppData(String title, String description, String thumbNailImage, String contentUrl, String packagName, String versionCode) {
        this.title = title;
        this.description = description;
        this.thumbNailImage = thumbNailImage;
        this.contentUrl = contentUrl;
        this.packagName = packagName;
        this.versionCode = versionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbNailImage() {
        return thumbNailImage;
    }

    public void setThumbNailImage(String thumbNailImage) {
        this.thumbNailImage = thumbNailImage;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getPackagName() {
        return packagName;
    }

    public void setPackagName(String packagName) {
        this.packagName = packagName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
