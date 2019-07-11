/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task3;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

/**
 * This class represents a simple BlockChain.
 * Dependent on Block
 * @author  Zhexin Chen
 * @date    March 18 2019
 * 
 */
public class BlockChain extends java.lang.Object{
    List<Block> blockList;
    String chainHash;
    
    /**
     * blockList: an ArrayList holds Blocks
     * chainHash: a String holds a SHA256 hash 
     * of the most recently added Block
     * 
     * This constructor creates an empty ArrayList for Block storage
     * This constructor sets the chain hash to the empty string
     */
    public BlockChain(){
        blockList = new ArrayList<>();
        chainHash = "";
    }
    
     /**
     * add a new block to the BlockChain
     * @param newBlock 
     */
    void addBlock(Block newBlock){
        newBlock.setPreviousHash(this.chainHash);
        //run proofOfWork to get good hash
        this.chainHash = newBlock.proofOfWork();
        this.blockList.add(newBlock);
    }
    
    /**
     * get the size of the chain
     * @return the size of the chain
     */
    int getChainSize(){
        return blockList.size();
    }
    
    /**
     * retrieve the latest block
     * @return the last block of the chain
     */
    Block getLatestBlock(){
    return blockList.get(blockList.size()-1);
    }
    
    /**
     * get the current time
     * @return the current system time
     */
    Timestamp getTime(){
    return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * hashes per second of the computer holding this chain
     * @return number of times the computer can hash "00000000" 
     * in 1000 milliseconds
     */
    int hashesPerSecond() {
        int times = 0;
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        
        String s = "00000000";
        //keep hashing until 1ooo milliseconds passed
        while (duration<=1000){
        byte[] bytesOfText = s.getBytes();
        try{
        MessageDigest md_sha256 = MessageDigest.getInstance("SHA-256");
        byte[] theDigest = md_sha256.digest(bytesOfText);
        String hexString = javax.xml.bind.DatatypeConverter.printHexBinary(theDigest);
        }
        catch (NoSuchAlgorithmException e){
        e.printStackTrace();
        }
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);
        //increment times
        times++;
        }


        return times;
    }
    
    /**
     * starting from the leftmost block, check whether the difficulties and
     * the hashes match with each other
     * @return true if and only if the chain is valid
     */
    boolean isChainValid(){
        if (blockList.size()==0)
            return false;
        
        if (blockList.size()==0){
            Block gB = blockList.get(0);
            String hashString = gB.proofOfWork();
            // check the leading 0's
            for (int i = 0;i<gB.getDifficulty();i++){
                if (hashString.charAt(i)!='0')
                    return false;
            }
            // check hashes
            if (!hashString.equals(this.chainHash))
                return false;
            return true;
        }
        
        else {
        for (int i = 1; i<blockList.size();i++){
            // check hashes match proof of work
            String hashString = blockList.get(i-1).proofOfWork();
            // check leading 0's
            for (int j =0;j<blockList.get(i-1).getDifficulty();j++){
            if (hashString.charAt(j)!='0')
                return false;
            }
            
            if (!hashString.equals(blockList.get(i).getPreviousHash()))
                return false;
        }
        
        // check last block's hash and leadning 0's
        Block gB = blockList.get(blockList.size()-1);
        String hashString = gB.proofOfWork();
        for (int i = 0;i<gB.getDifficulty();i++){
            if (hashString.charAt(i)!='0')
                return false;
            }
        if (!hashString.equals(this.chainHash))
            return false;
        return true;
        }
    }
    
