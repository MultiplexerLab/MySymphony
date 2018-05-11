package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class JapitoJibon implements Serializable {

    private String contentTitle;
    private String imageUrl;
    private String contentType;
    private String contentDescription,contentUrl,contentCat, thumbNail_image;
    int contentId,contentPrice;

    public JapitoJibon(String contentTitle, String contentDescription, String imageUrl, String contentType, String contentUrl, String thumbNail_image, String contentCat, int contentId, int contentPrice) {
        this.contentTitle = contentTitle;
        this.imageUrl = imageUrl;
        this.contentType = contentType;
        this.contentDescription=contentDescription;
        this.contentUrl=contentUrl;
        this.contentPrice=contentPrice;
        this.contentId=contentId;
        this.contentCat=contentCat;
        this.thumbNail_image = thumbNail_image;

    }
    public String getContentDescription() {
        return contentDescription;
    }
    public String getContentCat() {
        return contentCat;
    }
    public int getContentPrice() {
        return contentPrice;
    }
    public String getContentTitle() {
        return contentTitle;
    }
    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getThumbNail_image() {
        return thumbNail_image;
    }
    public String getContentUrl() {
        return contentUrl;
    }
    public int getContentId() {
        return contentId;
    }
}
