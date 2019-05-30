import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;



/**
 * Filename:   BPTree.java
 * Project:    Food Query and Meal Analysis
 * Course:     cs400 
 * Authors:    Dominic Bourget, David Waltz, Austin Muschott, Megan Stoffel, Matthew McJoynt
 * Due Date:   Wed. Dec 12 10 pm
 * 
 *
 *
/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;

    /**
     * Public constructor
     * 
     * @param branchingFactor 
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }

        this.branchingFactor= branchingFactor;
        this.root= new LeafNode();
        
    }
 
    /*
     * (non-Javadoc)
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {

    	if(key == null)
    	{
    		throw new IllegalArgumentException("No null keys allowed");
    	}
    	root.insert(key, value);// call insert on instance of root
    }
    
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        else if(key == null)
        {
        	return new ArrayList<V>();
        }
        List<V> values= root.rangeSearch(key, comparator);
        return values;
        
    }
    
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;

        
        /**
         * Package constructor
         */
        Node() {
           
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String toString() {
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
   
        
        /**
         * Package constructor
         */
        InternalNode() {

            this.keys = new ArrayList<K>();
			this.children = new ArrayList<Node>();

        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {

        	return children.get(0).getFirstLeafKey();//recursively calls until child is a leaf node

        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {

            int child = children.size();
            if(child > branchingFactor)
            {
            	return true;
            }
            else
            	return false;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */	
        void insert(K key, V value) {
        	Node n=null;
			for(int i=0; i<keys.size(); i++)
        	{

        		if(key.compareTo(keys.get(i))<0)//getting placement of where key would be inserted
        		{
        			n=children.get(i); //getting child at spot before key
        			break;
        		}
        	}
        	if(n== null)//key value is bigger than of childs keys
        	{
        		n=children.get(keys.size());
        		if(keys.size()>=2)
        		{
        			if(key.compareTo(keys.get(keys.size()-2))==0)
        			{
        				n=children.get(keys.size()-1);
        			}
        		}
        	}
        
        	n.insert(key, value);//calling insert on child, either goes back to internal or leaf node insert
        	if(n.isOverflow()) //checking to see if we need to split the child of the current node
        	{	
        		Node split= n.split();
        		insertHelper(split.getFirstLeafKey(), split);
        	}
        	if(root.isOverflow()) //once the recursion pops back up check if root overfilled
        	{
        		Node split= split();
        		InternalNode root1= new InternalNode();
        		root1.keys.add(split.getFirstLeafKey());//adding smallest leaf of right subtree into root
        		root1.children.add(this); //adding the new roots split root and created split root
        		root1.children.add(split);
        		root= root1;		
        	}
        }
        //method used to add new child node to internal node
        //adds smallest key from splits sub tree to keys
        void insertHelper(K key, Node child)
        {
        
        	boolean checker=true;
        	int location1= 0;
        	for(int i=0; i<keys.size(); i++)
        	{

        		if(key.compareTo(keys.get(i))<0)//getting placement of where key would be inserted
        		{
        			checker=false;
        			location1= i;//location of where key should go as soon as key is smaller
        			break;
        		}
        	}
        	if(checker== true)//key value is bigger than of childs keys
        	{
        		location1=keys.size();
        		if(keys.size()>=2)
        		{
        			if(key.compareTo(keys.get(keys.size()-2))==0)
        			{
        				location1= keys.size()-1;
        			}
        		}
        	}
        
        	keys.add(location1, key);
        	children.add(location1 + 1, child);//offset by 1 for children of keys, adding new split child
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        
        Node split() {
        
        	int start= keys.size()/2 + 1;
        	
        	InternalNode split= new InternalNode();// new internal node
        	for(int i=start; i<keys.size(); i++ )
        	{	
        		split.keys.add(keys.get(i)); //adding half of the keys to split
        	}

        	for(int i=start; i<keys.size() +1; i++)//inserting half of the children into split
        	{
        		split.children.add(children.get(i));

        	}
        
        	int tracker= keys.size()- start +1;
        	for(int i= 0; i< tracker; i++)//removing old keys and children from original that are in split node
        	{
        		keys.remove(start -1);
        		children.remove(start);
        		
        	}
        	
        	return split;

        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
        	Node n=null;
			if(comparator.equals("==") || comparator.equals(">="))
			{
				for(int i=0; i<keys.size(); i++)
				{
					if(i<keys.size()-1) 
					{
						if(key.compareTo(keys.get(i+1))<=0 && key.compareTo(keys.get(i))>=0)//corner case where key is in in two nodes
						{
							n=children.get(i);
							break;
						}
					}
					else if(i>=1) //getting previous node if key split off into previous node
					{
						n=children.get(i-1);
						break;
					}
					else //key did not split into two leaf nodes
					{
						n=children.get(i);
					}
				}	
			}

			if(comparator.equals("<=")) 
			{
				n=children.get(0);//getting the most left leaf node
			}

        	List<V> values= n.rangeSearch(key, comparator);//calls itself until gets to leaf class
            return values;

        }
        
    
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {
        
        // List of values
        List<V> values;
        
        // Reference to the next leaf node
        LeafNode next;
        // Reference to the previous leaf node
        LeafNode previous;
        
        /**
         * Package constructor
         */
        LeafNode() {
         
            this.values = new ArrayList<V>();
            keys = new ArrayList<K>();
            this.next=null;
            this.previous=null;
         
			
        }
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
  
        	return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {//check if leaf node is overflowed
           if(values.size() > branchingFactor-1)//more values than branching factor allows
           {
        	   return true;
           }
           else
        	   return false;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
        	
        	boolean checker=true;
        	int location1= 0;
        	for(int i=0; i<keys.size(); i++)
        	{

        		if(key.compareTo(keys.get(i))<0)//getting placement of where key would be inserted
        		{
        			checker=false;
        			location1= i;//location of where key should go as soon as key is smaller
        			break;
        		}
        	}
        	if(checker== true)//key value is bigger than of childs keys
        	{
        		location1=keys.size();
       
        	}
     
        	keys.add(location1, key);
        	values.add(location1, value);

        	if(root.isOverflow())//stop using after internal nodes are created
        	{
            		Node split= split();//splitting the root into two 
            		InternalNode root1= new InternalNode();//root is now an internal node
            		root1.keys.add(split.getFirstLeafKey());//adding the middle value
            		root1.children.add(this);//adding all the children to the code
            		root1.children.add(split);
            		root= root1;
        	}
           
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            LeafNode split= new LeafNode();
            int start= keys.size() +1; //indexing of place to split
            start=start/2;
            for(int i=start; i<keys.size(); i++)
            {
            	split.keys.add(keys.get(i));//adding half of values to split leaf
            	split.values.add(values.get(i));
            }
            int tracker= keys.size()- start;
            for(int i=0; i< tracker; i++)
            {
            	keys.remove(start); // removing values from the originalnode
            	values.remove(start);
            }

            split.next=next;
            split.previous= this;
            this.next=split;
            
            return split;
            	
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            
          
          List<V> range= new ArrayList<V>();
           boolean checker=false;//var used to iterate till next is null or wrong value hit
           LeafNode n=this;//setting temp variable used to iterate to next leaf node
           if(comparator.equals(">="))
           {
        	  while(checker==false)  
        	  {
        	
        		  for(int i=0; i< n.keys.size(); i++)  
        		 {
        			
        			  if(n.keys.get(i).compareTo(key)>=0)//checking to see if value is greater than key
        			 {
        				 range.add(n.values.get(i));
        			 }
        			 
        		 }
        		 if(n.next!= null)//so null crash does not occur
        		 {
        		  n=n.next;
        		 }
        		 else
        			 checker=true;
        	  }
           }
           else if(comparator.equals("<="))
           {
        	  while(checker==false)  
        	  {
        		 for(int i=0; i< n.values.size(); i++)  
        		 {
        			 if(n.keys.get(i).compareTo(key)<=0) //if value is less than or equal
        			 {
        				 range.add(n.values.get(i));
        			 }
        			 else
        			 {
        				 break;
        			 }
        		 }
        		 if(n.next!= null)//getting next leaf node
        		 {
        		  n=n.next;
        		 }
        		 else
        			 checker=true;
        	  }
           }
           else if(comparator.equals("=="))
           {
         	  while(checker==false)  
         	  {
         	
         		  for(int i=0; i< n.keys.size(); i++)  
         		 {
         			
         			  if(n.keys.get(i).compareTo(key)==0)//if value is equal
         			  {
         				 range.add(n.values.get(i));
         			  }
         			  else if(n.keys.get(i).compareTo(key)>=0)//if value in leaf node is greater break from loop
         			  {
         				  checker=true;
         				  break;
         						  
         			  }
         			 
         		 }
         		 if(n.next!= null)
         		 {
         		  n=n.next;
         		 }
         		 else
         			 checker=true;
         	  }
            }
       
           return range;
   
           
        }
        
    } // End of class LeafNode

    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(5);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d};

        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Double j = dd[rnd1.nextInt(9)];
            list.add(j);
   
            bpTree.insert(j, j);

            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        List<Double> filteredValues = bpTree.rangeSearch(null, "==");
        System.out.println("Filtered values: " + filteredValues.toString());
       
     
       
       
    }

} 
