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
import fr.epita.iamcore.exceptions.*;

/**
 * @author Zick
 * DAO stands for Data Access Object
 */
public class FileIdentityDAO implements DAO{
	
	private File file;
	private PrintWriter p;
	private Scanner scan;

	/**
	 * This is the constructor of FileIdentityDAO
	 * It will create a file writer in append mode and open a file reader
	 */
	public FileIdentityDAO(){
		openWriter(true);
		try {
			FileReader r = new FileReader(file);
			this.scan = new Scanner(r);
			this.setIdentityLastId();
			
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
	 * @throws SaveDAOException if there is any save error occurs
	 */
	public void save(Identity i) throws SaveDAOException{		
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
	 * This is a listAll method, it will list all the identities in the file 
	 * @return  List of result identities
	 * @throws NumberFormatException if can not convert to integer, FileNotFoundException if the file does not exist
	 */
	public List<Identity> listAll(){
		ArrayList<Identity> results = new ArrayList<Identity>();
		
		while (this.scan.hasNext()) {
			this.scan.nextLine();
			
			// something before
			int id = 0;
			try{
				id = Integer.parseInt(this.scan.nextLine());
			}
			catch(NumberFormatException e){}
			String displayName = this.scan.nextLine();
			// something after
			String email = this.scan.nextLine();
			this.scan.nextLine();
			
			results.add(new Identity(id,displayName,email));
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
	
	/**
	 * This is a search method, it will search if the identity exist and return a list of result
	 * It uses scanIdentityOfFile method
	 * @param i the identity to search
	 * @return List of result identities
	 * @throws SearchDAOException if any search error occurs
	 */
	public List<Identity> search(Identity i) throws SearchDAOException{
		// TODO : complete with a real search
		return this.scanIdentityOfFile(i, 1);
	}
	
	/**
	 * This is a update method, it will update the identity with a new name and email
	 * It uses scanIdentityOfFile method
	 * @param i the identity need to be updated
	 * @throws UpdateDAOException if any update error occurs
	 */
	public void update(Identity i) throws UpdateDAOException{
		
		ArrayList<Identity> results = this.scanIdentityOfFile(i,2);
		
		//Erase the content of the old file
		openWriter(false);
		p.print("");
		p.close();
		
		//Open a file writer to write
		openWriter(true);
		for(int j=0;j<results.size();j++){
			try{
				this.save(results.get(j));
			}
			catch(SaveDAOException e){
				throw new UpdateDAOException();
			}

		}
	}
	
	/**
	 * This is a delete method, it will delete an identity
	 * It uses scanIdentityOfFile method
	 * @param i the identity to delete
	 * @throws DeleteDAOException if any delete error occurs
	 */
	public void delete(Identity i) throws DeleteDAOException{
		ArrayList<Identity> results = this.scanIdentityOfFile(i,3);
		
		//Erase the content of the old file
		openWriter(false);
		p.print("");
		p.close();
		
		//Open a file writer to write
		openWriter(true);
		for(int j=0;j<results.size();j++){
			try{
				this.save(results.get(j));
			}
			catch(SaveDAOException e){
				throw new DeleteDAOException();
			}
		}
		
	}
	
	/**
	 * This method is used to close the printer
	 */
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
	
	/**
	 * This is a method that will scan the file to find the latest ID of identity 
	 * It will set the counter of Identity class to be the latest ID
	 */
	public void setIdentityLastId(){
		
		int latestID = 0;

		while (this.scan.hasNext()) {
			this.scan.nextLine();
			try{
				latestID = Integer.parseInt(this.scan.nextLine());
			} catch(NumberFormatException e){
				e.printStackTrace();
			}
			
			//Scan to end an identity
			this.scan.nextLine();
			this.scan.nextLine();
			this.scan.nextLine();
		}
		
		this.scan.close();
		
		Identity.setCounter(latestID);
		
		//This is used to prevent the error
		try {
			this.scan = new Scanner(new FileReader(new File("/temp/tests/identities.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
