package harmony.app.ModelClass;

import java.io.Serializable;

public class ShikkhaSohaYika implements Serializable {

    String contentType,contentDescription, contentUrl, contentTitle, imageURL,contentCat;
    int contentId,contentPrice;

    public ShikkhaSohaYika(String contentType, String contentDescription, String contentUrl, String contentTitle, String imageURL, String contentCat, int contentId,int contentPrice) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTitle=contentTitle;
        this.imageURL = imageURL;
        this.contentDescription=contentDescription;
        this.contentCat=contentCat;
        this.contentId=contentId;
        this.contentPrice=contentPrice;

    }
    public String getContentDescription() {
        return contentDescription;
    }
    public String getContentCat() {
        return contentCat;
    }
    public int getContentId() {
        return contentId;
    }
    public int getContentPrice() {
        return contentPrice;
    }
    public String getContentType() {
        return contentType;
    }
    public String getContentUrl() {
        return contentUrl;
    }
    public String getContentTitle() {
        return contentTitle;
    }
    public String getImageURL() {
        return imageURL;
    }
}
