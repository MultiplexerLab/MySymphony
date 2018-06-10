package sym.appstore.ModelClass;

import java.io.Serializable;

public class PacMishali implements Serializable {

    String contentTitle , contentType , contentDescription , imageUrl,contentCat, thumbNail_image;
    int contentId;

    public PacMishali(String contentTitle, String contentType, String contentDescription, String imageUrl, String thumbNail_image, String contentCat, int contentId) {
        this.contentTitle = contentTitle;
        this.contentType = contentType;
        this.contentDescription = contentDescription;
        this.imageUrl = imageUrl;
        this.contentCat=contentCat;
        this.contentId=contentId;
        this.thumbNail_image = thumbNail_image;
    }
    public String getContentCat() {
        return contentCat;
    }
    public int getContentId() {
        return contentId;
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
    public String getThumbNail_image() {
        return thumbNail_image;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
