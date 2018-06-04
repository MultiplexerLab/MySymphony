package lct.mysymphony.ModelClass;

public class Icon {
    private int id;
    private String category;
    private String type;
    private String image;
    private int sequence;
    private String categoryTitle;

    public Icon(int id, String category, String type, String image, int sequence, String categoryTitle) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.image = image;
        this.sequence = sequence;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
