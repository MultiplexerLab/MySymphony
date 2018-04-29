package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class Kheladhula_Cricket_sorboshesh_sobgbad implements Serializable {

    String contentTitle , contentDescription , contentType , imageUrl , publishedAt,contentcat;
    int contentId;

    public Kheladhula_Cricket_sorboshesh_sobgbad(String contentTitle, String contentType, String publishedAt, String contentDescription, String imageUrl,String contentcat,int id) {
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentType = contentType;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;
        this.contentcat=contentcat;
        this.contentId=id;
    }

    public String getContentcat() {
        return contentcat;
    }

    public int getContentId() {
        return contentId;
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
