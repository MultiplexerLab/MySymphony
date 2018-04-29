package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class MulloChar implements Serializable {

    String contentType, contentUrl, contentTile, imageUrl,contentCat;
    int previousPrice, newPrice,contentId;


    public String getContentCat() {
        return contentCat;
    }

    public int getContentId() {
        return contentId;
    }

    public MulloChar(String contentType, String contentUrl, String contentTile, int previousPrice, int newPrice, String imageUrl, String contentCat, int contentId) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTile = contentTile;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
        this.imageUrl = imageUrl;
        this.contentCat=contentCat;
        this.contentId=contentId;



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
