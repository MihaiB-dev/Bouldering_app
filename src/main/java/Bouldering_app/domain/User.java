package Bouldering_app.domain;


public class User {
    private String fullName;
    private String hashPassword;

    public User (String fullName, String hashPassword){

        this.fullName = fullName;
        this.hashPassword = hashPassword;

    }
    public User() {

    }
    public String getFullName() {
        return fullName;
    }

}
