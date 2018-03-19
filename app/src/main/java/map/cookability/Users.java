package map.cookability;

import java.util.Map;

/**
 * Created by theon on 3/7/2018.
 */

public class Users {
    public String name;
    public String uid;
    public String email;
    public String photourl;
    public String documentid;

    Users(String name, Map<String,Object> data){
        this.documentid = name;
        this.name = (String)data.get("name");
        this.uid = (String)data.get("uid");
        this.email = (String)data.get("email");
        this.photourl = (String)data.get("photourl");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }
}