    /**
     * Test driver of the BlockChain
     * Create a BlockChain and add a genesis block
     * Continuously provide 7 options
     * 0. View basic blockchain status.
     * 1. Add a transaction to the blockchain.
     * 2. Verify the blockchain.
     * 3. View the blockchain.
     * 4. Corrupt the chain.
     * 5. Hide the corruption by repairing the chain.
     * 6. Exit.
     * 
     * Time consumption of adding blocks with difficulty level 4 and 5
     * Difficulty level 4: on average 180 milliseconds to add
     * Difficulty level 5: on average 3059 milliseconds to add, 
     *  and there are greater variances in the sample of Difficulty level 5
     * 
     * Time consumption of verifying blocks
     * Computed by verifying a chain with only two blocks -
     *  a genesis block and a added block (either 4 or 5 difficulty level)
     * Difficulty level 4: on average 178 milliseconds with large variances
     * Difficulty level 5: on average 3422 milliseconds with larger variances
     * 
     * @param args not used
     */
//    public static void main(String[] args){
//        System.out.println("Run:");
//        System.out.println("Block Chain Menu");
//        // create new block chain
//        BlockChain bC = new BlockChain();
//        // create the genesis block
//        Block genesisBlock = new Block(0,new Timestamp(System.currentTimeMillis()),"Genesis",2);
//        genesisBlock.setPreviousHash("");
//        // add the block to the block chain
//        bC.addBlock(genesisBlock);
//        bC.chainHash = genesisBlock.proofOfWork();
//        boolean flag = true;
//        // provide the options
//        while (flag){
//        System.out.println("0. View basic blockchain status.");
//        System.out.println("1. Add a transaction to the blockchain.");
//        System.out.println("2. Verify the blockchain.");
//        System.out.println("3. View the blockchain.");
//        System.out.println("4. Corrupt the chain.");
//        System.out.println("5. Hide the corruption by repairing the chain.");
//        System.out.println("6. Exit.");
//        Scanner in = new Scanner(System.in);
//        String userInput = in.nextLine();
//        
//        // print the status of the block chain
//        if (userInput.equals("0")){
//        System.out.println("Current size of chain: "+bC.getChainSize());
//        System.out.println("Current hases per second by this machine: "+bC.hashesPerSecond());
//        System.out.println("Difficulty of most recent block: "+bC.getLatestBlock().getDifficulty());
//        System.out.println("Nonce for most recent block: "+bC.getLatestBlock().getNonce().toString());
//        System.out.println("Chain hash:\n"+bC.chainHash);
//        }
//        
//        // ask the user to provide difficulty and transaction data
//        // then create the block
//        // then add the block to the block chain
//        if (userInput.equals("1")){    
//            System.out.println("Enter difficulty > 0");
//            String diff = in.nextLine();
//            int diffLevel = Integer.parseInt(diff);
//            System.out.println("Enter transaction");
//            String userData = in.nextLine();
//            long startTime = System.currentTimeMillis();
//            Block newBlock = new Block(bC.getChainSize(),new Timestamp(System.currentTimeMillis()),userData,diffLevel);
//            newBlock.setPreviousHash(bC.chainHash);
//            bC.chainHash=newBlock.proofOfWork();
//            bC.blockList.add(newBlock);
//            long endTime = System.currentTimeMillis();
//            System.out.println("Total execution time to add this block was " + (endTime-startTime) + " milliseconds");
//        }
//        
//        // call isChainValid() to verify the block chain
//        if (userInput.equals("2")){
//            System.out.println("Verifying entire chain");
//            long startTime = System.currentTimeMillis();
//            boolean isValid = bC.isChainValid();
//            long endTime = System.currentTimeMillis();
//            System.out.println("Chain verification: "+isValid);
//            System.out.println("Total execution time required to verify the chain was "+ (endTime-startTime)+" milliseconds");
//        }
//        
//        // view the block chain by calling toString() method
//        if (userInput.equals("3")){
//            System.out.println("View the Blockchain");
//            System.out.println(bC.toString());
//        }
//        
//        // ask the user to provide the index of block and new transaction
//        // then corrupt the chain by replacing the data
//        if (userInput.equals("4")){
//            System.out.println("Corrupt the Blockchain");
//            System.out.println("Enter block ID of block to Corrupt");
//            int corrID = Integer.parseInt(in.nextLine());
//            System.out.println("Enter new data for block "+corrID);
//            String newData = in.nextLine();
//            bC.blockList.get(corrID).setData(newData);
//            System.out.println("Block "+corrID+" now holds "+newData);         
//        }
//        
//        // call repairChain() to repair the block chain
//        if (userInput.equals("5")){
//            System.out.println("Repairing the entire chain");
//            long startTime = System.currentTimeMillis();
//            bC.repairChain();
//            long endTime = System.currentTimeMillis();
//            System.out.println("Total execution time required to repair the chain was "+ (endTime-startTime)+" milliseconds");
//            
//        }
//        
//        // exit
//        if (userInput.equals("6")){
//            flag=false;
//        }
//        }
//        
//    }
    
    /**
     * starting from the leftmost block
     * check if the block is valid
     * if not valid
     * recompute it
     */
    void repairChain(){
        for (int i = 1; i<blockList.size();i++){
            if (!blockList.get(i).getPreviousHash().equals(blockList.get(i-1).proofOfWork())){
            blockList.get(i).setPreviousHash(blockList.get(i-1).proofOfWork());
            }
        }
        
        if (!blockList.get(blockList.size()-1).proofOfWork().equals(chainHash))
            chainHash = blockList.get(blockList.size()-1).proofOfWork();
    }
    
    /**
     * get representation of each block by calling each block's toString()
     * combine them and export the blockChain's json representation
     * @return the json representation of the blockchain
     */
    @Override
    public String toString(){
        String result = "{\"ds_chain\":[";
        Iterator<Block> iter = blockList.iterator();
        while (iter.hasNext()){
        result += iter.next().toString()+",";
        }
        result=result.substring(0,result.length()-1);
        result+="],\"chainHash\":\""+this.chainHash+"\"}";
        return result;
    }
}