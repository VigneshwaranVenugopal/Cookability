package map.cookability.NotificationFile;

/**
 * Created by yanglili on 3/17/18.
 */

public class Notifications {
    private String abstr;
    private String message;
//    private String fromImage;
    private String fromName;
    private String fromId;
    private String read;
    private String notificationId;
    private String currentId;
    private String currentName;
    private String recipeName;
    private String requestedTime;
    private String note;
//    private String currentImage;

    public Notifications(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public String getFromImage() {
//        return fromImage;
//    }
//
//    public void setFromImage(String fromImage) {
//        this.fromImage = fromImage;
//    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public String getAbstr() {
        return abstr;
    }

    public void setAbstr(String abstr) {
        this.abstr = abstr;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }
//
//    public String getCurrentImage() {
//        return currentImage;
//    }
//
//    public void setCurrentImage(String currentImage) {
//        this.currentImage = currentImage;
//    }


    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
