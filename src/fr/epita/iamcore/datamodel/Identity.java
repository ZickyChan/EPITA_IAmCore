package fr.epita.iamcore.datamodel;

import java.util.Map;

public class Identity {
	private String displayName;
	private int uid;
	private String email;
	private static int counter = 0;
	Map<String,String> attributes;
	
	public Identity(String displayName, String email) {
		this.displayName = displayName;
		this.uid = ++counter;
		this.email = email;
	}
	public Identity(int id, String displayName, String email) {
		this.displayName = displayName;
		this.uid = id;
		this.email = email;
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
	
	
	
}
