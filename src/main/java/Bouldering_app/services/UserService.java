package Bouldering_app.services;

import Bouldering_app.domain.*;

import java.util.Scanner;  // Import the Scanner class
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> users;
    private Scanner myObj;
    private Password_hashing p = new Password_hashing(16);
    public UserService() {
        this.users = new ArrayList<>();
        myObj = new Scanner(System.in);
    }

    public void SignUp(){

        System.out.println("---------------");
        System.out.println("----Sign-up----");
        System.out.println("---------------");


        System.out.print("Climber = 1, Setter = 2, close = 0: ");
        String switchel = myObj.nextLine();


        System.out.print("Full name: ");
        String full_name = myObj.nextLine();

        System.out.print("Password: ");
        switch (switchel){
            case "1":
                users.add(new Climber(full_name, p.hash(myObj.nextLine().toCharArray())));
                break;
            case "2":
                users.add(new Setter(full_name, p.hash(myObj.nextLine().toCharArray())));
        }

        //test only
        System.out.println("add user" + users.getLast().getFull_name());
        System.out.println("password hashed" + users.getLast().getHashPassword());
    }

}
