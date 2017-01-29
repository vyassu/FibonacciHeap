import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class hashtagcounter {
	static Fibbo fib;						// Instantiating the Fibbo Heap Class Implementation
	
	// Driver class to read the input from the computer and perform various functions
    public static void main(String args[])
    {
    	String filepath = args[0];                                              // Extracting the input filepath from Command Line
    	String outfilepath = "./output_file.txt";							   // Setting the output filepath and filename 
    	fib = new Fibbo();													   // Creating instance of Fibbo class
    	
        HashMap<String, Node>nodeList= new HashMap<String,Node>();	           // HashMap to store hashtag and the location of the node
        
        // Creating file Input and Output instance
        File file = new File(filepath);
        File outfile  = new File(outfilepath);
        
        // Creating Buffered Writer and Reader for write and read respectively
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            writer = new BufferedWriter(new FileWriter(outfile));
            
            String line = null;												// Variable to store every line of the input file
            
            // Loop to iterate over the every line
            while ((line = reader.readLine()) != null) 
            {
    		    String [] datalist = line.split(" ");						// Split the line into hashtag and no of tweet
    		    //System.out.println(line);
    		    Node node;											
    		    // Condition to check whether line contains hashtag
    		    if (datalist[0].contains("#")==true)
    		    {
    		    	String data = datalist[0].replace("#","");				// Remove # from the hashtag
    		    	// Condition to check whether the hashtag is present in the HashMap
    		        if (nodeList.containsKey(data)==false)
    		        {
    		        	node = new Node(data, new Long(datalist[1]));		// Creating the node with the hashtag and # tweets	
    		            fib.insert(node);									// 	Inserting the node into Fibbo
    		            nodeList.put(data,node);
    		        }
    		        else
    		            fib.increaseKey(nodeList.get(data),new Long(datalist[1]));	// Increase the node's value
    		   }
    		    // If the line contains only stop .. Stop the Program
    		   else if (datalist[0].equalsIgnoreCase("stop"))
    		   {
    		       //System.out.println("PROGRAM STOPPING!!");
    		       if (writer!=null)
               		writer.close();
                   if (reader != null) {
                       reader.close();
                   }
    		       System.exit(0);
    		   }
    		   // Below code removes the max element from the Fibbonacci Heap and writes it to a file
    		   else
    		   {
    		       int noOfRemove=Integer.parseInt(datalist[0]);
    		       ArrayList <Node>removedele = new ArrayList<Node>();
    		       String output="";
    		       
    		       // Loop to remove max element from the Fibonacci Heap and save it in a string
    		       while(noOfRemove>0)
    		       {
    		             Node removNode = fib.removeMax();
    		             removedele.add(removNode);
    		             if (noOfRemove==1)
    		            	 output+=removNode.keys;
    		             else
    		            	 output+=removNode.keys+",";
    		             nodeList.remove(removNode.keys);
    		             noOfRemove-=1;
    		       }
    		       output+="\n";
    		       writer.write(output);								// Write the string into Output file.
    		       //System.out.println("final output :"+output);
    		       
    		       // Loop to insert the node back into the Fibbonacci Heap
    		       for (Node remnode : removedele)
    		       {
    		    	   fib.insert(remnode);
    		           nodeList.put(remnode.keys,remnode);
    		       }
    		   }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally {
        	// Code to close all file handle
            try {
            	if (writer!=null)
            		writer.close();
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
