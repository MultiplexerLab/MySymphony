package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class ShocolChobi implements Serializable {

    String contentType, contentUrl , contentTile,contentCat;
    int contentId,contentPrice;


    public int getContentPrice() {
        return contentPrice;
    }

    public ShocolChobi(String contentType, String contentUrl, String contentTile, String contentCat , int contentId, int contentPrice) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTile = contentTile;
        this.contentCat=contentCat;
        this.contentId=contentId;
        this.contentPrice=contentPrice;

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

}
