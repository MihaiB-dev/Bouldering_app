package Bouldering_app.domain;

import java.time.LocalDateTime;

public class Ascent {

    private Route route;
    private LocalDateTime date;
    private int attempts;

    public Ascent(Route route, int attempts) {
        this.route = route;
        this.attempts = attempts;
        this.date = LocalDateTime.now();
    }

    public Route getRoute() {
        return new Route(route);
    }
}
