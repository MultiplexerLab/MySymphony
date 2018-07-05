package harmony.app.ModelClass;

import java.io.Serializable;

public class JibonJapon implements Serializable {

    String contentTitle , contentType , contentDescription , imageUrl,contentCat, thumbnailImgUrl;
    int contentId;
    public JibonJapon(String contentTitle, String contentType, String contentDescription, String imageUrl, String contentCat, String thumbnailImgUrl, int contentId) {
        this.contentTitle = contentTitle;
        this.contentType = contentType;
        this.contentDescription = contentDescription;
        this.imageUrl = imageUrl;
        this.contentCat=contentCat;
        this.contentId=contentId;
        this.thumbnailImgUrl = thumbnailImgUrl;
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
    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getContentCat() {
        return contentCat;
    }
    public int getContentId() {
        return contentId;
    }
}
