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
    Recipe(String name, Map<String,Object> data){
        documentName = name;
        title = (String) data.get("name");
        imageUrl = (String) data.get("imageurl");
        steps = (String) data.get("steps");
        ingridients = (String) data.get("ingridients");
        nonveg = (String) data.get("nonveg");
        category = (String) data.get("category");
    }
}