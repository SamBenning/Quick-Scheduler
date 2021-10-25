package sample.model;

/**
 * Model object class to represent user objects from the database.*/
public class User {

    private int userId;
    private String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }


    @Override
    public String toString() {
        return "(ID: " + userId + ")" + userName;
    }
}
