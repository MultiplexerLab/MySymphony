package harmony.app.ModelClass;

import java.io.Serializable;

public class MusicVideo implements Serializable {

    String contentTitle, contentType, contentDescription, contentUrl, contentCat, contentSubCat, thumbnailImgUrl;
    int contentId, contentPrice;

    boolean isSubscribed;

    public MusicVideo(String contentTitle, String contentType, String contentDescription, String contentUrl, String contentCat, String contentSubCat, String thumbnailImgUrl, int contentId, int contentPrice) {
        this.contentTitle = contentTitle;
        this.contentType = contentType;
        this.contentDescription = contentDescription;
        this.contentUrl = contentUrl;
        this.contentCat = contentCat;
        this.contentSubCat = contentSubCat;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentId = contentId;
        this.contentPrice = contentPrice;
    }

    public int getContentPrice() {
        return contentPrice;
    }

    public void setContentPrice(int contentPrice) {
        this.contentPrice = contentPrice;
    }

    public String getContentCat() {
        return contentCat;
    }

    public String getContentSubCat() {
        return contentSubCat;
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

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}

