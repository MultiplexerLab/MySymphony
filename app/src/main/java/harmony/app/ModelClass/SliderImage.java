package harmony.app.ModelClass;

import java.io.Serializable;

public class SliderImage implements Serializable {
    private String contentUrl;
    private int id;
    private String contentDescription;
    private String contentType, contentTitle, contentCat, thumbNail_image;
    int contentPrice;

    public SliderImage(String contentUrl, String description, String contentType, String contentTitle, String contentCat, int contentId, String contentDesc, String thumbNail_image, int contentPrice) {
        this.contentUrl = contentUrl;
        this.contentDescription = description;
        this.contentType = contentType;
        this.contentTitle = contentTitle;
        this.contentCat = contentCat;
        this.id = contentId;
        this.contentPrice = contentPrice;
        this.thumbNail_image = thumbNail_image;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentCat() {
        return contentCat;
    }

    public void setContentCat(String contentCat) {
        this.contentCat = contentCat;
    }

    public String getThumbNail_image() {
        return thumbNail_image;
    }

    public void setThumbNail_image(String thumbNail_image) {
        this.thumbNail_image = thumbNail_image;
    }

    public int getContentPrice() {
        return contentPrice;
    }

    public void setContentPrice(int contentPrice) {
        this.contentPrice = contentPrice;
    }
}
