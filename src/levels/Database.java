package levels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String DATABASE_URL = "jdbc:sqlite:SQL.db";
    private static Connection connection;
    private static Database instance;

    private Database() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
