package sym.appstore.ModelClass;

public class AppData {

    String title;
    String description;
    String thumbNailImage;
    String contentUrl;

    public AppData(String title, String description, String thumbNailImage, String contentUrl) {
        this.title = title;
        this.description = description;
        this.thumbNailImage = thumbNailImage;
        this.contentUrl = contentUrl;
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
}
