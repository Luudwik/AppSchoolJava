package AppJava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConn {

	Connection con;
	PreparedStatement pst;
	ResultSet rs;

	public void Connect() throws ClassNotFoundException, SQLException 
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://student.cs.uni.opole.pl/130594","130594@student.cs.uni.opole.pl", "130594");
			
		}
		catch (SQLException e) 
		{
			System.out.println(e);
		}
	}
}
