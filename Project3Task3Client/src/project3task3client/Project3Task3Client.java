/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task3client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



/**
 * Simple result class to store the responses
 * @author Zhexin Chen (zhexinc)
 */
class Result {
    String value;

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}

/**
 * This is the client code for project 3 task 3
 * @author Zhexin Chen
 * @data   Mar 18 2019
 */
public class Project3Task3Client{

    /**
     * @param args the command line arguments - not used
     */
    public static void main(String[] args)  throws Exception {
        // keep providing the menu
        System.out.println("Run:");
        System.out.println("Block Chain Menu");
        boolean flag = true;
         while (flag){
        System.out.println("1. Add a transaction to the blockchain.");
        System.out.println("2. Verify the blockchain.");
        System.out.println("3. View the blockchain.");
        System.out.println("4. Exit.");
        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();
        
        // if user enters 1, ask user the new transaction and difficulty
        // and then call addTransaction() method
        // print exec time
        if (userInput.equals("1")){
            System.out.println("Enter difficulty > 0");
            String diff = in.nextLine();
            System.out.println("Enter transaction");
            String userData = in.nextLine();
            System.out.println(addTransaction(userData, diff));
        }
        
        // if user enters 2
        // let the server verify the block chain by calling verifyBlockChain()
        // print the result and exec time
        if (userInput.equals("2")){
        String[] result = verifyBlockChain().split(",");
        System.out.println("Chain verification: "+result[1]);
        System.out.println("Total execution time required to verify the chain was "+ result[0] +" milliseconds");
        }
         
        // if user enters 3
        // let the server return the block chain by calling viewBlockChain()
        // print JSON representation
        if (userInput.equals("3")){
        System.out.println("View the Blockchain");
        System.out.println(viewBlockChain());
        }
          
        // if user enters 4
        // exit
        if (userInput.equals("4")){
        flag = false;
        }
        }
    }
    
    /**
     * request the server to verify the blockchain
     * by calling doGet()
     * @return the verify result and exec time
     */
    public static String verifyBlockChain(){
        Result r = new Result();
        int status = doGet("verify",r);
        return r.getValue();
    }
    
    /**
     * request the server to send back the JSON representation of the blockchain
     * by calling doGet()
     * @return JSON representation of the blockchain
     */
    public static String viewBlockChain(){
        Result r = new Result();
        int status = doGet("view",r);
        return r.getValue();
    }
    
    /**
     * request the server to add a new block by calling doPost()
     * @return the exec time
     */
    public static String addTransaction(String transaction, String difficulty){
        long startTime = System.currentTimeMillis();
        int status = doPost(transaction,difficulty);
        long endTime = System.currentTimeMillis();
        return "Total execution time to add this block was " + (endTime-startTime) + " milliseconds";
    }
    
    public static int doGet(String type,Result r){
    // Make an HTTP GET passing the name on the URL line
         
         r.setValue("");
         String response = "";
         HttpURLConnection conn;
         int status = 0;
         
                  try {  
                
                // pass the name on the URL line
		URL url = new URL("http://localhost:8080/Project3Task3Server/BlockChainService" + "//"+type);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
                // tell the server what format we want back
		conn.setRequestProperty("Accept", "text/plain");
 	
                // wait for response
                status = conn.getResponseCode();
                
                // If things went poorly, don't try to read any response, just return.
		if (status != 200) {
                    // not using msg
                    String msg = conn.getResponseMessage();
                    return conn.getResponseCode();
                }
                String output = "";
                // things went well so let's read the response
                BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));
 		
		while ((output = br.readLine()) != null) {
			response += output;
         
		}
 
		conn.disconnect();
 
	    } 
                catch (MalformedURLException e) {
		   e.printStackTrace();
	    }   catch (IOException e) {
		   e.printStackTrace();
	    }
            
         // return value from server 
         // set the response object
         r.setValue(response);
         // return HTTP status to caller
         return status;
    }
    
    public static int doPost(String transaction, String difficulty) {
          
        int status = 0;
        String output;
        
        try {  
                // Make call to a particular URL
		URL url = new URL("http://localhost:8080/Project3Task3Server/BlockChainService/");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
                // set request method to POST and send name value pair
                conn.setRequestMethod("POST");
		conn.setDoOutput(true);
                // write to POST data area
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(transaction+","+difficulty);
                out.close();
                
                // get HTTP response code sent by server
                status = conn.getResponseCode();
                
                //close the connection
		conn.disconnect();
	    } 
            // handle exceptions
            catch (MalformedURLException e) {
		      e.printStackTrace();        
            } 
            catch (IOException e) {
		      e.printStackTrace();
	    }
       
            // return HTTP status
         
            return status;
    }
    
}
