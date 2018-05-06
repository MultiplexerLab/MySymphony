package lct.mysymphony.ModelClass;

import java.io.Serializable;

public class DataBaseData implements Serializable {
    String contentTitle;
    String contentCat;
    String contentType;
    String contentDesc;
    String contentStatus;
    int contentId;

    public String getContentStatus() {
        return contentStatus;
    }

    public DataBaseData(String contentTitle, String contentCat, String contentType, String contentDesc, String contentStatus,int contentId) {
        this.contentTitle = contentTitle;
        this.contentCat = contentCat;
        this.contentType = contentType;
        this.contentDesc = contentDesc;
        this.contentStatus=contentStatus;
        this.contentId=contentId;

    }

    public String getContentTitle() {
        return contentTitle;
    }

    public String getContentCat() {
        return contentCat;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public int getContentId() {
        return contentId;
    }
}
