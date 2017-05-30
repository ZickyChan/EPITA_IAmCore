package fr.epita.iamcore.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Authenticator {
	private static Map<String,String> accounts = createMap();
    private static Map<String, String> createMap()
    {
        Map<String,String> myMap = new HashMap<String,String>();
        myMap.put("adm", "123");
        myMap.put("admin", "456");
        return myMap;
    }

	
	/**
	 * This method is used to do the authentication.
	 * The system will take the input from the user and compare with the system's database
	 * System's database is the map which contains the user name and the following passwords
	 * @param scan is the Scanner to read user input
	 * @return
	 */
	public static boolean authenticated (Scanner scan) {
		System.out.println("Enter the username: ");
		String user = scan.nextLine().trim();
		System.out.println("Enter the password: ");
		String password = scan.nextLine().trim();
		
		
		
		if(accounts.containsKey(user) && accounts.get(user).equals(password)){
			return true;
		}
		System.out.println("Failed authentication!");
		return false;
	}
}
