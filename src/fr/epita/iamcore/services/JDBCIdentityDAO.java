/**
 * 
 */
package fr.epita.iamcore.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.iamcore.datamodel.Identity;
import fr.epita.iamcore.exceptions.*;

/**
 * @author tbrou
 *
 */
public class JDBCIdentityDAO {
	
	Connection connection;
	
	/**
	 * This is the constructor of the JDBCIdentityDAO class
	 * It opens a connection to the jdbc database
	 * @throws SQLException if there is any sql error occurs
	 * 
	 */
	public JDBCIdentityDAO() throws SQLException {
		// TODO Auto-generated method stub
		String connectionString = "jdbc:derby://localhost:1527/sample";
		String user = "user"; //This is for the schema
		String password = "123";
				
		this.connection = DriverManager.getConnection(connectionString, user, password);
	}

	/**
	 * This is a showAll method, it will list all the identities in the database 
	 * @throws SQLException if there is any sql error occurs
	 * @return List of result identities
	 */
	public List<Identity> showAll() throws SQLException{
		List<Identity> returnedList = new ArrayList<Identity>();

		PreparedStatement selectAll = this.connection.prepareStatement("SELECT * from IDENTITIES");
		ResultSet results = selectAll.executeQuery();
		
		boolean isEmpty = true;
		
		while (results.next()){
			int id = results.getInt("UID");
			String displayName = results.getString("NAME");
			String email = results.getString("EMAIL");
				
			returnedList.add(new Identity(id,displayName,email));
			isEmpty = false;
		}
			
		selectAll.close();
		
		if(isEmpty){
			System.out.println("The database is empty!");
		}
		
		return returnedList;
	}
	
	/**
	 * This is a save method, it will record the identity in to the database
	 * @param identity is the identity to record
	 * @throws SaveDAOException if there is any save error occurs
	 */
	public void save(Identity identity) throws SaveDAOException{
		try{
			PreparedStatement preparedStatement = this.connection.prepareStatement("insert into IDENTITIES (NAME, EMAIL) values(?, ?)");
			preparedStatement.setString(1, identity.getDisplayName());
			preparedStatement.setString(2, identity.getEmail());
			preparedStatement.execute();
			preparedStatement.close();
		}
		catch(SQLException e){
			throw new SaveDAOException();
		}
	}
	
	/**
	 * This is a search method, it will search if the identity exist and return a list of result
	 * @param identity is the identity to search
	 * @throws SearchDAOException if there is any search error occurs
	 * @return List of result identities
	 */
	public List<Identity> search(Identity identity) throws SearchDAOException{
		List<Identity> returnedList = new ArrayList<Identity>();
		
		try{
			PreparedStatement searchStatement = this.connection.prepareStatement("SELECT * from IDENTITIES WHERE UID = ? AND NAME = ?");
			searchStatement.setInt(1, identity.getUid());
			searchStatement.setString(2, identity.getDisplayName());
			ResultSet results = searchStatement.executeQuery();

			boolean isEmpty = true;
			
			while (results.next()){
				int id = results.getInt("UID");
				String displayName = results.getString("NAME");
				String email = results.getString("EMAIL");
				
				returnedList.add(new Identity(id,displayName,email));
				isEmpty = false;
			}
			
			if(isEmpty){
				System.out.println("No results found!");
			}
			
			searchStatement.close();
		}
		catch(SQLException e){
			throw new SearchDAOException();
		}
		
		return returnedList;
	}
	
	/**
	 * This is a delete method, it will delete an identity from database
	 * @param i the identity to delete
	 * @throws DeleteDAOException if there is any delete error occurs
	 */
	public void delete(Identity i) throws DeleteDAOException{
		try{
			PreparedStatement deleteStatement = this.connection.prepareStatement("DELETE FROM IDENTITIES WHERE UID = ? AND NAME = ?");
			deleteStatement.setInt(1, i.getUid());
			deleteStatement.setString(2, i.getDisplayName());
			
			deleteStatement.execute();
			deleteStatement.close();
		}
		catch(SQLException e){
			throw new DeleteDAOException();
		}
	}
	
	/**
	 * This is a update method, it will update the identity with a new name and email
	 * It uses scanIdentityOfFile method
	 * @param i the identity need to be updated
	 * @throws UpdateDAOException if there is any update error occurs
	 */
	public void update(Identity i) throws UpdateDAOException{
		try{
			PreparedStatement updateStatement = this.connection.prepareStatement("UPDATE IDENTITIES SET NAME = ?, EMAIL = ? WHERE UID = ?");
			updateStatement.setString(1, i.getDisplayName());
			updateStatement.setString(2, i.getEmail());
			updateStatement.setInt(3, i.getUid());
			
			updateStatement.execute();
			updateStatement.close();
		}
		catch(SQLException e){
			throw new UpdateDAOException();
		}
	}
	
	/**
	 * This method is used to close the database connection
	 * @throws SQLException if there is any sql error occurs
	 */
	public void closeDatabase() throws SQLException{
		this.connection.close();
	}
}
