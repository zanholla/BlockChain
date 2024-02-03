package edu.liberty;

import java.util.Date;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
	private String timestamp;
	private byte[] blockHash;
	private byte[] previousHash;
	private String blockData;
	
	Block() {
		blockData = "";
		timestamp = "";
		blockHash = null;
		previousHash = null;
	}
	Block(String data, byte[] prevHash) {
		try {
			newBlock(data, prevHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	public byte[] getBlockHash() {
		return blockHash;
	}
	public byte[] getPreviousHash() {
		return previousHash;
	}
	public String getBlockHashString() {
		return toHexString(blockHash);
	}
	public String getPreviousHashString() {
		return toHexString(previousHash);
	}
	public String getTimestamp() {
		return timestamp;
	}
	public String getBlockData() {
		return blockData;
	}
	
	
	/////////////////////////////////// code borrowed from https://www.geeksforgeeks.org/sha-256-hash-in-java/
	private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
 
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
	private static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
 
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }
	///////////////////////////////////
	private void setTimeStamp() {
		timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS a z").format(new Date());
	}
	private void hashBlock() throws NoSuchAlgorithmException {
		blockHash = getSHA(timestamp + getPreviousHashString() + blockData);
	}
	private void newBlock(String data, byte[] prevHash) throws NoSuchAlgorithmException {
		setTimeStamp();
		blockData = data;
		previousHash = prevHash;
		hashBlock();
	}
	public static Block genesis() {
		Block b = new Block("GenesisData.....................", "GenesisData.....................".getBytes());
		/*b.setTimeStamp();
		b.blockData = "GenesisData.....................";
		b.previousHash = b.getBlockData().getBytes();
		try {
			b.hashBlock();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}*/
		
		return b;
	}

	
}
