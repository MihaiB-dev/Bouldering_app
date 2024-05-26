package Bouldering_app.services;

import Bouldering_app.databaseConnections.DatabaseUser;
import Bouldering_app.domain.*;

import java.util.Scanner;  // Import the Scanner class


public class UserService {

    private User current_user = null;
    private Scanner myObj;
    private static int lastIndex; //lastIndex

    private DatabaseUser databaseUser = DatabaseUser.DatabaseUser();
    private Password_hashing p = new Password_hashing(16);
    public UserService() {
        this.current_user = new User();
        myObj = new Scanner(System.in);
        lastIndex = databaseUser.getLastId();
        System.out.println("Last index: " + lastIndex);


        //if the database is empty, we add the admin
        if(lastIndex == 1){
            databaseUser.insertSetter("admin", p.hash("ciscosecpa55".toCharArray()), lastIndex);
            lastIndex++;
        }
    }

    public int SignUp(){
        System.out.println("---------Sign-up---------");

        System.out.print("Climber = 1, Setter = 2, close = 0: ");
        String switchel = myObj.nextLine();


        System.out.print("Full name: ");
        String full_name = myObj.nextLine();

        boolean isDuplicate = databaseUser.verifyDuplicateName(full_name);
        if(isDuplicate){
            System.out.println("Numele este deja folosit");
            return -1;
        }

        System.out.print("Password: ");
        switch (switchel){
            case "1":
                databaseUser.insertClimber(full_name, p.hash(myObj.nextLine().toCharArray()), lastIndex);
                current_user = databaseUser.getById(lastIndex);
                lastIndex++;
                break;
            case "2":
                databaseUser.insertSetter(full_name, p.hash(myObj.nextLine().toCharArray()), lastIndex);
                current_user = databaseUser.getById(lastIndex);
                lastIndex++;
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
        int id_result =  databaseUser.authenticate(p, full_name, hashed_password);
        if(id_result == 0){
            System.out.println("Numele sau parola sunt gresite");
            return -1;
        }
        else{
            if (full_name.equals("admin")){
                current_user = databaseUser.getById(id_result);
                return -2509;
            }
            return id_result;
        }


    }

//    public static void showAscents(int index){
//        if(!isClimber(index)){return;}
//
//        ((Climber) current_user[index]).showAscents_sortByDifficulty();
//
//
//    }
    public  void profile(){
        if (isSetter()) {
            System.out.println(((Setter) current_user).printProfile());
        } else {
            System.out.println(((Climber) current_user).printProfile());
        }
    }
    public User getUser(){return current_user;}
    public boolean isSetter(){return current_user instanceof Setter;}
    public boolean isClimber(){return current_user instanceof Climber;}


    public void showAllUsers(User user) {
        //verify if the name is admin
        if (!user.getFullName().equals("admin")) {
            System.out.println("Only admin can see all users");
            return;
        }
        databaseUser.showAllUsers();
    }

    public void deleteUser(User user, String fullName) {
        //verify if the name is admin
        if (!user.getFullName().equals("admin")) {
            System.out.println("Only admin can see all users");
            return;
        }
        databaseUser.deleteUser(fullName);
    }
}
