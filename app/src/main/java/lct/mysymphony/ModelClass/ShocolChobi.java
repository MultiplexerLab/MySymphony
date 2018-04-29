package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class ShocolChobi implements Serializable {

    String contentType, contentUrl , contentTile,contentCat;
    int contentId;


    public ShocolChobi(String contentType, String contentUrl, String contentTile,String contentCat , int contentId) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTile = contentTile;
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

}
