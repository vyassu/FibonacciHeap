import java.util.HashMap;

public class Fibbo {
	Node max;											// Variable to store Max element of the Heap
	HashMap<Integer, Node> meldNode = null;				// HashMap/Table to store node data required for pair-wise combine
	
	// Constructor to initialize the private data members of the Fibbo Heap Class
	public Fibbo()
    {
		max=null;
    }
	
	// Insert a given node in the root circular list
    void insert(Node node)
    {
    	// Condition to check whether the Heap is empty
    	if (max == null)
    	{
            max = node;
            node.qnext = node;
            node.qprev = node;
    	}
        else
        {
            Node firstnode = max;
            
            // Condition to check whether Heap contains a single element
            if(firstnode.qprev==firstnode)
            {
            	firstnode.qprev = node;
            	node.qnext = firstnode;
            }
            else
            {	
            	node.qnext = firstnode.qnext;
            	firstnode.qnext.qprev = node;
            }
            
            // Pointer to link the new node to the right of the current max element of the heap
            firstnode.qnext = node;
            node.qprev = firstnode;
            node.childcut=false;
            
            // Check whether the inserted node value is greater than the max node value
            if (max.value < node.value)
                max = node;
        }
    }
    
    // Increase the node value and check whether the new value is greater than its parent value if yes then perform childcut
    void increaseKey(Node node,long value)
    {
    	//display();
    	
    	long newvalue = node.value+value;
    	node.value = newvalue;
    	
    	// check whether the node is part of the root circular list
        if (node.parent==node)
        {
        	// Check whether the node value greater than the max of the heap if yes then change the max pointer
            if (max.value < newvalue)
                max = node;
        }
        else
        {
        	Node nodeparent = node.parent;
        	
        	// Check whether the new node value is lesser than the value of its parent
            if (nodeparent.value < newvalue)
            {
            	// Condition to check whether the parent lost a child previously
                if (node.parent.childcut == false)
                {
	    
                	//Condition to check whether the parent has more than 1 child elements
                	if (nodeparent.degree>1)
	                {	
                		// Removing the node from the circular list of the parent and decreasing the parent degree
                		Node prev = node.qprev;
	                    Node next = node.qnext;
	                    prev.qnext = next;
	                    next.qprev = prev;
	                    nodeparent.child = prev;
	                    nodeparent.degree -= 1;
	                }
	                else
	                {
	                	// remove the link to the child from its parent
	                	nodeparent.child = nodeparent;
	                	nodeparent.degree = 0;
	                }
	                node.parent = node;
	                node.value=newvalue;
	                node.qnext=node;
	                node.qprev=node;
	                insert(node);					// Invoke insert to append the node into the circular list
	                nodeparent.childcut=true;		// Change the childcut flag to true
	                //display();
                }
                else
                {
                	node = remove(node);			// Invoke remove to dissociate node from its parent
                	insert(node);					// Insert the node into the root circular list
                	cascasdecut(nodeparent);		// Invoke cascadecut as the parent had lost a child perform the current operation
                }
            }
        }
    }
    
    // Reset the max of the Heap 
    void resetMax()
    {
        Node firstnode = max;
        Node pointer = max;
        Node tempmax = max;
        boolean flag=true;
        while(pointer.qnext!=firstnode)
        {
            if (pointer.value>tempmax.value)
            	tempmax = pointer;
            pointer=pointer.qnext;
            flag=false;
        }
        if (flag==false)
        {
        	if (pointer.value > tempmax.value)
        		tempmax = pointer;
        }
        max = tempmax;
    }
    
