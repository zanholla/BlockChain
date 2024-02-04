package edu.liberty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

	public static void main(String[] args) {
		BlockChain b = new BlockChain();
		
		Vote vote = new Vote("gamerman", "bobberton", 41, "575-09-2245", "Gus");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
		b.addBlock(gson.toJson(vote));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		vote.generateVote("leeroy", "jenkins", 69, "634-56-8678", "Jimbob");
		b.addBlock(gson.toJson(vote));
		
		b.printBlockData(b.getLen() - 3);
		b.printBlockData(b.getLen() - 2);
		b.printBlockData(b.getLen() - 1);
		
		Vote vote2 = gson.fromJson(b.getBlock(b.getLen() - 1).getBlockData(), Vote.class);
		System.out.println("\n" + vote2.getVoterHash() + " " + vote2.getCandidate());
		
		Vote vote3 = gson.fromJson(b.getBlock(b.getLen() - 2).getBlockData(), Vote.class);		
		System.out.println("\n" + vote3.getVoterHash() + " " + vote3.getCandidate());

		
		
		return;
	}
	/*
	 * TODO:
	 >* Use JSON to create data payloads that can be appropriately used to store any sort of data
	 * Based on the type of JSON object, create a list of hosts that can be used for decentralization
	 * Votes can also be a type of JSON. Hash a voter's personal details to keep them anonymous but able to have their participation traced
	 * Communicate payloads to hosts with an encrypted message
	 * Perform hash comparison with previous block to ensure chain validity
	 * Decide on a port to perform data transfers on
	 * Set up a console to cast and receive votes. If voter's hash is found already, do not permit the vote to be cast
	 * Potentially perform hash comparison against database of registered voters to ensure that voters are legitimate
	 * Voter registration may also be performed by making a blockchain entry
	*/
}
