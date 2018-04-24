package tanvir.multiplexer.ModelClass;

import java.io.Serializable;

public class Kheladhula_Cricket_sorboshesh_sobgbad implements Serializable {

    String contentTitle , contentDescription , contentType , imageUrl , publishedAt;

    public Kheladhula_Cricket_sorboshesh_sobgbad(String contentTitle, String contentType, String publishedAt, String contentDescription, String imageUrl) {
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentType = contentType;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;
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

    public String getPublishedAt() {
        return publishedAt;
    }
}
