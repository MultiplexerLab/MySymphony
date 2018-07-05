package harmony.app.ModelClass;

import java.io.Serializable;

public class Porashuna implements Serializable {

    String contentTitle, contentType, contentDescription, contentUrl, contentCat, thumbnailImgUrl;
    int contentId;

    public Porashuna(String contentTitle, String contentType, String contentDescription, String contentUrl, String thumbnailImgUrl, String contentCat, int contentId) {
        this.contentTitle = contentTitle;
        this.contentType = contentType;
        this.contentDescription = contentDescription;
        this.contentUrl = contentUrl;
        this.contentCat = contentCat;
        this.contentId = contentId;
        this.thumbnailImgUrl = thumbnailImgUrl;

    }

    public String getContentCat() {
        return contentCat;
    }

    public int getContentId() {
        return contentId;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }
}
