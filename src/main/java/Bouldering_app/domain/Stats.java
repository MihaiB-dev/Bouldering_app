package Bouldering_app.domain;

import java.util.Scanner;

import static java.lang.Double.min;

public class Stats {
    public double strength;
    public double technique;
    public double endurance;
    public double flexibility;

    public Stats(double strength, double technique, double endurance, double flexibility) {
        this.strength = strength;
        this.technique = technique;
        this.endurance = endurance;
        this.flexibility = flexibility;
    }


    public String printForRoute(){
        return
                " strength=" + strength +
                ", technique=" + technique +
                ", endurance=" + endurance +
                ", flexibility=" + flexibility;
    }

    @Override
    public String toString() {
        return "Strength = " + progressBar(strength) +
                "\nTechnique = " + progressBar(technique) +
                "\nEndurance = " + progressBar(endurance) +
                "\nFlexibitility = " + progressBar(flexibility);
    }

    private String progressBar(double value){
        int completed = (int)value;
        int remaining = 100 - completed;

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < completed; i++) {
            bar.append("=");
        }
        for (int i = 0; i < remaining; i++) {
            bar.append(" ");
        }
        bar.append("] ");
        bar.append(value).append("%");

        return bar.toString();
    }

    public void Update(Ascent ascent){
        Stats routeStats = ascent.getRoute().getRoutestats();
        Grade routeGrade = ascent.getRoute().getOriginalGrade();
        this.strength = min(100, this.strength  + routeGrade.importance() * routeStats.strength / 10);
        this.technique = min(100, this.technique  + routeGrade.importance() * routeStats.technique / 10);
        this.endurance +=  min(100, this.endurance  + routeGrade.importance() * routeStats.endurance / 10);
        this.flexibility +=  min(100, this.flexibility  + routeGrade.importance() * routeStats.flexibility / 10);
    }
}
