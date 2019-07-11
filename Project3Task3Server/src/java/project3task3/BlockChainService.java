/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task3;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * This is the server side code for project 3 task 3
 * @author Zhexin Chen (zhexinc)
 * @date Mar 19 2019
 */

// define the URL pattern
@WebServlet(name = "BlockChainService", urlPatterns = {"/BlockChainService/*"})
public class BlockChainService extends HttpServlet{
    // use member variables and constructor to initialize the blockchain
    BlockChain bC = new BlockChain();
    Block genesisBlock = new Block(0,new Timestamp(System.currentTimeMillis()),"Genesis",2);
    
    public BlockChainService(){
    genesisBlock.setPreviousHash("");
    // add the block to the block chain
    bC.addBlock(genesisBlock);
    bC.chainHash = genesisBlock.proofOfWork();
    }
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Console: doGET visited");

        String result = "";
        
        // The type is on the path /name so skip over the '/'
        String type = (request.getPathInfo()).substring(1);
        
        // if the task is to verify the blockchain
        // verify it by calling isChainValid()
        // return the result and exec time
        if(type.equals("verify")) {
            response.setStatus(200);
            long startTime = System.currentTimeMillis();
            boolean isValid = bC.isChainValid();
            long endTime = System.currentTimeMillis();
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println(""+(endTime-startTime)+","+isValid);     
        }
        
        // if the task is to view the blockchain
        // just return its JSON representation
        // by calling toString()
        else if (type.equals("view")){      
        // Things went well so set the HTTP response code to 200 OK
        response.setStatus(200);
        // tell the client the type of the response
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(bC.toString());      
        }
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Console: doPost visited");
        
        // Read what the client has placed in the POST data area
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String data = br.readLine();
        
        // extract new transaction and difficulty needed for creating the new block
        String[] requests = data.split(",");
        
        long startTime = System.currentTimeMillis();
        Block newBlock = new Block(bC.getChainSize(),new Timestamp(System.currentTimeMillis()),requests[0],Integer.parseInt(requests[1]));
        newBlock.setPreviousHash(bC.chainHash);
        bC.chainHash=newBlock.proofOfWork();
        bC.blockList.add(newBlock);
        long endTime = System.currentTimeMillis();
        String result =  ("Total execution time to add this block was " + (endTime-startTime) + " milliseconds");

        //send back the exec time
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result); 
        response.setStatus(200);       
    }  
    
}
