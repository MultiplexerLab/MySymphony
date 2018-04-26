package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class Auttohashi implements Serializable {

    String contentTitle , contentDescription , contentType , imageUrl ;

    public Auttohashi(String contentTitle, String contentType, String contentDescription, String imageUrl) {
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentType = contentType;
        this.imageUrl = imageUrl;

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
