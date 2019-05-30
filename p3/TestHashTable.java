
/**
 * Filename: TestHashTable.java Project: p3 Authors: Matthew McJoynt (Debra Lecture 001) and Austin Muschott (Andy Lecture 003)
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: October 29, 2018
 * 
 * Credits: none
 * 
 * Bugs: test011_update() test contradicts test009_testPutArrayList()
 */

import java.util.NoSuchElementException; // expect to need
import static org.junit.Assert.*;
import org.junit.Before; // setUp method
import org.junit.After; // tearDown method
import org.junit.Test;

/** TODO: add class header comments here */
public class TestHashTable {

	// Allows us to create a new hash table before each test
	static HashTableADT<Integer, Object> ht;

	/**
	 * @throws Exception
	 *             if it can't contruct the table
	 */
	@Before
	public void setUp() throws Exception {
		ht = new HashTable<Integer, Object>();
	}

	/**
	 * @throws Exception
	 *             if it can't tear down the table
	 */
	@After
	public void tearDown() throws Exception {
		ht = null;
	}

	/**
	 * IMPLEMENTED AS EXAMPLE FOR YOU Tests that a HashTable is empty upon
	 * initialization
	 */
	@Test
	public void test000_IsEmpty() {
		assertEquals("size with 0 entries:", 0, ht.size());
	}

	/**
	 * IMPLEMENTED AS EXAMPLE FOR YOU Tests that a HashTable is not empty after
	 * adding one (K, V) pair
	 */
	@Test
	public void test001_IsNotEmpty() {
		ht.put(1, 2);
		int expected = 1;
		int actual = ht.size();
		assertEquals("size with one entry:", expected, actual);
	}

	/**
	 * Tests get() by putting one element in the table and calling get() on it with
	 * its key
	 */
	@Test
	public void test002_testGet() {
		Object expected = 1;
		ht.put(5, expected);
		Object actual = ht.get(5);
		assertEquals("get one int:", expected, actual);
	}

	/**
	 * Calls get on a null key and expects an IllegalArgumentException to return
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test003_testGetIllegalArgumentException() {
		ht.get(null);
	}

	/**
	 * Calls get on a key that does not have a value, expects NoSuchElementException
	 * to be thrown
	 */
	@Test(expected = NoSuchElementException.class)
	public void test004_testGetNoSuchElementException() {
		ht.put(5, 1);
		ht.get(0);// zero is empty
	}

	/**
	 * Puts a double into the table, calls get() on its key and expects the correct
	 * return value
	 */
	@Test
	public void test005_testGetDouble() {
		Object expected = 1.0;
		ht.put(5, expected);
		Object actual = ht.get(5);
		assertEquals("get one double:", expected, actual);
	}

	/**
	 * Makes sure that get() works correctly when the value is a String
	 */
	@Test
	public void test006_testGetString() {
		Object expected = "String";
		ht.put(5, expected);
		Object actual = ht.get(5);
		assertEquals("get one double:", expected, actual);
	}

	/**
	 * Trys to put an element into the table with a null key, expects
	 * IllegalArgumentException to be thrown
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test007_testPutException() {
		ht.put(null, 3);
	}

	/**
	 * Puts an element into the table, calls get on its key and makes sure it
	 * returns the correct result
	 */
	@Test
	public void test008_testPutEmptySpot() {
		Object expected = 5;
		int location = 2;
		ht.put(location, expected);
		Object result = ht.get(location);
		assertEquals("Put() for empty spot:", expected, result);
	}

	/**
	 * Puts 2 elements in the same location in the table, makes sure that it handles
	 * the collision correctly
	 */
	@Test // tests if array list can be created
	public void test009_testPutArrayList() {
		Object firstInsert = 6;
		Object secondInsert = 7;
		int location = 3;
		ht.put(location, firstInsert);
		ht.put(location, secondInsert);
		Object result = ht.get(location);
		Object expected = firstInsert;
		assertEquals("Put with collision:", expected, result);
	}

	/**
	 * Puts 3 elements in the same spot in the table and makes sure the collision is
	 * handled correctly.
	 */
	@Test // tests if array list can hold more than two values i.e. create and then add to
			// it
	public void test012_testPutExistingArrayList() {
		Object firstInsert = 6;
		Object secondInsert = 7;
		Object thirdInsert = 8;
		int location = 3;
		ht.put(location, firstInsert);
		ht.put(location, secondInsert);
		ht.put(location, thirdInsert);// third insert means inserting into existing array list
		Object result = ht.get(location);
		Object expected = firstInsert;
		assertEquals("Put three with collision:", expected, result);
	}

