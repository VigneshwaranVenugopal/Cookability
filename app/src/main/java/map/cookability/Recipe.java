package map.cookability;
import java.util.Map;

/**
 * Created by theon on 3/1/2018.
 */

public class Recipe {
    public String documentName;
    public String title;
    public String description;
    public String imageUrl;
    Recipe(String name, Map<String,Object> data){
        documentName = name;
        title = (String) data.get("first");
        imageUrl = (String) data.get("image");
    }
}