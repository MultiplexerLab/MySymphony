package harmony.app.ModelClass;

public class AppData {

    String id;
    String contentCat;
    String contentType;
    String contentTitle;
    String contentDescription;
    String thumbNail_image;
    int contentPrice;
    String contentUrl;
    String reference1;
    String reference2;
    String reference3;

    public AppData(String contentId, String title, String description, String thumbNailImage, String contentUrl, int contentPrice, String packagName, String versionCode) {
        this.id = contentId;
        this.contentTitle = title;
        this.contentDescription = description;
        this.thumbNail_image = thumbNailImage;
        this.contentUrl = contentUrl;
        this.contentPrice = contentPrice;
        this.reference1 = packagName;
        this.reference3 = versionCode;
    }

    public String getId() {
        return id;
    }

    public int getContentPrice() {
        return contentPrice;
    }

    public void setContentPrice(int contentPrice) {
        this.contentPrice = contentPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentCat() {
        return contentCat;
    }

    public void setContentCat(String contentCat) {
        this.contentCat = contentCat;
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

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public String getThumbNail_image() {
        return thumbNail_image;
    }

    public void setThumbNail_image(String thumbNail_image) {
        this.thumbNail_image = thumbNail_image;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getReference1() {
        return reference1;
    }

    public void setReference1(String reference1) {
        this.reference1 = reference1;
    }

    public String getReference2() {
        return reference2;
    }

    public void setReference2(String reference2) {
        this.reference2 = reference2;
    }

    public String getReference3() {
        return reference3;
    }

    public void setReference3(String reference3) {
        this.reference3 = reference3;
    }
}
