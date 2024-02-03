package edu.liberty;

import java.util.ArrayList;

public class BlockChain {
	private int len;
	ArrayList<Block> chain;
	
	BlockChain() {
		chain = new ArrayList<Block>();
		createChain();
	}
	public int getLen() {
		return len;
	}	
	public Block getBlock(int index) {
		try {
			return chain.get(index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void printBlockData(int index) {
		try {
			System.out.println("Block Data: " + chain.get(index).getBlockData());
			System.out.println("Block Hash: " + chain.get(index).getBlockHashString());
			System.out.println("Previous Hash: " + chain.get(index).getPreviousHashString());
			System.out.println("Timestamp: " + chain.get(index).getTimestamp() + "\n");
			chain.get(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createChain() {
		chain.add(0, Block.genesis());
		len = 1;
	}
	public void addBlock(String data) {
		chain.add(new Block(data, chain.get(len - 1).getBlockHash()));
		len = chain.size();
	}
}
