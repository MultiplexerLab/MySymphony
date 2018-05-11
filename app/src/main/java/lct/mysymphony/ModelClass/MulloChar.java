package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class MulloChar implements Serializable {

    String contentType, contentUrl, contentTile, imageUrl,contentCat, thumbNailImgUrl;
    int previousPrice, newPrice,contentId;

    public MulloChar(String contentType, String contentUrl, String contentTile, String thumbNailImgUrl, int previousPrice, int newPrice, String imageUrl, String contentCat, int contentId) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTile = contentTile;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
        this.imageUrl = imageUrl;
        this.contentCat=contentCat;
        this.contentId=contentId;
        this.thumbNailImgUrl = thumbNailImgUrl;
    }
    public String getContentCat() {
        return contentCat;
    }
    public int getContentId() {
        return contentId;
    }
    public String getContentType() {
        return contentType;
    }
    public String getContentUrl() {
        return contentUrl;
    }
    public String getContentTile() {
        return contentTile;
    }
    public String getThumbNailImgUrl() {
        return thumbNailImgUrl;
    }
    public int getPreviousPrice() {
        return previousPrice;
    }
    public int getNewPrice() {
        return newPrice;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
