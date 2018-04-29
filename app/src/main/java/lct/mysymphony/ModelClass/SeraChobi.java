package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class SeraChobi implements Serializable {

    private String image_url;
    private int contentId;
    private String description;
    private String contentType,contentTitle,contentCat;

    public String getImage_url() {
        return image_url;
    }

    public int getContentId() {
        return contentId;
    }

    public String getDescription() {
        return description;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public String getContentCat() {
        return contentCat;
    }

    public SeraChobi(String image_url, String description, String contentType, String contentTitle, String contentCat,int contentId) {
        this.image_url = image_url;
        this.contentId = contentId;
        this.description = description;
        this.contentType = contentType;
        this.contentTitle = contentTitle;
        this.contentCat = contentCat;
    }
}