	/**
	 * Makes sure that the table resizes and rehashes correctly
	 */
	@Test
	public void test013_testExpandingHashTable() {
		Object firstInsert = 1;
		Object secondInsert = 2;
		Object thirdInsert = 3;
		Object fourthInsert = 4;
		ht.put(0, firstInsert);
		ht.put(1, secondInsert);
		ht.put(2, thirdInsert);
		ht.put(3, fourthInsert);
		if (ht.size() <= 3) {
			fail("Hash table failed to expand");
		}
	}

	/**
	 * IMPLEMENTED AS EXAMPLE FOR YOU Other tests assume <int,Object> pairs, this
	 * test checks that <Long,Object> pair also works.
	 */
	@Test
	public void test010_Long_Object() {
		Long key = 9876543210L;
		Object expected = "" + key;
		HashTableADT<Long, Object> table = new HashTable<Long, Object>();
		table.put(key, expected);
		Object actual = table.get(key);
		assertTrue("put-get of (Long,Object) pair", expected.equals(actual));
	}

	/*
	 * Tests that the value for a key is updated when tried to insert again.
	 */
	@Test
	public void test011_Update() {
		// TODO: implement this test
		fail("this test makes no sense if there is a collison then its handled with an arraylist");
	}

	@Test
	public void test014_Test_String_Value() {
		String expected = "wholesome";
		int key = 10;
		ht.put(key, expected);
		Object result = ht.get(key);
		Object key2 = "wholesome";
		assertTrue("Results of using string as a value", key2.equals(result));
	}

	@Test
	public void test015_Test_Lengthy_String_Key() {
		String key = "Mo Bamba";
		HashTableADT<String, Object> stringTable = new HashTable<String, Object>();
		int value = 100;
		stringTable.put(key, value);
		Object actual = stringTable.get(key);
		assertTrue("put-get of (String, Object) pair", value == (int) actual);

	}

	@Test
	public void test016_Test_Two_Word_String_Key() {
		String key = "Mo";
		HashTableADT<String, Object> stringTable = new HashTable<String, Object>();
		int value = 100;
		stringTable.put(key, value);
		Object actual = stringTable.get(key);
		assertTrue("put-get of (String, Object) pair", value == (int) actual);
	}

	@Test
	public void test017_Test_One_Word_String_Key() {
		String key = "A";
		HashTableADT<String, Object> stringTable = new HashTable<String, Object>();
		int value = 100;
		stringTable.put(key, value);
		Object actual = stringTable.get(key);
		assertTrue("put-get of (String, Object) pair", value == (int) actual);
	}

	@Test
	public void test018_Test_Capital_V_LowerCase_String_Key() {
		String key = "A";
		String lower = "a";
		HashTableADT<String, Object> stringTable = new HashTable<String, Object>();
		int value = 100;
		int lowerValue = 101;
		stringTable.put(key, value);
		stringTable.put(lower, lowerValue);
		Object actual = stringTable.get(key);
		Object lowerActual = stringTable.get(lower);
		if (actual.equals(lowerActual)) {
			fail("lower and uppercase values should hash to different locations");
		} else {
			assertTrue("upper and lower case tests", value == (int) actual);
		}
	}

	/*
	 * Tests that inserting many and removing one entry from the hash table works
	 */
	@Test(timeout = 1000 * 10)
	public void test100_InsertManyRemoveOne() {
		int many = 1000; // I am defining many as 1000 here but Theoretically it could be any number. In
		// this case it would take each insert taking 10 seconds to time out
		String value = "Cardi";// Not a hot take its factual
		for (int i = 0; i < many; i++) {
			ht.put(i, value);
		}
		ht.remove(0);// just removes the first value
	}

	/*
	 * Tests ability to insert many entries and and remove many entries from the
	 * hash table
	 */
	@Test(timeout = 1000 * 10)
	public void test110_InsertRemoveMany() {
		int many = 1000; // I am defining many as 1000 here but Theoretically it could be any number. In
							// this case it would take each insert taking 10 seconds to time out
		String value = "In-N-Out > Culver's";// Not a hot take its factual
		for (int i = 0; i < many; i++) {
			ht.put(i, value);
		}
		for (int j = ht.size() - 1; j >= 0; j--) {
			ht.remove(j);
		}
	}
}