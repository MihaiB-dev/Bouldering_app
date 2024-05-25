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
}