    // Remove the max element from the Fibbonaci Heap and returning it to the callee
    Node removeMax()
    {
        Node removedNode = max;
        //System.out.println("Going to start Remove of ..."+max.keys);
        max = removedNode.qnext;
        
        //display();
        
        // Check whether the max node has child elements
        if (removedNode.degree == 0)
        {
        	// Check whether the max node is the only element in the Fibbonaci Heap
        	if (removedNode.qnext == removedNode)
                max =null;
            else
            {
            	// Remove the max element from circular list by resetting the pointers of the adjacent elements
                Node nextele = removedNode.qnext;
                Node prevele = removedNode.qprev;
                nextele.qprev = prevele;
                prevele.qnext = nextele;
                max = removedNode.qnext;
            }
        }
        else
        {
        	// Check whether the node is only element in the circular list if yes then promote its child circular list as the root circular list
            if (removedNode.qnext == removedNode)
            	max = removedNode.child;
            else
            {
            	// Pointer to the max and its adjacent element
            	Node rootprev= removedNode.qprev;
                Node rootnext= removedNode.qnext;
                // Pointer to the child element and its adjacent element
                Node firstchild = removedNode.child;
                Node lastchild = firstchild.qprev;
               
                // fit the child element into root elements
                rootprev.qnext = firstchild;
                firstchild.qprev = rootprev;
                rootnext.qprev = lastchild;
                lastchild.qnext = rootnext; 
            }  
        }
        
        // Reset all the pointers  to itself and set the degree to zero
        removedNode.degree = 0;
        removedNode.child = removedNode;
        removedNode.qnext = removedNode;
        removedNode.qprev = removedNode;
        removedNode.parent= removedNode;
        
        // Reset the max element of the Fibbonaci Heap
        resetMax();
        
        Node ptr = max;
        ptr.parent = ptr;
        
        // Iterate across all the elements of the root circular list and resetting the parent to itself and childcut to false
        while(ptr.qnext!=max)
        {
        	//System.out.println("in here");
        	ptr.parent = ptr;
        	ptr.childcut = false;
            ptr=ptr.qnext;
        }
        ptr.parent = ptr;
       
        meldNode = new HashMap<Integer,Node>();								// Initializing the Hashtable for pairwise combine
        
        //System.out.println("Starting pairwisecombine.....");
        pairwisecombine(max);												// Invoke pairwise combine
        //System.out.println("End of remove...... Max element is"+max.keys);
        
        meldNode=null;														// deleting all the contents of the Hashtable and making it ready for the next pairwise combine
        return removedNode;
    }
    
