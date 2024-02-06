package edu.liberty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.lvsq.jgossip.core.GossipService;
import net.lvsq.jgossip.event.GossipListener;
import net.lvsq.jgossip.model.GossipMember;
import net.lvsq.jgossip.model.GossipState;

public class DataListener implements GossipListener {
	GsonBuilder builder = new GsonBuilder();
	Gson gson = builder.create();
	Voter voter;
	Vote vote;
	
	@Override
	public void gossipEvent(GossipMember member, GossipState state, Object payload) {
		if (state == GossipState.UP) {
			//System.out.println(member + " is up.");
			//System.out.println("gameing4");
		}
		if (state == GossipState.DOWN) {
			//System.out.println(member + " has gone down.");
			//System.out.println("gameing3");
		}
		if (state == GossipState.JOIN) {
			//System.out.println(member + " has joined. Sending payload");
			//System.out.println("gameing2");
			//publish("asdfjkl;");
		}
		if (state == GossipState.RCV) {
			//System.out.println(member + " has sent message " + payload);
			//System.out.println("gameing");
			vote = null;
			voter = null;
			vote = gson.fromJson((String) payload, Vote.class);
		    /*Pattern pattern = Pattern.compile("\"candidate\\\\\"", Pattern.CASE_INSENSITIVE);
		    Matcher matcher = pattern.matcher((String)payload);*/
			if (vote.getCandidate() != null && vote.getCandidate() != "") {
				Main.b.addBlock((String)payload, vote.getTimestamp());
				if (!Main.b.getBlock(Main.b.getLen() - 1).getPreviousHashString().equalsIgnoreCase(Main.b.getBlock(Main.b.getLen() - 2).getBlockHashString())) {
					System.out.println("Vote addedd but invalid. Hash mismatch");
				}
				System.out.println("Vote added from received message.");
			} else {
				voter = gson.fromJson((String) payload, Voter.class);
				Main.b.addBlock((String)payload, voter.getTimestamp());
				System.out.println("Voter added from received message.");
			}
		}
	}

}
