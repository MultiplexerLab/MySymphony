package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class Cartoon implements Serializable {

    String contentTitle , contentDescription , contentType , imageUrl,contentCat ;
    int contentId;

    public String getContentCat() {
        return contentCat;
    }

    public int getContentId() {
        return contentId;
    }

    public Cartoon(String contentTitle, String contentType, String contentDescription, String imageUrl, String contentCat, int contentId) {
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentType = contentType;
        this.imageUrl = imageUrl;
        this.contentCat=contentCat;
        this.contentId=contentId;


    }

    public String getContentTitle() {
        return contentTitle;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public String getContentType() {
        return contentType;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}
