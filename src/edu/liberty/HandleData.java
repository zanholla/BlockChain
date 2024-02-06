package edu.liberty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.*;

import net.lvsq.jgossip.core.GossipService;
import net.lvsq.jgossip.core.GossipSettings;
import net.lvsq.jgossip.event.GossipListener;
import net.lvsq.jgossip.model.GossipMember;
import net.lvsq.jgossip.model.SeedMember;

public class HandleData {
	public static GossipService gossipService;
	private Vote vote = new Vote();
	private GossipListener listener = new DataListener();
	private List<SeedMember> seedNodes = new ArrayList<SeedMember>();
	private GossipSettings settings = new GossipSettings();
	
	public static String getIP() throws MalformedURLException {
		String urlString = "http://checkip.amazonaws.com/";
		URL url = new URL(urlString);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
		    try {
				return br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
	}
	
	public GossipService getGossipService() {
		return gossipService;
	}
	
	public void createGossipService() {
		SeedMember seed = new SeedMember();
		settings.setGossipInterval(10000);

		String ip;

		/*try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ip = "0.0.0.0";
		}*/
		
		try { //use later on in the process
			ip = getIP();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			ip = "0.0.0.0";
		}
		
		try {
		    BufferedReader br = new BufferedReader(new FileReader("hostlist.txt"));
		    String str;
		    int i = 0;
		    while((str = br.readLine()) != null) {
		    	System.out.println("Read in host " + str);
				seed.setCluster("VoteChain");
				seed.setIpAddress(str);
				seed.setPort(31113);
				seedNodes.add(seed);
				++i;
		    }
		    br.close();
		    if (i == 0) {
				seed.setCluster("VoteChain");
				seed.setIpAddress(ip);
				seed.setPort(31113);
				seedNodes.add(seed);
		    }
		} catch (IOException e) {
			e.printStackTrace();
			seed.setCluster("VoteChain");
			seed.setIpAddress(ip);
			seed.setPort(31113);
			seedNodes.add(seed);
		}
		

		/*
		if (ip != "80.241.210.126") {
			seed.setCluster("VoteChain");
			seed.setIpAddress("194.163.137.102");
			seed.setPort(31113);
			seedNodes.add(seed);
		}
		if (ip != "194.163.137.102") {
			seed.setCluster("VoteChain");
			seed.setIpAddress("80.241.210.126");
			seed.setPort(31113);
			seedNodes.add(seed);
		}*/
		
		//GossipMember gossipMember = new GossipMember("VoteChain", ip, 31113, null, null);
		

		
		//String id = Block.toHexString(Block.getSHA("1"));
		
		try {
			gossipService = new GossipService("VoteChain", ip, 31113, null, seedNodes, settings, listener);
		} catch (Exception e) {
			e.printStackTrace();
			gossipService = null;
		}
		gossipService.start();
		
		//gossipService.getGossipManager().publish("Hello World");
	}

	public void stopGossipService() {
		gossipService.shutdown();
		System.out.println("Shutting down service...");
	}
	public void addNewMember(String ipAddress) {
		SeedMember seed = new SeedMember();
		seed.setCluster("VoteChain");
		seed.setIpAddress(ipAddress);
		seed.setPort(31113);
		seedNodes.add(seed);
	}
	public void broadcastMessage(String message) {
		gossipService.getGossipManager().publish(message);
	};
}
