package fr.epita.iamcore.datamodel;

import java.util.Map;

public class Identity {
	private String displayName;
	private int uid;
	private String email;
	private static int counter = 0;
	Map<String,String> attributes;
	
	/**
	 * This is a constructor for Identity class.
	 * This constructor set the displayName and the email by the relevant arguments.
	 * It set the uid automatically by using counter
	 * @param displayName
	 * @param email
	 */
	public Identity(String displayName, String email) {
		this.displayName = displayName;
		this.uid = ++counter;
		this.email = email;
	}
	
	/**
	 * This is a constructor for Identity class.
	 * This constructor set the uid, the displayName and the email by the relevant arguments.
	 * @param id
	 * @param displayName
	 * @param email
	 */
	public Identity(int id, String displayName, String email) {
		this.displayName = displayName;
		this.uid = id;
		this.email = email;
	}
	
	/**
	 * @param counter the counter to set
	 */
	public static void setCounter(int i){
		counter = i;
	}
	
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Identity [displayName=" + displayName + ", uid=" + uid + ", email=" + email + ", attributes="
				+ attributes + "]";
	}
	
	/**
	 * This method is used to print the identity in the format:
	 * <pre>
	 * --- Identity ---
	 * Identity id: uid
	 * Identity display name: displayName
	 * Identity email: email
	 * --------------
	 * </pre>
	 * @return
	 */
	public String display(){
		String result = "";
		result += "---Identity---\n";
		result += "Identity id: " + this.uid + "\n";
		result += "Identity display name: " + this.displayName + "\n";
		result += "Identity email: " + this.email + "\n";
		result += "--------------";
		return result;
	}
	
	
	
}
