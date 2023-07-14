package AppJava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class handles the database connection for the application.
 */
public class DbConn {

    private Connection con;
    private String dataBase = "jdbc:mysql://student.cs.uni.opole.pl/130594";
    private String user = "130594@student.cs.uni.opole.pl";
    private String password = "130594";

    /**
     * Establishes a connection to the database.
     *
     * @return the connection object
     * @throws ClassNotFoundException if the database driver class is not found
     * @throws SQLException           if a database access error occurs
     */
    public Connection Connect() throws ClassNotFoundException, SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the database connection
            con = DriverManager.getConnection(dataBase, user, password);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return con;
    }
}
