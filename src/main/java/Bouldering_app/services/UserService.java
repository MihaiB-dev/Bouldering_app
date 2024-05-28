package Bouldering_app.services;

import Bouldering_app.databaseConnections.DatabaseUser;
import Bouldering_app.domain.*;

import java.util.Scanner;  // Import the Scanner class


public class UserService {

    private User current_user = null;
    private Scanner myObj;
    private static int lastIndex; //lastIndex

    private DatabaseUser databaseUser = DatabaseUser.getDatabaseUser();
    private final Password_hashing p = new Password_hashing(16);
    public UserService() {
        this.current_user = new User();
        myObj = new Scanner(System.in);
        lastIndex = databaseUser.getLastId();

        //if the database is empty, we add the admin
        if(lastIndex == 1){
            System.out.println("Creating tables...");
            System.out.println("The database is empty, we add the admin, setter and climber");

            databaseUser.insertSetter("admin", p.hash("ciscosecpa55".toCharArray()), lastIndex);
            lastIndex++;
            databaseUser.insertClimber("climber", p.hash("ciscosecpa55".toCharArray()), lastIndex);
            lastIndex++;
            databaseUser.insertSetter("setter", p.hash("ciscosecpa55".toCharArray()), lastIndex);
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
            current_user = databaseUser.getById(id_result);

            if (full_name.equals("admin")){
                return -2509;
            }

            return id_result;
        }

    }

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
            System.out.println("Only admin can delete an user");
            return;
        }
        databaseUser.deleteUser(fullName);
    }

    public void forgotPassword() {
        System.out.println("---------Forgot Password---------");
        System.out.print("Full name: ");
        String full_name = myObj.nextLine();
        System.out.print("Email to mihaibivol.dev@gmail.com to change your password, here you can write what code sent you: ");
        String enter_secret_code = myObj.nextLine();
        if (enter_secret_code.equals("pao")) {
            System.out.print("New password: ");
            char[] hashed_password = myObj.nextLine().toCharArray();
            databaseUser.updatePassword(full_name, p.hash(hashed_password));
        } else {
            System.out.println("Wrong code");
        }

    }

    public void updateUser(int loggedUser) {
        current_user = databaseUser.getById(loggedUser);
        if (isClimber()) {
            //add to the user the new ascent
            int idClimber = DatabaseUser.getDatabaseUser().getIdClimber(current_user);
            ((Climber) current_user).updateAscents(idClimber);
        }

    }

    public void showAscents() {
        if (!isClimber()) {
            System.out.println("Only climbers can see their ascents");
            return;
        }
        ((Climber) current_user).showAscents_sortByDifficulty();
    }
}
