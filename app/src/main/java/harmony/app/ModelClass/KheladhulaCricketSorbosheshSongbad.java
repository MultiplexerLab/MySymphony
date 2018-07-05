package harmony.app.ModelClass;

import java.io.Serializable;

public class KheladhulaCricketSorbosheshSongbad implements Serializable {

    String contentTitle , contentDescription , contentType , imageUrl , publishedAt,contentcat, thumbnailImgUrl;
    int contentId;
    public KheladhulaCricketSorbosheshSongbad(String contentTitle, String contentType, String publishedAt, String contentDescription, String thumbnailImgUrl, String imageUrl, String contentcat, int id) {
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentType = contentType;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;
        this.contentcat=contentcat;
        this.contentId=id;
        this.thumbnailImgUrl = thumbnailImgUrl;
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
    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getPublishedAt() {
        return publishedAt;
    }
}