    // Inserting a node in the circular list of its parent
    Node insertNode(Node parent, Node child)
    {
    	// Check whether the parent has child elements
    	if (parent.degree>=1)
    	{
    		// Insert the node as into the circular list of the parent
    		Node firstchild = parent.child;
    		Node lastchild  = firstchild.qprev;
    		firstchild.qprev = child;
    		lastchild.qnext = child;
    		child.qprev = lastchild;
    		child.qnext = firstchild;
    		
    	}
    	else
    	{
    		// Insert the child as the first child of the parent
    		parent.child = child;
    		child.qnext = child;
    		child.qprev = child;	
    	}
    	child.parent = parent;					// Set the parent pointer of the child to the parent
    	parent.degree+=1;						// Increase the degree of the parent
    	return parent;							// Return the parent element
    }
    
    
    // Pairwise combine of all the node elements within the root circular list in a recursive manner
    void pairwisecombine(Node element)
    {
    	// Insert the node in argument into the hashtable if it is not present 
       if (meldNode.containsKey(new Integer(element.degree))==false)
    	   meldNode.put(new Integer(element.degree), element);
       
       Node ptr = max.qnext;
       
       // Loop over the entire elements of the circular list
       while(ptr.qnext!= max)
       {
    	   // Checkwhether the given element exists in the hastable and whether the degree of the current element is present in the hashtable
    	   // If there is an another element with same degree then combine the elements 
	       if (meldNode.containsKey(new Integer(ptr.degree)) && meldNode.containsValue(ptr)==false)
	       {
	    	   // Extract the element from the hashtable/map that has the same degree as the current element of the circular list
	    	   Node tempNode = meldNode.remove(new Integer(ptr.degree));
	    	   
	    	   // Check which element is bigger 
	    	   if (tempNode.value < ptr.value)
	    	   {
	    		   // detach the element from its linkages
	    			Node tempNext = tempNode.qnext;
	    			Node tempPrev = tempNode.qprev;
	    		    tempNext.qprev = tempPrev;
	    		    tempPrev.qnext = tempNext;
	    			tempNode.qnext = tempNode;
	    			tempNode.qprev = tempNode;
	    			
	    			// Insert the element obtained from the hashtable as the child element of the current circular list element
	    			tempNode = insertNode(ptr, tempNode);
	    		}
	    		else
	    	    {
	    			// detach the element from its linkages
	    			 Node ptrnext = ptr.qnext;
	    			 Node ptrprev = ptr.qprev;
	    			 ptrnext.qprev = ptrprev;
	    		     ptrprev.qnext = ptrnext;
	    			 ptr.qnext = ptr;
	    			 ptr.qprev = ptr;
	    			 
	    			 // Insert the current element as the child element of the element obtained from the hashtable
	    			 tempNode = insertNode(tempNode,ptr);	   
	    		} 
	    	    pairwisecombine(tempNode);			// Invoke pairwise combine with the new combined element
	    	    break;
	    	}
	    	else
	    	{
	    		// Check whether the element is present in the hashtable
	    		if (meldNode.containsValue(ptr)==false)
	    		  meldNode.put(new Integer(ptr.degree), ptr);
	    		ptr = ptr.qnext;
	    	}
       }
       return;
    }
    
    // Remove an element from its parent element used in cascade cut
    Node remove(Node node)
    {
    	Node parent  = node.parent;
    	
    	//  Check whether the node is at the root
    	if (node.parent==node)
    		return node;
    	
    	// Check whether parent element has multiple child elements
    	if (parent.degree >1)
    	{
    		// Detach the child element from its siblings
    		Node prev = node.qprev;
    		Node next = node.qnext;
    		prev.qnext = next;
    		next.qprev = prev;
    		parent.child = prev;				// Setting the child element of the parent as next element of the current child element
    	}
    	else
    		parent.child = parent;				// Setting the parent as its own child element all the child elements have been removed
    	
    	// Decrease the degree and set the childcut of the parent to TRUE
    	parent.degree --;					
    	parent.childcut = true;
    	
    	// Remove all the links from the child element
    	node.qprev = node;
		node.qnext = node;
		node.parent = node;
		
		// Reset the max element element of the circular list
		resetMax();
		
    	return node;							// Return the child element
    }
    
    // Check whether a node has lost an element before if yes then insert the node into the root circular list
    void cascasdecut(Node node)
    {
    	Node removedNodeParent = node.parent;
    	// Check whether the node is at the root if yes set the childcut as false
    	if (removedNodeParent != node)
    	{
    		Node newnode = remove(node);		// Remove the node from its parent
			insert(newnode);					// Insert the child into the root circular list
		
		//  Check whether the parent has childcut is TRUE
    	if(removedNodeParent.childcut!=false)
    		cascasdecut(removedNodeParent);
    	}
    	else
    		node.childcut = false;				// Reset childcut of the node as FALSE since it is in circular list
    	return;
    }
    
    
    // Function to display the elements of the root circular list
    void display()
    {
        Node firstnode = max;
        Node nodepointer = max;
        int counter=0;
        while(nodepointer.qnext!=max)
        {
            System.out.print("["+nodepointer.keys+" "+nodepointer.value+" "+nodepointer.degree+" "+nodepointer.parent.keys+"] ");
            nodepointer = nodepointer.qnext;
        }
        System.out.println("["+nodepointer.keys+" "+nodepointer.value+" "+nodepointer.degree+" "+nodepointer.parent.keys+"]");
    }

}
