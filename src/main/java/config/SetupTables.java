package config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupTables {

    public void createTables() {
        String createUser = """
            
                CREATE TABLE IF NOT EXISTS user (
                id int PRIMARY KEY AUTO_INCREMENT,
                fullName varchar(100),
                hashPassword varchar(10000)
            )
            """;
        String createSetter = """
                
                CREATE TABLE IF NOT EXISTS setter (
                id int PRIMARY KEY AUTO_INCREMENT,
                id_user int,
                dateEmployee date,
                foreign key(id_user) references user(id) ON DELETE CASCADE
            )
            """;
        String createStats = """
                CREATE TABLE IF NOT EXISTS stats (
                id int PRIMARY KEY AUTO_INCREMENT,
                strength double(6,5),
                techinque double(6,5),
                endurance double(6,5),
                flexibility double(6,5)
            )
            """;
        String createClimber = """
                CREATE TABLE IF NOT EXISTS climber (
                id int PRIMARY KEY AUTO_INCREMENT,
                id_user int,
                avgGrade varchar(20),
                userStats int,
                
                foreign key(id_user) references user(id) ON DELETE CASCADE,
                foreign key(userStats) references stats(id) ON DELETE CASCADE
            )
            """;
        String createRoute = """
                CREATE TABLE IF NOT EXISTS route (
                id int PRIMARY KEY AUTO_INCREMENT,
                originalGrade varchar(20),
                path varchar(100),
                nrAttempts int,
                liveGrade varchar(20),
                dateAdded date,
                routeStats int,
                id_setter int,
                
                foreign key (id_setter) references setter(id),
                foreign key (routeStats) references stats(id)
            )
            """;

        String createAscent = """
                CREATE TABLE IF NOT EXISTS ascent (
                id int PRIMARY KEY AUTO_INCREMENT,
                id_route int,
                date date,
                attempts int,
                id_climber int,
                
                foreign key (id_route) references route(id),
                foreign key (id_climber) references climber(id)
            )
            """;

        String checkAdmin = "SELECT * FROM user WHERE fullName = 'admin'";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute(createUser);

            statement = connection.createStatement();
            statement.execute(createSetter);

            statement = connection.createStatement();
            statement.execute(createStats);

            statement = connection.createStatement();
            statement.execute(createClimber);

            statement = connection.createStatement();
            statement.execute(createRoute);

            statement = connection.createStatement();
            statement.execute(createAscent);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}