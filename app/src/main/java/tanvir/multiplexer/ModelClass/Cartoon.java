package tanvir.multiplexer.ModelClass;

import java.io.Serializable;

public class Cartoon implements Serializable {

    String contentTitle , contentDescription , contentType , imageUrl ;

    public Cartoon(String contentTitle, String contentType, String contentDescription, String imageUrl) {
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
