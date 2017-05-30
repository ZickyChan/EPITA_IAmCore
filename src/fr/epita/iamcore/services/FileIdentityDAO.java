/**
 * 
 */
package fr.epita.iamcore.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.epita.iamcore.datamodel.Identity;

/**
 * @author Zick
 * DAO stands for Data Access Object
 */
public class FileIdentityDAO {
	
	private File file;
	private PrintWriter p;
	private Scanner scan;
	//Constructor
	public FileIdentityDAO(){
		openWriter(true);
		try {
			FileReader r = new FileReader(file);
			this.scan = new Scanner(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This is a save method, it will record the identity in the file using
	 * that format:
	 * <pre>
	 * --- Identity ---
	 * uid
	 * displayName
	 * email
	 * --- Identity ---
	 * </pre>
	 * @param i the identity to record
	 */
	public void save(Identity i){		
		//Concrete write operations in the file
		this.p.println("--- Identity ---");
		this.p.println(i.getUid());
		this.p.println(i.getDisplayName());
		this.p.println(i.getEmail());
		this.p.println("--- Identity ---");
		
		//commit the buffer in real life
		this.p.flush();
		
	}
	
	/**
	 * This is a search method, it will search if the identity exist and return a list of result
	 * It uses scanIdentityOfFile method
	 * @param i the identity to search
	 * @return 
	 */
	public List<Identity> search(Identity i){
		// TODO : complete with a real search
		return this.scanIdentityOfFile(i, 1);
	}
	
	/**
	 * This is a search method, it will search if the identity exist
	 * It uses scanIdentityOfFile method
	 * @param i the identity to search
	 */
	public void update(Identity i){
		
		ArrayList<Identity> results = this.scanIdentityOfFile(i,2);
		
		//Erase the content of the old file
		openWriter(false);
		p.print("");
		p.close();
		
		//Open a file writer to write
		openWriter(true);
		for(int j=0;j<results.size();j++){
			this.save(results.get(j));
		}
	}
	
	/**
	 * This is a search method, it will search if the identity exist
	 * It uses scanIdentityOfFile method
	 * @param i the identity to search
	 */
	public void delete(Identity i){
		ArrayList<Identity> results = this.scanIdentityOfFile(i,3);
		
		//Erase the content of the old file
		openWriter(false);
		p.print("");
		p.close();
		
		//Open a file writer to write
		openWriter(true);
		for(int j=0;j<results.size();j++){
			this.save(results.get(j));
		}
		
	}
	
	public void closeDAO(){
		p.close();
	}
	
	
	/**
	 * This is a method which open a file writer and print writer
	 * in order to write a file
	 * @param append is a boolean that tells if the file writer will overwrite or append to the file
	 * true for append and false for overwrite
	 */
	public void openWriter(boolean append){
		this.file = new File("/temp/tests/identities.txt");
		if(!file.exists()){
			//Create directory structure
			file.getParentFile().mkdirs();
			
			//Create the new file
			try {
				//	risky operation
				file.createNewFile();
			} catch (IOException e) {
				// print error message
				e.printStackTrace();
			}
		}
		
		try {
			FileWriter w = new FileWriter(file,append);
			this.p = new PrintWriter(w);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This is a method that will scan all the identities in the file 
	 * and return an array list that is formed by different purposes.
	 * Purpose 1. To search for an identity
	 * Purpose 2. To update an identity
	 * Purpose 3. To delete an identity
	 * @param i the identity which is used to perform an action
	 * @param opt the option that represent a purpose
	 */
	public ArrayList<Identity> scanIdentityOfFile(Identity i, int opt){
		ArrayList<Identity> results = new ArrayList<Identity>();
		
		while (this.scan.hasNext()) {
			this.scan.nextLine();
			
			// something before
			int id = 0;
			try{
				id = Integer.parseInt(this.scan.nextLine());
			}
			catch(Exception e){}
			String displayName = this.scan.nextLine();
			// something after
			String email = this.scan.nextLine();
			this.scan.nextLine();
			
			switch(opt){
				//This is the searching case
				case 1:
					if(i.getDisplayName().equalsIgnoreCase(displayName)){
						results.add(new Identity(id,displayName,email));
					}
					break;
				//This is the update case
				case 2:
					if(i.getUid() == id){
						results.add(i);
					}
					else{
						results.add(new Identity(id,displayName,email));
					}
					break;
				//This is the delete case
				case 3:
					if(i.getUid() != id){
						results.add(new Identity(id,displayName,email));
					}
					break;
				default:
						break;
			}
			
		}

		this.scan.close();
		
		//This is used to prevent the error
		try {
			this.scan = new Scanner(new FileReader(new File("/temp/tests/identities.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results;
	}
	
}
