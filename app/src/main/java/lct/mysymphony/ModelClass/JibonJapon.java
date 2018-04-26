package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class JibonJapon implements Serializable {

    String contentTitle , contentType , contentDescription , imageUrl;

    public JibonJapon(String contentTitle, String contentType, String contentDescription, String imageUrl) {
        this.contentTitle = contentTitle;
        this.contentType = contentType;
        this.contentDescription = contentDescription;
        this.imageUrl = imageUrl;
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