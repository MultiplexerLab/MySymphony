package lct.mysymphony.ModelClass;

import java.io.Serializable;

/**
 * Created by Lenovo on 4/23/2018.
 */

public class SliderImage implements Serializable{
    private String image_url;
    private int contentId;
    private String description;
    private String contentType,contentTitle,contentCat;



    public SliderImage(String image_url, String description, String contentType, String contentTitle, String contentCat,int contentId) {
        this.image_url = image_url;
        this.description = description;
        this.contentType=contentType;
        this.contentTitle=contentTitle;
        this.contentCat=contentCat;
        this.contentId=contentId;

    }

    public int getContentId() {
        return contentId;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
