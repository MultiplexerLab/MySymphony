package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class ShocolChobi implements Serializable {

    String contentType, contentUrl , contentTile;


    public ShocolChobi(String contentType, String contentUrl, String contentTile) {
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentTile = contentTile;

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
