package tanvir.multiplexer.ModelClass;

import java.io.Serializable;

public class MulloChar implements Serializable {

    String contentType, contentUrl, contentTile, imageUrl;
    int previousPrice, newPrice;


    public MulloChar(String contentType, String contentUrl, String contentTile, int previousPrice, int newPrice, String imageUrl) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTile = contentTile;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
        this.imageUrl = imageUrl;


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
