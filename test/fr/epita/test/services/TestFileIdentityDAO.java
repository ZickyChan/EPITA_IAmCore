/**
 * 
 */
package fr.epita.test.services;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import fr.epita.iamcore.datamodel.Identity;
import fr.epita.iamcore.exceptions.DeleteDAOException;
import fr.epita.iamcore.exceptions.SaveDAOException;
import fr.epita.iamcore.exceptions.SearchDAOException;
import fr.epita.iamcore.exceptions.UpdateDAOException;
import fr.epita.iamcore.services.Authenticator;
import fr.epita.iamcore.services.FileIdentityDAO;
import fr.epita.loggingsystem.Logger;
import fr.epita.loggingsystem.LoggerConfiguration;

/**
 * @author Zick
 *
 */
public class TestFileIdentityDAO {

	private static Scanner scan = new Scanner(System.in);
	private static Logger logger;
	
	/**
	 * This main method is used to run the program
	 * It will ask the user to authenticate himself/herself
	 * Users can choose the following option:
	 * 1. Show all identities
	 * 2. Search Identity"
	 * 3. Save Identity
	 * 4. Update Identity
	 * 5. Delete Identity
	 * 6. Exit
	 * The program will continue asking the user to perform an action unless he/she chooses exit
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException{		
		
		LoggerConfiguration config = new LoggerConfiguration("/temp/fileDAOapplication.log");
		logger = new Logger(config);
		
		int invalidTime = 0;
		
		while(true){
			//Ask for the authentication
			System.out.println("Enter the username: ");
			String user = scan.nextLine().trim();
			System.out.println("Enter the password: ");
			String password = scan.nextLine().trim();
			
			boolean authentication = Authenticator.authenticated(user,password);
		
			if(!authentication){
				System.out.println("Invalid username or password");
				logger.log("unable to authenticate");
				invalidTime++;
				
				//If there are 3 failures, the program closes
				if(invalidTime == 3){
					scan.close();
					System.out.println("You have been blocked from the system! System shut down!");
					logger.log("block access");
					return;
				}
			}
			else{
				logger.log("User " + user + " has logged in");
				break;
			}
		}
		
		
		FileIdentityDAO file = new FileIdentityDAO();
		
		System.out.println("Successfully authentication!");
		System.out.println("Welcome to the system! What would you like to do?");
		while(true){	
			System.out.println("1. Show all identites");
			System.out.println("2. Search Identity");
			System.out.println("3. Save Identity");
			System.out.println("4. Update Identity");
			System.out.println("5. Delete Identity");
			System.out.println("6. Exit");
			
			
			String input = scan.nextLine().trim();
			
			boolean valid = true;
			String result[];
			
			List<Identity> iList;
			switch(input){
				//This is the show identities cases
				case "1":
					iList = file.listAll();
					for(int i=0; i<iList.size(); i++){
						System.out.println(iList.get(i).display());
					}
					break;
				//This is search case
				case "2":
					result = askForIdentity("search");
					try {
						iList = file.search(new Identity(Integer.parseInt(result[0]),result[1],result[2]));
						for(int i=0; i<iList.size(); i++){
							System.out.println(iList.get(i).display());
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						System.out.println("Unexpected error occured");
						logger.log("Unexpected error occured");
					} catch (SearchDAOException e) {
						// TODO Auto-generated catch block
						System.out.println("There are searching errors");
						logger.log("Searching error occured");
					}
					break;
				//This is save case
				case "3":
					result = askForIdentity("save");
					try {
						file.save(new Identity(result[1],result[2]));
					} catch (SaveDAOException e1) {
						// TODO Auto-generated catch block
						System.out.println("There are save errors");
						logger.log("Save error occured");
					}
					valid = true;
					break;
					//This is update case
				case "4":
					result = askForIdentity("update");
					try {
						file.update(new Identity(Integer.parseInt(result[0]),result[1],result[2]));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						System.out.println("Unexpected error occured");
						logger.log("Unexpected error occured");
					} catch (UpdateDAOException e) {
						// TODO Auto-generated catch block
						System.out.println("There are update errors");
						logger.log("Update error occured");
					}
					System.out.println("You have sucessfully updated the identity");
					break;
				//This is delete case
				case "5":
					result = askForIdentity("delete");
					try {
						file.delete(new Identity(Integer.parseInt(result[0]),result[1],result[2]));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						System.out.println("Unexpected error occured");
						logger.log("Unexpected error occured");
					} catch (DeleteDAOException e) {
						// TODO Auto-generated catch block
						System.out.println("There are delete errors");
						logger.log("Delete error occured");
					}
					System.out.println("You have sucessfully deleted the identity");
					break;
				case "6":
					System.out.println("Thank you for using our service! Have a good day!");
					valid = true;
					break;
				default:
					System.out.println("Invalid option! Please try again!");	
					valid = false;
					break;
			}
			
			logger.log("User chose option " + input);
			if (valid&&input.equalsIgnoreCase("6")){
				break;
			}
		}
		
		file.closeDAO();
	}
	
	public static String[] askForIdentity(String purpose){
		String[] result = new String[3];
		boolean invalid = true;
		
		//Ask for id
		while(invalid && !purpose.equalsIgnoreCase("save")){
			System.out.println("Enter the identity id you want to " + purpose +  ":" );
			result[0] = scan.nextLine().trim();
			
			try{
				Integer.parseInt(result[0]);
				invalid = false;
			}
			catch(Exception e){
				System.out.println("Invalid id!");
				logger.log("User typed invalid id");
				invalid = true;
			}
		}
		
		//Ask for a name
		if(purpose.equalsIgnoreCase("update")){
			System.out.println("Enter new identity name: ");
		}
		else{
			System.out.println("Enter the identity name you want to " + purpose +  ": ");
		}
		result[1] = scan.nextLine().trim();
		
		//Ask for a email
		if(purpose.equalsIgnoreCase("update")){
			System.out.println("Enter new identity email: ");
			result[2] = scan.nextLine().trim();
		}
		else if(purpose.equalsIgnoreCase("save")){
			System.out.println("Enter the identity email you want to " + purpose +  ": ");
			result[2] = scan.nextLine().trim();
		}
		else{
			result[2] = "";
		}
		
		
		//Return result
		return result;
	}

}
