package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class ShikkhaSohaYika implements Serializable {

    String contentType,contentDescription, contentUrl, contentTitle, imageURL;

    public String getContentDescription() {
        return contentDescription;
    }

    public ShikkhaSohaYika(String contentType, String contentDescription, String contentUrl, String contentTitle, String imageURL) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTitle=contentTitle;
        this.imageURL = imageURL;
        this.contentDescription=contentDescription;
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
