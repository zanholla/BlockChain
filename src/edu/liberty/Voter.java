package edu.liberty;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Voter {
	private String voterHash;
	private String timestamp;

	Voter() {
		voterHash = "";
		timestamp = "";
	}
	Voter(String firstName, String lastName, String SSN) {
		generateVoter(firstName, lastName, SSN);
		setTimestamp();
	}
	public String getVoterHash() {
		return voterHash;
	}
	public String getTimestamp() {
		return timestamp;
	}
	private void setTimestamp() {
		timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss a z").format(new Date());
	}
	public void generateVoter(String firstName, String lastName, String SSN) {
		try {
			this.voterHash = Block.toHexString(Block.getSHA(firstName + lastName + SSN));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
