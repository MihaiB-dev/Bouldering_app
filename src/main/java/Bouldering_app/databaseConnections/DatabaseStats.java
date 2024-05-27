package Bouldering_app.databaseConnections;

import Bouldering_app.domain.Stats;
import config.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseStats {
    private static DatabaseStats databaseStats = null;

    private DatabaseStats(){

    }
    public static DatabaseStats DatabaseStats(){
        if (databaseStats == null){
            databaseStats = new DatabaseStats();
        }
        return databaseStats;
    }

    public static Stats getById(int userStats) {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectStats = "SELECT * FROM stats WHERE id = ?";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(selectStats);
            preparedStatement.setInt(1, userStats);
            preparedStatement.executeQuery();
            preparedStatement.getResultSet().next();
            return new Stats(preparedStatement.getResultSet().getDouble("strength"),
                    preparedStatement.getResultSet().getDouble("techinque"),
                    preparedStatement.getResultSet().getDouble("endurance"),
                    preparedStatement.getResultSet().getDouble("flexibility"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public int getLastId(){
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectStats = "SELECT id FROM stats ORDER BY id DESC LIMIT 1";
        int id_stats = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectStats);
            preparedStatement.executeQuery();
            preparedStatement.getResultSet().next();
            id_stats = preparedStatement.getResultSet().getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id_stats;
    }
    //create a stat and return its id
    public int createStats(){
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        //firstly create a new Stats row
        String insertStats = "INSERT INTO stats (strength, techinque, endurance, flexibility) VALUES (0,0,0,0)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertStats);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return getLastId();
    }

    public int createStats(Stats stats){
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        //firstly create a new Stats row
        String insertStats = "INSERT INTO stats (strength, techinque, endurance, flexibility) VALUES (?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertStats);
            preparedStatement.setDouble(1, stats.strength);
            preparedStatement.setDouble(2, stats.technique);
            preparedStatement.setDouble(3, stats.endurance);
            preparedStatement.setDouble(4, stats.flexibility);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getLastId();
    }

    public void updateStats(int idClimber, Stats stats) {
        //change the object from stats table where idClimber ==  climber.id with the new stats
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String updateStats = "UPDATE stats SET strength = ?, techinque = ?, endurance = ?, flexibility = ?" +
                            " WHERE id = (SELECT userStats FROM climber WHERE id = ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(updateStats);
            preparedStatement.setDouble(1, stats.strength);
            preparedStatement.setDouble(2, stats.technique);
            preparedStatement.setDouble(3, stats.endurance);
            preparedStatement.setDouble(4, stats.flexibility);
            preparedStatement.setInt(5, idClimber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
