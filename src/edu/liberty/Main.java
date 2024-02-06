package edu.liberty;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.lvsq.jgossip.model.GossipMember;
import net.lvsq.jgossip.model.HeartbeatState;



public class Main {
	public static BlockChain b = new BlockChain();
	
	public static void main(String[] args) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		
		try {
		    /*FileInputStream fileInputStream = new FileInputStream("voterchain.sav");
		    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		    b = (BlockChain) objectInputStream.readObject();
		    objectInputStream.close();*/
		    BufferedReader br = new BufferedReader(new FileReader("voterchain.txt"));
		    String str;
		    str = br.readLine();
		    b = gson.fromJson(str, BlockChain.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		HandleData data = new HandleData();
		Runnable runnableTask = () -> {
			data.createGossipService();
		};
		List<Runnable> runnableTasks = new ArrayList<>();
		runnableTasks.add(runnableTask);
		executor.execute(runnableTask);
		
		String consoleInput = "";
		Console console = System.console();
		System.out.print("\033[H\033[2J");  
		System.out.flush();
		while (!consoleInput.equalsIgnoreCase("quit")) {
			System.out.println("\nWelcome to BlockChain voting!\n\n"
					+ "Commands:\n\t"
					+ "newvote\n\t"
					+ "addvoter\n\t"
					+ "addhost\n\t"
					+ "status\n\t"
					+ "validate\n\t"
					+ "results\n\t"
					+ "quit\n\n"
					);
			consoleInput = console.readLine("-> ");
			if (consoleInput.equalsIgnoreCase("newvote")) {
				String firstName;
				String lastName;
				String SSN;
				String voterCandidate;
				
				firstName = console.readLine("\nPlease enter the voter\'s first name:\n-> ");
				lastName = console.readLine("\nPlease enter the voter\'s last name:\n-> ");
				SSN = console.readLine("\nPlease enter the voter\'s social security number:\n-> ");
				voterCandidate = console.readLine("\nPlease enter the voter\'s chosen candidate:\n-> ");
				
				Vote vote = new Vote(firstName, lastName, SSN, voterCandidate);
				
				boolean duplicate = false;
				
				Vote blockVote;
				boolean found = false;
				Voter blockVoter;
				for (int i = 1; i < b.getLen(); ++i) {
					blockVote = gson.fromJson(b.getBlock(i).getBlockData() , Vote.class);
					if (blockVote.getCandidate() != null && !blockVote.getCandidate().equalsIgnoreCase("")) {
						if (blockVote.getVoterHash().equalsIgnoreCase(vote.getVoterHash())) {
							duplicate = true;
							System.out.println("Duplicate vote found.");
							break;
						}
					} else {
						blockVoter = gson.fromJson(b.getBlock(i).getBlockData(), Voter.class);
						if (blockVoter.getVoterHash().equalsIgnoreCase(vote.getVoterHash())) {
							found = true;
							break;
						}
					}
					if (i == b.getLen() - 1) {
						System.out.println("Voter not found in registration.");
					}
				}

				if (!duplicate && found) {
					b.addBlock(gson.toJson(vote));
					data.broadcastMessage(b.getBlock(b.getLen() - 1).getBlockData());
				}
				
			} else if (consoleInput.equalsIgnoreCase("addvoter")) {
				String firstName;
				String lastName;
				String SSN;
				
				firstName = console.readLine("\nPlease enter the voter\'s first name:\n-> ");
				lastName = console.readLine("\nPlease enter the voter\'s last name:\n-> ");
				SSN = console.readLine("\nPlease enter the voter\'s social security number:\n-> ");
				
				Voter voter = new Voter(firstName, lastName, SSN);
				boolean duplicate = false;
				
				Vote vote;
				Voter blockVoter;
				for (int i = 1; i < b.getLen(); ++i) {
					vote = gson.fromJson(b.getBlock(i).getBlockData() , Vote.class);
					if (vote.getCandidate() != null && !vote.getCandidate().equalsIgnoreCase("")) {
						continue;
					} else {
						blockVoter = gson.fromJson(b.getBlock(i).getBlockData(), Voter.class);
						if (blockVoter.getVoterHash().equalsIgnoreCase(voter.getVoterHash())) {
							duplicate = true;
							System.out.println("Duplicate voter found.");
							break;
						}
					}
				}
				if (!duplicate) {
					b.addBlock(gson.toJson(voter));
					data.broadcastMessage(b.getBlock(b.getLen() - 1).getBlockData());
				}

				
			} else if (consoleInput.equalsIgnoreCase("addhost")) {
				consoleInput = console.readLine("\nPlease enter the IP address of the new host:\n-> ");
				data.addNewMember(consoleInput);
				
			} else if (consoleInput.equalsIgnoreCase("status")) {
				for (int i = 0; i < data.getGossipService().getGossipManager().getLiveMembers().size(); ++i) {
					System.out.println(data.getGossipService().getGossipManager().getLiveMembers().get(i).getIpAddress());
				}
				for (int i = 1; i < b.getLen(); ++i) {
					b.printBlockData(i);
				}
			} else if (consoleInput.equalsIgnoreCase("validate")) {
				for (int i = 1; i < b.getLen(); ++i) {
					if (!b.getBlock(i - 1).getBlockHashString().equalsIgnoreCase(b.getBlock(i).getPreviousHashString())) {
						System.out.println("Failed validating hashes on block " + i);
					}
					/*byte[] tmp = b.getBlock(i - 1).getBlockHash();
					for (int j = 0; j < b.getBlock(i).getPreviousHash().length; ++i) {
						if (tmp[j] != b.getBlock(i).getPreviousHash()[j]) {
							System.out.println("Failed validating hashes on block " + i + " at byte " + j);
						}
					}*/
					
				}
				System.out.println("Hash sequence checked.");
			} else if (consoleInput.equalsIgnoreCase("results")) {
				ArrayList<Voter> voters = new ArrayList<Voter>();
				ArrayList<ArrayList<Vote>> votes = new ArrayList<ArrayList<Vote>>();
				Vote vote;
				Voter voter;
				int voteCount = 0;
				boolean found = false;;
				
				for (int i = 1; i < b.getLen(); ++i) {
					found = false;
					vote = gson.fromJson(b.getBlock(i).getBlockData() , Vote.class);
					if (vote.getCandidate() != null && !vote.getCandidate().equalsIgnoreCase("")) {
						++voteCount;
						for (int j = 0; j < votes.size(); ++j) {
							for (int k = 0; k < votes.get(j).size(); ++k) {
								if (vote.getCandidate().equalsIgnoreCase(votes.get(j).get(k).getCandidate())) {
									votes.get(j).add(vote);
									found = true;
									break;
								}
							}
							if (found == true) {
								break;
							} else if (j == votes.size() - 1) {
								votes.add(new ArrayList<Vote>());
								votes.get(j + 1).add(vote);
							}
						}
						//votes.add(vote);
					} else {
						voter = gson.fromJson(b.getBlock(i).getBlockData(), Voter.class);
						voters.add(voter);
					}
				}
				System.out.println(voters.size() + " registered voters.\n" + voteCount + " votes received.");
				for (int i = 0; i < votes.size(); ++i) {
					System.out.println(votes.get(i).size() + " votes for " + votes.get(i).get(0).getCandidate() + ".");
				}

			} else if (consoleInput.equalsIgnoreCase("quit")) {
				continue;
			} else {
				System.out.print("\033[H\033[2J");  
				System.out.flush();
				System.out.println("\nInvalid Input. Please try again...");
			}
		}
		try {
		    BufferedWriter br = new BufferedWriter(new FileWriter("hostlist.txt"));
		    for (Map.Entry<GossipMember, HeartbeatState> str : data.getGossipService().getGossipManager().getEndpointMembers().entrySet()) {
		        br.write(str.getKey().getIpAddress() + System.lineSeparator());
		    }
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.stopGossipService();
		executor.shutdown();
		try {
		    if (!executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
		    	executor.shutdownNow();
		    } 
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
		
		try {
		    /*FileOutputStream fileOutputStream = new FileOutputStream("voterchain.sav");
		    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		    objectOutputStream.writeObject(b);
		    objectOutputStream.flush();
		    objectOutputStream.close();*/
			BufferedWriter br = new BufferedWriter(new FileWriter("voterchain.txt"));
		    br.write(gson.toJson(b));
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Goodbye...");/*
		Set<Thread> threads = Thread.getAllStackTraces().keySet();
		System.out.printf("%-15s \t %-15s \t %-15s \t %s\n", "Name", "State", "Priority", "isDaemon");
		for (Thread t : threads) {
		    System.out.printf("%-15s \t %-15s \t %-15d \t %s\n", t.getName(), t.getState(), t.getPriority(), t.isDaemon());
		}*/
		System.exit(0);
		return;
	}
	/*
	 * TODO:
	 >* Use JSON to create data payloads that can be appropriately used to store any sort of data
	 >* Based on the type of JSON object, create a list of hosts that can be used for decentralization
	 >* Votes can also be a type of JSON. Hash a voter's personal details to keep them anonymous but able to have their participation traced
	 >* Communicate payloads to hosts with an encrypted message
	 >* Perform hash comparison with previous block to ensure chain validity
	 >* Decide on a port to perform data transfers on
	 >* Set up a console to cast and receive votes. If voter's hash is found already, do not permit the vote to be cast
	 >* Potentially perform hash comparison against database of registered voters to ensure that voters are legitimate
	 >* Voter registration may also be performed by making a blockchain entry
	*/
}
