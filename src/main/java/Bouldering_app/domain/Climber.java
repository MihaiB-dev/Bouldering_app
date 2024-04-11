package Bouldering_app.domain;

import Bouldering_app.services.UserInteractionService;

import java.util.ArrayList;
import java.util.List;

public class Climber extends User implements UserInteractionService {
    private Grade avgGrade;
    private Stats userStats;
    private List<Ascent> ascents;


    public Climber(String full_name, String hashPassword) {
        super(full_name, hashPassword);
        avgGrade = Grade._4;
        userStats = new Stats(0,0,0,0);
        ascents = new ArrayList<>();
    }

    public Grade getAvgGrade() {
        return avgGrade;
    }

    public Stats getUserStats() {
        return userStats;
    }

    public List<Ascent> getAscents() {
        return ascents;
    }

    //change the avgGrade
    //Update user stats
    public void addAscent(Ascent ascent){
        ascents.add(ascent);

        //make avg grade
        int sum = 0;
        for(Ascent element : ascents){
            Grade grade  = element.getRoute().getOriginalGrade();
            sum += grade.ordinal();
        }
        avgGrade = Grade.values()[(int)(sum/ascents.size())];

        //update user stats
        //create a function of adding points
        userStats.Update(ascent);
    }

    @Override
    public String printProfile() {
        return "name: " + this.getFull_name()
                         + "\navg_grade: " + this.avgGrade + " Color: " + this.avgGrade.getColor()
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
