package map.cookability;

import java.util.Map;

/**
 * Created by theon on 3/7/2018.
 */

public class Users {
    private  String name, image, type, dish;
//    Users(String name, Map<String,Object> data){
//        this.documentid = name;
//        this.name = (String)data.get("name");
//        this.uid = (String)data.get("uid");
//        this.email = (String)data.get("email");
//        this.photourl = (String)data.get("photourl");
//    }


    public Users(String name, String image, String type, String dish) {
        this.name = name;
        this.image = image;
        this.type = type;
        this.dish = dish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }
}
