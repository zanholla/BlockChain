package edu.liberty;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Vote {
	private String voterHash;
	private String candidate;
	private String timestamp;
	Vote() {
		voterHash = "";
		candidate = "";
		timestamp = "";
	}
	Vote(String firstName, String lastName, String SSN, String candidate) {
		generateVote(firstName, lastName, SSN, candidate);
		setTimestamp();
	}
	Vote(String firstName, String lastName, String SSN, String candidate, String time) {
		generateVote(firstName, lastName, SSN, candidate);
		setTimestamp(time);
	}
	
	public void generateVote(String firstName, String lastName, String SSN, String candidate) {
		try {
			this.voterHash = Block.toHexString(Block.getSHA(firstName + lastName + SSN));
			this.candidate = candidate;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	public String getCandidate() {
		return candidate;
	}
	public void setCandidate(String candidate) {
		this.candidate = candidate;
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
	private void setTimestamp(String time) {
		timestamp = time;
	}
}
