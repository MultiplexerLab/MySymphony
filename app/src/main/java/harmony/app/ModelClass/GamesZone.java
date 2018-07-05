package harmony.app.ModelClass;

import java.io.Serializable;

public class GamesZone implements Serializable {

    String contentType, contentUrl , contentTile,contentCat, thumbNailImgUrl;
    int previousPrice , newPrice,contentId;

    public GamesZone(String contentType, String contentUrl, String contentTile, String thumbNailImgUrl, int previousPrice, int newPrice,String contentCat,int contentId) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTile = contentTile;
        this.thumbNailImgUrl = thumbNailImgUrl;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
        this.contentCat=contentCat;
        this.contentId=contentId;
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
}
