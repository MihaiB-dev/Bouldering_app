package Bouldering_app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ascent {

    private Route route;
    private LocalDate date;
    private int attempts;

    public Ascent(Route route, int attempts) {
        this.route = route;
        this.attempts = attempts;
        this.date = LocalDate.now();
    }
    public Ascent(Route route, int attempts, LocalDate date) {
        this.route = route;
        this.attempts = attempts;
        this.date = date;
    }
    public Ascent(Ascent ascent){
        this.route = ascent.route;
        this.date = ascent.date;
        this.attempts = ascent.attempts;
    }

    public Route getRoute() {
        return new Route(route);
    }

    @Override
    public String toString() {
        return
                "route: " + route +
                "\ndate completed: " + date +
                ", attempts for this ascent: " + attempts;
    }
}
