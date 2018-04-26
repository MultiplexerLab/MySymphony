package lct.mysymphony.ModelClass;

/**
 * Created by Lenovo on 4/24/2018.
 */

public class Notifications {
    private String title;
    private String description;
    private String time;

    public Notifications(String title, String description, String time) {
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
