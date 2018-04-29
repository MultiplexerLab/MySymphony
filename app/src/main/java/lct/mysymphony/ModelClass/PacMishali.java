package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class PacMishali implements Serializable {

    String contentTitle , contentType , contentDescription , imageUrl,contentCat;
    int contentId;

    public String getContentCat() {
        return contentCat;
    }

    public int getContentId() {
        return contentId;
    }

    public PacMishali(String contentTitle, String contentType, String contentDescription, String imageUrl, String contentCat, int contentId) {
        this.contentTitle = contentTitle;
        this.contentType = contentType;
        this.contentDescription = contentDescription;
        this.imageUrl = imageUrl;
        this.contentCat=contentCat;
        this.contentId=contentId;

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

    public String getImageUrl() {
        return imageUrl;
    }
}
