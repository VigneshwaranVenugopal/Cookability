package map.cookability;

import java.util.Map;

/**
 * Created by theon on 3/7/2018.
 */

public class users {
    public String name;
    public String uid;
    public String email;
    public String photourl;
    public String documentid;
    users(String name, Map<String,Object> data){
        this.documentid = name;
        this.name = (String)data.get("name");
        this.uid = (String)data.get("uid");
        this.email = (String)data.get("email");
        this.photourl = (String)data.get("photourl");
    }
}
