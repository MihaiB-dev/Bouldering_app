package Bouldering_app.domain;

import java.util.ArrayList;
import java.util.List;

public class Climber extends User{
    private int avgGrade;
    private Stats userStats;
    private List<Ascent> ascents;


    public Climber(String full_name, String hashPassword) {
        super(full_name, hashPassword);
        avgGrade = 0;
        userStats = new Stats();
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

}
