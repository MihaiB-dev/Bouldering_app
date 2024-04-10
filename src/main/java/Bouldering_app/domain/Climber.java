package Bouldering_app.domain;

import Bouldering_app.services.UserInteractionService;

import java.util.ArrayList;
import java.util.List;

public class Climber extends User implements UserInteractionService {
    private int avgGrade;
    private Stats userStats;
    private List<Ascent> ascents;


    public Climber(String full_name, String hashPassword) {
        super(full_name, hashPassword);
        avgGrade = 0;
        userStats = new Stats(0,0,0,0);
        ascents = new ArrayList<>();
    }

    public int getAvgGrade() {
        return avgGrade;
    }

    public Stats getUserStats() {
        return userStats;
    }

    public List<Ascent> getAscents() {
        return ascents;
    }

    @Override
    public String printProfile() {
        return "name: " + this.getFull_name()
                         + "\navg_grade: " + this.avgGrade
                         + "\nStats:\n" + userStats ;
//                         + "\n" + showAscents());
    }

    //this will show the routes in descending order by difficulty
    @Override
    public int chooseRoute() {
        return 0;
    }

    @Override
    public void showImage() {

    }

}
