package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class JapitoJibonMC implements Serializable {

    private String contentTitle;
    private String imageUrl;
    private String contentType;
    private String contentDescription,contentUrl,contentCat;
    int contentId,contentPrice;



    public String getContentDescription() {
        return contentDescription;
    }



    public JapitoJibonMC(String contentTitle, String contentDescription, String imageUrl, String contentType, String contentUrl, String contentCat, int contentId, int contentPrice) {
        this.contentTitle = contentTitle;
        this.imageUrl = imageUrl;
        this.contentType = contentType;

        this.contentDescription=contentDescription;
        this.contentUrl=contentUrl;
        this.contentPrice=contentPrice;
        this.contentId=contentId;
        this.contentCat=contentCat;

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

    public String getContentUrl() {
        return contentUrl;
    }



    public int getContentId() {
        return contentId;
    }
}
