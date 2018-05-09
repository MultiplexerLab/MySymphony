package lct.mysymphony.ModelClass;

import java.io.Serializable;

/**
 * Created by Lenovo on 4/23/2018.
 */

public class SliderImage implements Serializable{
    private String image_url;
    private int contentId;
    private String description;
    private String contentType,contentTitle,contentCat,contentDesc, thumbNail_image;
    int contentPrice;


    public SliderImage(String image_url, String description, String contentType, String contentTitle, String contentCat,int contentId,String contentDesc, String thumbNail_image, int contentPrice) {
        this.image_url = image_url;
        this.description = description;
        this.contentType=contentType;
        this.contentTitle=contentTitle;
        this.contentCat=contentCat;
        this.contentId=contentId;
        this.contentDesc=contentDesc;
        this.contentPrice=contentPrice;
        this.thumbNail_image=thumbNail_image;

    }

    public String getContentDesc() {
        return contentDesc;
    }

    public int getContentPrice() {
        return contentPrice;
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

    public String getThumbNail_image() {
        return thumbNail_image;
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
