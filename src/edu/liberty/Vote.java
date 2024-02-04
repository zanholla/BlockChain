package edu.liberty;

import java.security.NoSuchAlgorithmException;

public class Vote {
	private String voterHash;
	private String candidate;
	Vote() {
		voterHash = "";
		candidate = "";
	}
	Vote(String firstName, String lastName, int age, String SSN, String candidate) {
		generateVote(firstName, lastName, age, SSN, candidate);
	}
	
	public void generateVote(String firstName, String lastName, int age, String SSN, String candidate) {
		try {
			this.voterHash = Block.toHexString(Block.getSHA(firstName + lastName + age + SSN));
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
}
