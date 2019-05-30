/**
 * Filename:   TestAVLTree.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.lang.IllegalArgumentException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** TODO: add class header comments here*/
public class TestAVLTree {	
	private static int failed;
	private static int passed;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Testing AVLTree.java");
		failed = 0;
		passed = 0;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Testing of AVLTree.java completed, passed " + passed + " tests, failed " + failed + " tests.");
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * Tests that an AVLTree is empty upon initialization.
	 */
	@Test
	public void test01isEmpty() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		try {
			assertTrue(tree.isEmpty());
		} catch (AssertionError e) {
			System.out.println("test01isEmpty failed, empty tree did not return true for isEmpty().");
			failed++;
			return;
		}
		passed++;
		System.out.println("test01 passed");
	}

	/**
	 * Tests that an AVLTree is not empty after adding a node.
	 */
	@Test
	public void test02isNotEmpty() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		try {
			tree.insert(1);
			assertFalse(tree.isEmpty());
		} catch (AssertionError e) {
			System.out.println("test02isNotEmpty failed, non-empty tree returned true for isEmpty()");
			failed++;
			return;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
		passed++;
		System.out.println("test02 passed");
	}
	
	/**
	 * Tests functionality of a single delete following several inserts.
	 */
	@Test
	public void test03insertManyDeleteOne() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		try {
			tree.insert(2);
			tree.insert(1);
			tree.insert(3);
			
			tree.delete(3);
			
			assertTrue(tree.print().equals("1 2 ") || tree.print().equals("1 2"));
		} catch (AssertionError e) {
			System.out.println("test03insertManyDeleteOne failed, tree inserted 3 items and deleted one incorrectly");
			failed++;
			return;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
		
		passed++;
		System.out.println("test03 passed");
	}
	
	/**
	 * Tests functionality of many deletes following several inserts.
	 */
	@Test
	public void test04insertManyDeleteMany() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		try {
			tree.insert(2);
			tree.insert(1);
			tree.insert(3);
			
			tree.delete(3);
			tree.delete(1);
			
			assertTrue(tree.print().equals("2 ") || tree.print().equals("2"));
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		} catch (AssertionError e) {
			System.out.println("test04insertManyDeleteMany failed, did not contain proper nodes after multiple insertions and deletions");
			failed++;
		}
		
		passed++;
		System.out.println("test04 passed");
	}
	@Test
	public void test05searchValueNotInAVL() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		try {
			tree.insert(1);
			
			tree.search(4);
		} catch (IllegalArgumentException e) {
			passed++;
			System.out.println("test05 passed");
			return;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("test05searchValueNotInAVL failed, searched for a key not in the tree and did not throw IllegalArgumentException as expected");
		failed++;
	}
	@Test
	public void test06searchValueRemovedFromAVL() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		try {
			tree.insert(1);
			tree.insert(2);
			
			tree.delete(2);
			
			tree.search(2);
		} catch (IllegalArgumentException e) {
			passed++;
			System.out.println("test06 passed");
			return;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("test06searchValueRemovedFromAVL failed, searched for key recently deleted from the tree and did not throw IllegalArgumentException as expected");
		failed++;
	}
	
	@Test
	public void test07searchValueInAVL() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		try {
			tree.insert(1);
			tree.insert(2);
			
			tree.search(1);
		} catch (IllegalArgumentException e) {
			System.out.println("test07searchValueInAVL failed, searched for value that was in the tree but unexpectedly threw IllegalArgumentException");
			failed++;
			return;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
		
		passed++;
		System.out.println("test07 passed");
	}
	
	@Test
	public void test08printAVL() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		try {
			tree.insert(1);
			tree.insert(0);
			tree.insert(2);
			
			assertEquals(tree.print(), "0 1 2 ");
		} catch (AssertionError e) {
			System.out.println("test08printAVL failed, expected tree with nodes 0, 1, and 2 to print 0 1 2 but printed" + tree.print() + "instead");
			failed++;
			return;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			return;
		}
		passed++;
		System.out.println("test08 passed");
	}
	
	@Test
	public void test09isBalancedTreeInsertingAll() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		
		try {
			tree.insert(2);
			tree.getHead().setLeftNode(1);
			tree.getHead().setRightNode(3);
			
			tree.getHead().getLeftNode().setLeftNode(0);
			tree.getHead().getRightNode().setRightNode(4);
			
			assertTrue(tree.checkForBalancedTree());
			
			tree.getHead().getLeftNode().getLeftNode().setLeftNode(-1);
			
			assertFalse(tree.checkForBalancedTree());
		} catch (AssertionError e) {
			System.out.println("test09isBalancedTreeInsertingAll failed, did not give expected outcome on one of two trees, one of which was balanced and one of which was not");
			failed++;
			return;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			return;
		}
		passed++;
		System.out.println("test09 passed");
	}
	
	@Test
	public void test10isBinarySearchTree() {
		AVLTree<Integer> tree = new AVLTree<>(null);
		AVLTree<Integer> tree1 = new AVLTree<>(null);
		
		try {
			tree.insert(1);
			tree.getHead().setLeftNode(2);
			tree.getHead().setRightNode(3);
			assertFalse(tree.checkForBinarySearchTree());
			
			tree1.insert(1);
			tree1.getHead().setLeftNode(0);
			tree1.getHead().setLeftNode(2);
			assertTrue(tree.checkForBinarySearchTree());
		} catch (AssertionError e) {
			System.out.println("test10isBinarySearchTree failed, incorrectly identified what is and isn't a binary search tree");
			failed++;
			return;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			return;
		}
		passed++;
		System.out.println("test10 passed");
	}
}
