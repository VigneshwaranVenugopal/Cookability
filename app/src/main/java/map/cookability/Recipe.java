package map.cookability;
import java.util.Map;

/**
 * Created by theon on 3/1/2018.
 */

public class Recipe {
    public String documentName;
    public String title;
    public String steps;
    public String ingridients;
    public String nonveg;
    public String imageUrl;
    public String category;

    public Recipe(){}

    Recipe(String name, Map<String,Object> data){
        documentName = name;
        title = (String) data.get("name");
        imageUrl = (String) data.get("imageurl");
        steps = (String) data.get("steps");
        ingridients = (String) data.get("ingridients");
        nonveg = (String) data.get("nonveg");
        category = (String) data.get("category");
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getIngridients() {
        return ingridients;
    }

    public void setIngridients(String ingridients) {
        this.ingridients = ingridients;
    }

    public String getNonveg() {
        return nonveg;
    }

    public void setNonveg(String nonveg) {
        this.nonveg = nonveg;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    private String dish, image, name, type;
//
//    public Recipe(String dish, String image, String name, String type) {
//        this.dish = dish;
//        this.image = image;
//        this.name = name;
//        this.type = type;
//    }
//
//    public String getDish() {
//        return dish;
//    }
//
//    public void setDish(String dish) {
//        this.dish = dish;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
}