package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class BigganOProjukti implements Serializable {

    String contentTitle, contentType, contentDescription, imageUrl, contentCat, thumbnailImgUrl;
    int contentId;

    public BigganOProjukti(String contentTitle, String contentType, String contentDescription, String imageUrl, String thumbnailImgUrl, String contentCat, int contentId) {
        this.contentTitle = contentTitle;
        this.contentType = contentType;
        this.contentDescription = contentDescription;
        this.imageUrl = imageUrl;
        this.contentCat = contentCat;
        this.contentId = contentId;
        this.thumbnailImgUrl = thumbnailImgUrl;
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
    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
