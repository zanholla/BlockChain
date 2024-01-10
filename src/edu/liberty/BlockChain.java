package edu.liberty;

import java.security.NoSuchAlgorithmException;

public class BlockChain {

	
	public static void main(String[] args) {
		try {
			String asdf = "asdfasdfasdfasdfasdfasdf";
            System.out.println(asdf + " : " + Block.toHexString(Block.getSHA(asdf)));
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return;
	}	
}
