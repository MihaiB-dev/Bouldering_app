package Bouldering_app.domain;
import Bouldering_app.domain.Password_hashing;



public class User {
    private String full_name;
    private String hashPassword;


    public User (String full_name, String hashPassword){

        this.full_name = full_name;
        this.hashPassword = hashPassword;

    }

    public String getFull_name() {
        return full_name;
    }

    public String getHashPassword() {
        return hashPassword;
    }

}
