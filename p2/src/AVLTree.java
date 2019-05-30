/**
 * Filename:   AVLTree.java
 * Project:    p2
 * Authors:    Debra Deppeler, Matthew McJoynt
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:    001
 * 
 * Due Date:   Before 10pm on September 24, 2018
 * Version:    1.0
 * 
 * Credits:    none
 * 
 * Bugs:       no known bugs, but not complete either
 */

import java.lang.IllegalArgumentException;

/** 
 * @param <K> extends Comparable so that the nodes in the tree can be compared
 */
public class AVLTree<K extends Comparable<K>> implements AVLTreeADT<K> {
	private BSTNode<K> head;
	
	public AVLTree(BSTNode<K> node) {
		this.head = node;
	}
	
	public BSTNode<K> getHead() {return head;}
	
	/** TODO: add class header comments here
	 * Represents a tree node.
	 * @param <K>
	 */
	class BSTNode<K> {
		/* fields */
		private K key;	// comparable key used to determine priority in the tree
		private int height;	// the level of the node in the tree
		private BSTNode<K> left, right;	// pointer to the left and right child, null if there is none
		
		/**
		 * Constructor for a BST node.
		 * @param key
		 */
		BSTNode(K key) {
			this.key = key;
			height = 1;
			left = null;
			right = null;
		}

		/* accessors */
		public BSTNode<K> getLeftNode(){return left;}
		public BSTNode<K> getRightNode(){return right;}
		public int getHeight(){return height;}
		public K getKey(){return key;}

		/* mutators */
		public void setLeftNode(K key) {left = new BSTNode<K>(key);}
		public void setRightNode(K key) {right = new BSTNode<K>(key);}
		public void setLeftNode(BSTNode<K> left) {this.left = left;}
		public void setRightNode(BSTNode<K> right) {this.right = right;}
		public void setHeight(int height) {this.height = height;}
		public void setKey(K key) {this.key = key;}
	}
	
	public static void main(String[] args) throws IllegalArgumentException, DuplicateKeyException {
		AVLTree<Integer> tree = new AVLTree<>(null);
		tree.insert(1);
		System.out.println(tree.print());
	}
	
	/**
	 * @return true if the tree is empty, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * @param K key is the key to the node that is being inserted
	 * @throws DuplicateKeyException if a node with the same key is already in the tree
	 * @throws IllegalArgumentException if K is not a valid key
	 */
	@Override
	public void insert(K key) throws DuplicateKeyException, IllegalArgumentException {
		BSTNode<K> node = new BSTNode<K>(key);
		
		if(head == null) {
			head = node;
			return;
		}
		
		BSTNode<K> left = head.getLeftNode();
		BSTNode<K> right = head.getRightNode();
		
		int comp = head.getKey().compareTo(node.getKey());
		
		if(comp == 0)
			throw new DuplicateKeyException();
		else if(comp > 0) { //should be inserted to the left
			if(left == null) {
				head.setLeftNode(node);
				rebalance();
				return;
			}
			
			AVLTree<K> newTree = new AVLTree<>(left);
			newTree.insert(key);
		} else if(comp < 0) { //should be inserted to the right
			if(right == null) {
				head.setRightNode(node);
				rebalance();
				return;
			}
			
			AVLTree<K> newTree = new AVLTree<>(right);
			newTree.insert(key);
		}
	}
	
	public void rebalance() {
		
	}
	
	public void easyInsert(K key) throws DuplicateKeyException, IllegalArgumentException {
		BSTNode<K> node = new BSTNode<K>(key);
		
		if(head == null)
			head = node;
		else if(head.getKey().compareTo(node.getKey()) == 0)
			throw new DuplicateKeyException();
		else if(head.getKey().compareTo(node.getKey()) > 0)
			head.setLeftNode(node);
		else if(head.getKey().compareTo(node.getKey()) < 0)
			head.setRightNode(node);
	}
	
	private BSTNode<Integer> leftRotate(BSTNode<Integer> n, K key){
		BSTNode<Integer> g = n;
		BSTNode<Integer> p = g.getRightNode();
//		BSTNode<Integer> k = p.getRightNode();
		
		BSTNode<Integer> temp = p.getLeftNode();
		
		p.setLeftNode(g);
		g.setRightNode(temp);
		
		return p;
	}
	
	private BSTNode<Integer> rightRotate(BSTNode<Integer> n, K key){
		BSTNode<Integer> g = n;
		BSTNode<Integer> p = g.getLeftNode();
//		BSTNode<Integer> k = p.getLeftNode();
		
		BSTNode<Integer> temp = p.getRightNode();
		
		p.setRightNode(g);
		g.setLeftNode(temp);
		
		return p;
	}

