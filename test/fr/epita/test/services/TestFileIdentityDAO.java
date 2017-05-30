/**
 * 
 */
package fr.epita.test.services;

import fr.epita.iamcore.datamodel.Identity;
import fr.epita.iamcore.services.FileIdentityDAO;

/**
 * @author Zick
 *
 */
public class TestFileIdentityDAO {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Identity identity = new Identity("anonymous2","sdas@dsadas.com");
		FileIdentityDAO f = new FileIdentityDAO();
		f.save(identity);
		System.out.println(f.search(identity));

	}

}
