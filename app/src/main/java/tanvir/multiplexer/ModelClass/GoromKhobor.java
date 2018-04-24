package tanvir.multiplexer.ModelClass;

import java.io.Serializable;

public class GoromKhobor implements Serializable {

    String contentTitle , contentDescription , contentType , imageUrl , publishedAt;

    public GoromKhobor(String contentTitle, String contentDescription, String contentType, String imageUrl, String publishedAt) {
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
