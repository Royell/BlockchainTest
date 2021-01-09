import java.awt.*;
import java.io.*;
import java.security.*;
import java.util.*;

public class Block {
	private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;
 
    public Block(String data, String previousHash, long timeStamp) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
    }
    // standard getters and setters

    public String calculateBlockHash() {
        String dataToHash = previousHash 
          + Long.toString(timeStamp) 
          + Integer.toString(nonce) 
          + data;
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes("UTF_8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            //logger.log(Level.SEVERE, ex.getMessage());
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }
    
    List<Block> blockchain = new ArrayList<>();
    int prefix = 4;
    String prefixString = new String(new char[prefix]).replace('\0', '0');
    
    //@Test
    public void givenBlockchain_whenValidated_thenSuccess() {
        boolean flag = true;
        for (int i = 0; i < blockchain.size(); i++) {
            String previousHash = i==0 ? "0" : blockchain.get(i - 1).getHash();
            flag = blockchain.get(i).getHash().equals(blockchain.get(i).calculateBlockHash())
              && previousHash.equals(blockchain.get(i).getPreviousHash())
              && blockchain.get(i).getHash().substring(0, prefix).equals(prefixString);
                if (!flag) break;
        }
        assertTrue(flag);
    }
}
