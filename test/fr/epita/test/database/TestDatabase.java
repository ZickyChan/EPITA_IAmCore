/**
 * 
 */
package fr.epita.test.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Zick
 *
 */
public class TestDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		String connectionString = "jdbc:derby://localhost:1527/sample";
		String user = "IAMCORE"; //This is for the schema
		String password = "dsadasda";
		
		Connection connection = DriverManager.getConnection(connectionString, user, password);
		
		PreparedStatement preparedStatement = connection.prepareStatement("insert into IDENTITIES (NAME, EMAIL, BIRTHDATE) values(?, ?, ?)");
		
		preparedStatement.setString(1, "Quentin");
		preparedStatement.setString(2, "qdeca@qdeca.com");
		preparedStatement.setDate(3,  new Date(new java.util.Date().getTime()));
		preparedStatement.execute();
		preparedStatement.close();
		
		//PreparedStatement selectAllStatement = connection.prepareStatement("select * from IDENTITIES");
		
		PreparedStatement select = connection.prepareStatement("SELECT * from IDENTITIES WHERE UID = ? AND NAME = ?");
		select.setInt(1, 2);
		select.setString(2, "Quentin");
		
		System.out.println(select.toString());		
		ResultSet results = select.executeQuery();
		
		
		while (results.next()){
			String displayName = results.getString("NAME");
			String email = results.getString("EMAIL");
			//Date birthDate = results.getDate("IDENTITY_BIRTHDATE");
			
			System.out.println(displayName + " - " + email );
			
		}
		
		select.close();
		results.close();
		connection.close();
	}

}
