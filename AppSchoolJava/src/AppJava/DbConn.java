package AppJava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConn {

	private Connection con;
	private String dataBase = "jdbc:mysql://student.cs.uni.opole.pl/130594";
	private String user = "130594@student.cs.uni.opole.pl";
	private String password = "130594";

	/**
	 * Connecting to database
	 * @return connection to database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection Connect() throws ClassNotFoundException, SQLException 
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(dataBase,user,password);
		}
		catch (SQLException e) 
		{
			System.out.println(e);
		}
		return con;
	}
}
