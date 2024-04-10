package Bouldering_app.domain;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Route {

    private Grade originalGrade;
    private Path namePicture;
    private int nrAttempts;
    private Grade liveGrade;
    private LocalDate dateAdded;
    private Stats routestats;

    public Route(Grade originalGrade, Path namePicture, Stats routestats) {
        this.originalGrade = originalGrade;
        this.namePicture = namePicture;
        this.routestats = routestats;

        this.liveGrade = originalGrade;
        this.nrAttempts = 0;
        this.dateAdded = LocalDate.now();
    }

    public Route(Route route){
        this.originalGrade = route.getOriginalGrade();
        this.namePicture = route.getNamePicture();
        this.routestats = route.getRoutestats();

        this.liveGrade = route.getLiveGrade();
        this.nrAttempts = route.getNrAttempts();
        this.dateAdded = route.getDateAdded();
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }
    public Grade getOriginalGrade() {return originalGrade;}
    public Path getNamePicture() {
        return namePicture;
    }
    public int getNrAttempts() {
        return nrAttempts;
    }
    public Grade getLiveGrade() {return liveGrade;}
    public Stats getRoutestats() {
        return routestats;
    }


    @Override
    public String toString() {
        return
                "originalGrade=" + originalGrade +
                ", namePicture=" + namePicture.getFileName().toString() +
                ", nrAttempts=" + nrAttempts +
                ", liveGrade=" + liveGrade +
                ", dateAdded=" + dateAdded +
                "\nroutestats=" + routestats.printForRoute()+
                "\n";
    }
}