	/**
	 * @param K key is the key to the node that should be deleted
	 * @throws IllegalArgumentException if K is not a valid key
	 */
	@Override
	public void delete(K key) throws IllegalArgumentException {
		if(key == null)
			throw new IllegalArgumentException();
		if(!search(key))
			return;
		
		BSTNode<K> left = head.getLeftNode();
		BSTNode<K> right = head.getRightNode();
		int comp = head.getKey().compareTo(key);
		
		if(comp == 0) {
			head = right;
			if(head.getLeftNode() == null)
				head.setLeftNode(left);
			else {
				BSTNode<K> cur = head.getLeftNode();
				
				while(cur.getLeftNode() != null)
					cur = cur.getLeftNode();
				
				cur.setLeftNode(left);
			}
		} else if(comp > 0) { // insert on left
			AVLTree<K> newTree = new AVLTree<>(left);
			newTree.delete(key);
		} else if(comp < 0) {
			AVLTree<K> newTree = new AVLTree<>(right);
			newTree.delete(key);
		}
	}

	/**
	 * @return true if key is in the tree, false and throws IllegalArgumentException otherwise
	 */
	@Override
	public boolean search(K key) throws IllegalArgumentException {
		try {
			if(head == null)
				return false;
			
			int comparison = key.compareTo(head.getKey());
		
			if(comparison == 0)
				return true;
			else if(comparison > 0) {
				if(head.getRightNode() != null) {
					AVLTree<K> right = new AVLTree<K>(head.getRightNode());
					return right.search(key);
				} else
					throw new IllegalArgumentException();
			}
			else if(comparison < 0) {
				if(head.getLeftNode() != null) {
					AVLTree<K> left = new AVLTree<K>(head.getLeftNode());
					return left.search(key);	
				} else
					throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			return false;
		}
		return false;
	}

	/**
	 * @return the AVL tree in-order traversal without any spaces or commas separating the elements
	 */
	@Override
	public String print() {
		String result = "";
		
		BSTNode<K> right = head.getRightNode();
		BSTNode<K> left = head.getLeftNode();
		
		if(left != null) {
			AVLTree<K> newTree = new AVLTree<>(left);
			
			result += newTree.print();	
		}
		result += head.getKey() + " ";
		if(right != null) {
			AVLTree<K> newTree = new AVLTree<>(right);
			
			result += newTree.print();
		}
		
		return result;
	}

	/**
	 * @return true if balanced tree, false if not
	 */
	@Override
	public boolean checkForBalancedTree() {
		BSTNode<K> right = head.getRightNode();
		BSTNode<K> left = head.getLeftNode();
		
		if(left == null && right == null || (left == null && right.getHeight() == 1) || (right == null && left.getHeight() == 1))
			return true;
		else if((left == null && right.getHeight() > 1) || (right == null && left.getHeight() > 1))
			return false;
		return checkBalancedHelper(right) && checkBalancedHelper(left);
	}
	
	private boolean checkBalancedHelper(BSTNode<K> node) {
		BSTNode<K> right = node.getRightNode();
		BSTNode<K> left = node.getLeftNode();
		
		if(left == null && right == null || (left == null && right.getHeight() == 1) || (right == null && left.getHeight() == 1))
			return true;
		else if((left == null && right.getHeight() > 1) || (right == null && left.getHeight() > 1))
			return false;
		return checkBalancedHelper(right) && checkBalancedHelper(left);
	}

	/**
	 * @return true if binary search tree, false if not
	 */
	@Override
	public boolean checkForBinarySearchTree() {
		BSTNode<K> right = head.getRightNode();
		BSTNode<K> left = head.getLeftNode();
		
		if(head == null || (left == null && right == null))
			return true;
		if(left == null && right != null) {
			if(head.getKey().compareTo(right.getKey()) >= 0)
				return false;
			AVLTree<K> rightTree = new AVLTree<>(right);
			return rightTree.checkForBinarySearchTree();
		} else if(left != null && right == null) {
			if(head.getKey().compareTo(left.getKey()) <= 0)
				return false;
			AVLTree<K> leftTree = new AVLTree<>(left);
			return leftTree.checkForBinarySearchTree();
		}
		else if(head.getKey().compareTo(left.getKey()) <= 0 || head.getKey().compareTo(right.getKey()) >= 0)
			return false;
		AVLTree<K> leftTree = new AVLTree<>(left);
		AVLTree<K> rightTree = new AVLTree<>(right);
		
		return leftTree.checkForBinarySearchTree() && rightTree.checkForBinarySearchTree();
	}

}








