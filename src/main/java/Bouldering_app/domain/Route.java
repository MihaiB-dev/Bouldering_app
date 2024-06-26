package Bouldering_app.domain;

import java.nio.file.Path;
import java.time.LocalDate;

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

    public Route(String originalGrade, String path, int nrAttempts, String liveGrade, LocalDate dateAdded) {
        this.originalGrade = Grade.valueOf("_" + originalGrade);
        this.namePicture = Path.of(path);
        this.nrAttempts = nrAttempts;
        this.liveGrade = Grade.valueOf("_" + liveGrade);
        this.dateAdded = dateAdded;
    }

    public Route(String originalGrade, String path, int nrAttempts, String liveGrade, LocalDate dateAdded, Stats stats) {
        this.originalGrade = Grade.valueOf("_" + originalGrade);
        this.namePicture = Path.of(path);
        this.nrAttempts = nrAttempts;
        this.liveGrade = Grade.valueOf("_" + liveGrade);
        this.dateAdded = dateAdded;
        this.routestats = stats;
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
    public String getPath(){return namePicture.toString();}


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

    public int getdatabaseId() {

        //the filename file is after the last / and before the .jpg or .png
        //get the last / from the string
        String filename = namePicture.getFileName().toString();
        int line = filename.lastIndexOf("/");
        int dot = filename.lastIndexOf(".");
        return Integer.parseInt(filename.substring(line + 1, dot));

    }
}
