package Bouldering_app.services;

import Bouldering_app.domain.*;

import java.util.Scanner;  // Import the Scanner class
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static User[] users;
    private Scanner myObj;
    private static int lastIndex;

    private Password_hashing p = new Password_hashing(16);
    public UserService() {
        this.users = new User[1000];
        myObj = new Scanner(System.in);
        lastIndex = 0;
    }

    public int SignUp(){
        System.out.println("---------Sign-up---------");

        System.out.print("Climber = 1, Setter = 2, close = 0: ");
        String switchel = myObj.nextLine();


        System.out.print("Full name: ");
        String full_name = myObj.nextLine();

        System.out.print("Password: ");
        switch (switchel){
            case "1":
                users[lastIndex] = new Climber(full_name, p.hash(myObj.nextLine().toCharArray()));
                lastIndex ++;
                break;
            case "2":
                users[lastIndex] = new Setter(full_name, p.hash(myObj.nextLine().toCharArray()));
                lastIndex ++;
        }
        return lastIndex - 1;
    }


    public int LogIn(){
        System.out.println("---------LogIn---------");

        System.out.print("Full name: ");
        String full_name = myObj.nextLine();

        System.out.print("Password: ");
        char[] hashed_password = myObj.nextLine().toCharArray();
        int i;
        for(i = 0; i < lastIndex; i ++){
            if (users[i].getFull_name().equals(full_name) && p.authenticate(hashed_password, users[i].getHashPassword())){
                return i;
            }
        }
        System.out.println("Numele sau parola sunt gresite");
        return -1;
    }

    public static User getUser(int index){return users[index];}
    public boolean isSetter(int index){return users[index] instanceof Setter;}
    public boolean isClimber(int index){return users[index] instanceof Climber;}



}
