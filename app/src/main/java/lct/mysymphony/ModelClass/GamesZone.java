package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class GamesZone implements Serializable {

    String contentType, contentUrl , contentTile;
    int previousPrice , newPrice;

    public GamesZone(String contentType, String contentUrl, String contentTile, int previousPrice, int newPrice) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTile = contentTile;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
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
}
