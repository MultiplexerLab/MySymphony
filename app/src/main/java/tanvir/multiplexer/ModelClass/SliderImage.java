package tanvir.multiplexer.ModelClass;

import java.io.Serializable;

/**
 * Created by Lenovo on 4/23/2018.
 */

public class SliderImage implements Serializable{
    private String image_url;
    private String description;

    public SliderImage(String image_url, String description) {
        this.image_url = image_url;
        this.description = description;
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
