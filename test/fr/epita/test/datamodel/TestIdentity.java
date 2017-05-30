package fr.epita.test.datamodel;

import fr.epita.iamcore.datamodel.Identity;
import fr.epita.iamcore.services.FileIdentityDAO;

public class TestIdentity {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Identity identity = new Identity("anonymous1","sdas@dsadas.com");
		System.out.println(identity.toString());
	}

}
