package sym.appstore.ModelClass;

import java.io.Serializable;

public class Auttohashi implements Serializable {

    String contentTitle , contentDescription , contentType , imageUrl ,contentCat, thumbnailImgUrl ;
    int contentId;

    public Auttohashi(String contentTitle, String contentType, String contentDescription, String imageUrl, String thumbnailImgUrl, String contentCat , int contentId) {
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentType = contentType;
        this.imageUrl = imageUrl;
        this.contentCat=contentCat;
        this.thumbnailImgUrl = thumbnailImgUrl;
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
