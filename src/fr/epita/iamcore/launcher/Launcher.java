package fr.epita.iamcore.launcher;


import java.io.FileNotFoundException;
import java.util.Scanner;

import fr.epita.iamcore.datamodel.Identity;
import fr.epita.iamcore.services.Authenticator;
import fr.epita.iamcore.services.FileIdentityDAO;
import fr.epita.loggingsystem.Logger;
import fr.epita.loggingsystem.LoggerConfiguration;

public class Launcher {
	
	/**
	 * @param args
	 */
	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) throws FileNotFoundException{		
		
		LoggerConfiguration config = new LoggerConfiguration("/temp/application.log");
		Logger logger = new Logger(config);
		
		boolean authentication = Authenticator.authenticated(scan);
		
		FileIdentityDAO file = new FileIdentityDAO();
		
		if(!authentication){
			logger.log("unable to authenticate");
			scan.close();
			return;
		}
		
		System.out.println("Successfully authentication!");
		System.out.println("Welcome to the system! What would you like to do?");
		while(true){	
			System.out.println("1. Create Identity");
			System.out.println("2. Save Identity");
			System.out.println("3. Update Identity");
			System.out.println("4. Delete Identity");
			System.out.println("5. Exit");
			
			
			String input = scan.nextLine().trim();
			
			boolean valid = true;
			String result[];
			switch(input){
				//This is create case
				case "1":
					result = askForIdentity("create");
					Identity i = new Identity(result[0],result[1]);
					System.out.println("You have created an identity: ");
					System.out.println(i.toString());
					break;
				//This is save case
				case "2":
					result = askForIdentity("save");
					file.save(new Identity(result[0],result[1]));
					valid = true;
					break;
				//This is update case
				case "3":
					System.out.println("Enter the identity's id that you want to update: ");
					try{
						int id = Integer.parseInt(scan.nextLine());
						result = askForIdentity("update");
						file.update(new Identity(id,result[0],result[1]));
						System.out.println("You have sucessfully updated the identity");
					}
					catch(Exception e){
						e.printStackTrace();
					}
					break;
				//This is delete case
				case "4":
					System.out.println("Enter the identity's id that you want to delete: ");
					try{
						int id = Integer.parseInt(scan.nextLine());
						result = askForIdentity("delete");
						file.delete(new Identity(id,result[0],result[1]));
						System.out.println("You have sucessfully deleted the identity");
					}
					catch(Exception e){
						e.printStackTrace();
					}
					break;
				case "5":
					System.out.println("Thank you for using our service! Have a good day!");
					valid = true;
					break;
				default:
					System.out.println("Invalid option! Please try again!");	
					valid = false;
					break;
			}
			
			logger.log("User chose option " + input);
			if (valid&&input.equalsIgnoreCase("5")){
				break;
			}
		}
		
		file.closeDAO();
	}
	
	public static String[] askForIdentity(String purpose){
		String[] result = new String[2];
		
		//Ask for a name
		if(purpose.equalsIgnoreCase("update")){
			System.out.println("Enter new identity name: ");
		}
		else{
			System.out.println("Enter the identity name you want to " + purpose +  ": ");
		}
		result[0] = scan.nextLine().trim();
		
		//Ask for a email
		if(purpose.equalsIgnoreCase("update")){
			System.out.println("Enter new identity mail: ");
		}
		else{
			System.out.println("Enter the identity mail you want to " + purpose +  ": ");
		}
		result[1] = scan.nextLine().trim();
		
		//Return result
		return result;
	}
	
}
