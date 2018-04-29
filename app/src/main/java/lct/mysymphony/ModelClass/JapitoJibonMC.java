package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class JapitoJibonMC implements Serializable {

    private String contentTitle;
    private String imageUrl;
    private String contentType;
    private String contentDescription,contentUrl,conntentCat;
    int contentId;



    public String getContentDescription() {
        return contentDescription;
    }

    public JapitoJibonMC(String contentTitle, String contentDescription, String imageUrl, String contentType,String contentUrl,String conntentCat,int contentId) {
        this.contentTitle = contentTitle;
        this.imageUrl = imageUrl;
        this.contentType = contentType;

        this.contentDescription=contentDescription;
        this.contentUrl=contentUrl;
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

    public String getConntentCat() {
        return conntentCat;
    }

    public int getContentId() {
        return contentId;
    }
}
