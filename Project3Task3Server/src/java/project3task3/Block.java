/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task3;


import java.sql.Timestamp;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.gson.*;

/**
 * This class represents a simple Block.
 * 
 * Each Block object has an index - 
 * the position of the block on the chain. 
 * The first block (the so called Genesis block) has an index of 0.
 * 
 * Each block has a timestamp - 
 * a Java Timestamp object, it holds the time of the block's creation.
 * 
 * Each block has a field named data - 
 * a String holding the block's single transaction details.
 * 
 * Each block has a String field named previousHash - 
 * the SHA256 hash of a block's parent. This is also called a hash pointer.
 * 
 * Each block holds a nonce - 
 * a BigInteger value determined by a proof of work routine. 
 * 
 * Each block has a field named difficulty - 
 * it is an int that specifies the exact number of 
 * left most hex digits needed by a proper hash. 
 * 
 * @author  Zhexin Chen (zhexinc)
 * @date    Mar 18 2019
 */
public class Block extends java.lang.Object{
    public int index;
    public java.sql.Timestamp timestamp;
    public java.lang.String data;
    public java.lang.String previousHash;
    public BigInteger nonce;
    public int difficulty;
    
    /**
     * Constructor of this class
     * @param index
     * @param timestamp
     * @param data
     * @param difficulty 
     */
    Block(int index, java.sql.Timestamp timestamp, java.lang.String data, int difficulty){
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
    }
    
    /**
     * combine the index, timestamp, data, previousHash, nonce and difficulty
     * to one string pending hash
     * then hash it using sha256
     * then convert to a hex string
     * @return the calculated hex string
     */
    String calculateHash(){
    byte[] bytesOfText = (index + timestamp.toString() + data + previousHash + nonce.toString() + difficulty).getBytes();
    byte[] theDigest = null;
    String hexString = null;
    try{
    MessageDigest md_sha256 = MessageDigest.getInstance("SHA-256");
    theDigest = md_sha256.digest(bytesOfText);
    hexString = javax.xml.bind.DatatypeConverter.printHexBinary(theDigest);
    }
    catch (NoSuchAlgorithmException e){
    e.printStackTrace();}
    return hexString;
    }
    
    /**
     * getter of data
     * @return data
     */
    String getData(){
    return data;
    }
    
    /**
     * getter of difficulty
     * @return difficulty
     */
    int getDifficulty(){
    return difficulty;
    }
    
    /**
     * getter of index
     * @return index
     */
    int getIndex(){
    return index;
    }
    
    /**
     * getter of nonce
     * @return nonce
     */
    BigInteger getNonce(){
    return nonce;
    }
    
    /**
     * getter of previous hash
     * @return previous hash
     */
    String getPreviousHash(){
    return previousHash;
    }
    
    /**
     * getter of timestamp
     * @return timestamp
     */
    Timestamp getTimestamp(){
    return timestamp;
    }
    
    /**
     * empty main method
     * @param args not used
     */
    public static void main(String[] args){
    }
    
    /**
     * The proof of work methods finds a good hash. 
     * It increments the nonce until it produces a good hash.
     * @return the good hash
     */
    String proofOfWork(){
    boolean flag = true;
    String goodHash = null;
    // initialize the nonce to be 0
    nonce = new BigInteger("0");
    // keep calculating hash until we find a good hash
    while (flag){
    goodHash = calculateHash();
    flag=false;
    for (int i =0; i<difficulty; i++){
    if (goodHash.charAt(i)!='0'){
        flag = true;
        // increment the nonce by 1
        nonce = nonce.add(new BigInteger("1"));
        break;
            }
        }
    }
    return goodHash;
    }
    
    /**
     * setter of data
     * @param data 
     */
    void setData(String data){
        this.data = data;
    }
    
    /**
     * setter of difficulty
     * @param difficulty 
     */
    void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }
    
    /**
     * setter of index
     * @param index 
     */
    void setIndex(int index){
    this.index = index;
    }
    
    /**
     * setter of previousHash
     * @param previousHash 
     */
    void setPreviousHash(String previousHash){
        this.previousHash = previousHash;
    }
    
    /**
     * setter of time stamp
     * @param timestamp 
     */
    void setTimestamp(Timestamp timestamp){
    this.timestamp = timestamp;
    }
    
    /**
     * using Gson to return the json representation of this block
     * @return String containing the json representation of this block
     */
    @Override
    public String toString(){
    Gson g = new Gson();
    return g.toJson(this);
    }
    
}