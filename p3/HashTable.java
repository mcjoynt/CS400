
/**
 * Filename: HashTable.java Project: p3 Authors: Matthew McJoynt (001 Debra) and Austin Muschott (003 Andy)
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: Friday October 19, 10pm Version: 1.0
 * 
 * Credits: none
 * 
 * Bugs: none
 */

import java.util.NoSuchElementException;
import java.util.ArrayList;

// If there is a collision, we chose to add the items at the same index of the
// table into an ArrayList, which can be easily added to and removed from
//
// if open addressing: describe probe sequence
// if buckets: describe data structure for each bucket
//
// Our hashing algorithm uses the first, middle, and last characters in a String
// and multiplies each by a factor of 2 to spread them out. Ints and Longs are just moded by the length of the array
//
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

	private double loadFactor;
	private Object[] Hash;

	/**
	 * Default constructor initializes Hash and sets size to 4
	 */
	public HashTable() {
		Hash = new Object[4];
	}

	/**
	 * @param initialCapacity
	 *            initial capacity of Hash
	 * @param loadFactor
	 *            initial loadFactor of the object
	 * 
	 *            Initializes Hash and loadFactor to given values
	 */
	public HashTable(int initialCapacity, double loadFactor) {
		Hash = new Object[initialCapacity];
		this.loadFactor = loadFactor;

	}

	/**
	 * @param key
	 *            is the key being used to put the value in the correct position in
	 *            the Hash table
	 * @param value
	 *            is the value being put into the Hash table
	 * @throw IllegalArgumentException if key is null
	 * 
	 *        Puts a new element into the Hash table, handles collisions If the
	 *        loadFactor exceeds .75 it resizes and rehashes the table
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void put(K key, V value) throws IllegalArgumentException {

		Object[] obj = new Object[2];
		obj[0] = key;
		obj[1] = value;

		if (loadFactor >= .75)
			resize();

		if (key == null) {
			throw new IllegalArgumentException();
		}

		int spot = hash(key);

		if (Hash[spot] == null) // spot is empty, simply add value to Hash
			Hash[spot] = obj;
		else if (Hash[spot] instanceof ArrayList<?>)
			((ArrayList<Object>) Hash[spot]).add(obj);
		// already has been a collision at this
		// location
		// simply add new item to ArrayList sub
		else { // spot has 1 item in it, need to create ArrayList
			ArrayList<Object> sub = new ArrayList<Object>();
			sub.add((Object) Hash[spot]);
			sub.add(obj); // value will be at location 0 in sub
			Hash[spot] = sub;
		}

		loadFactor = (double) size() / Hash.length;
	}

	/**
	 * Resizes the array and rehashes the values to spread them out Doesn't have any
	 * collisions
	 */
	@SuppressWarnings("unchecked")
	public void resize() {
		HashTable<K, V> newTable = new HashTable<K, V>(Hash.length * 2 + 1, 0);

		Object[] oldTable = Hash;

		for (int i = 0; i < oldTable.length; i++) {
			if (oldTable[i] instanceof ArrayList) { // collision, needs to be handled
				ArrayList<Object> cur = (ArrayList<Object>) oldTable[i];

				for (int j = 0; j < cur.size(); j++) {
					Object[] curNode = (Object[]) cur.get(j);
					K key = (K) curNode[0];
					V value = (V) curNode[1];

					newTable.put(key, value);
				}

			} else if (oldTable[i] != null) { // just a single element
				Object[] curNode = (Object[]) oldTable[i];
				K key = (K) curNode[0];
				V value = (V) curNode[1];

				newTable.put(key, value);
			}
		}

		Hash = newTable.Hash;
		loadFactor = (double) size() / Hash.length;
	}

	/**
	 * @param key
	 *            is the key used to locate the element that is being returned
	 * @throws NoSuchElementException
	 *             if there is no value located where the key indicates in the table
	 * @return the value of the element with the given key
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws NoSuchElementException, IllegalArgumentException {

		if (key == null) {
			throw new IllegalArgumentException();
		}
		int spot = hash(key);// improves run time by preventing constant call to hash @improve
		if (Hash[spot] == null) {
			throw new NoSuchElementException();
		}
		if (Hash[spot] instanceof ArrayList<?>) {
			ArrayList<Object> tempArray = new ArrayList<Object>();
			tempArray = (ArrayList<Object>) Hash[spot];
			Object[] cur = (Object[]) tempArray.get(0);
			return (V) cur[1];// if there exists an array in the hash spot then get the first value

		} else if (((Object[]) Hash[spot])[0] == key)
			return (V) ((Object[]) Hash[spot])[1];
		else
			throw new NoSuchElementException();
	}

	/**
	 * @param key
	 *            is the key that corresponds to the element that needs to be
	 *            removed
	 * @throws NoSuchElementException
	 *             if the key does not exist in the tree
	 * 
	 *             Removes an element from the table with a given key
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void remove(K key) throws NoSuchElementException, IllegalArgumentException {
		if (key == null)
			throw new IllegalArgumentException();
		if (Hash[hash(key)] instanceof ArrayList) {// case where there is a collision
			int location = hash(key);
			ArrayList<Object> sub = (ArrayList<Object>) Hash[location];

			sub.remove(0);
			Hash[location] = sub;// once first spot is removed the new array list is assigned to that location
									// replacing the old
		} else {
			if (Hash[hash(key)] != null) {// case where there is a value stored just set that location to null
				Hash[hash(key)] = null;
			} else {
				throw new NoSuchElementException();
			}
		}
		loadFactor = (double) size() / Hash.length;
	}

	/**
	 * @return the number of items in the Hash table at the time
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int size() {
		int total = 0;
		for (int i = 0; i < Hash.length; i++) {
			if (Hash[i] instanceof ArrayList<?>) {
				ArrayList<Object> sub = (ArrayList<Object>) Hash[i];
				total += sub.size(); // I wanted to make sure it calls size() from the ArrayList
										// class not this class
			} // but I'm not sure how so we might have to adjust this
			else if (Hash[i] != null)
				total++;
		}
		return total;
	}

	/**
	 * @param key
	 *            is the key that needs to be hashed to find the correct index in
	 *            the Hash table
	 * @return index of where the item should go in the Hash table
	 */
	public int hash(K key) throws NoSuchElementException {
		if (key == null) {
			throw new NoSuchElementException();
		}
		if (key instanceof String) {
			int middleChar = 0;
			int lastChar = 0;// if the string is not long enough to have a middle and last character
								// then these just are not taken into account
			int firstChar = (int) ((String) key).charAt(0);
			if (((String) key).length() >= 3) {
				int middleSpot = ((String) key).length() / 2;// finds the middle spot of the string
				middleChar = ((String) key).charAt(middleSpot);
			}
			if (((String) key).length() >= 2) {
				lastChar = ((String) key).charAt((((String) key).length() - 1));
			}
			return (firstChar * 8 + middleChar * 4 + lastChar * 2) % (Hash.length + 1);// this
																						// spreads
																						// out keys
																						// that have
			// the same letters but different
			// orders the constants that the
			// character values are multiplied
			// by are arbitrary and can be
			// changed but are kept small to
			// prevent overflow
		}
		if (key instanceof Long) {
			int intKey = ((Long) key).intValue();
			return intKey % (Hash.length);
		}
		return ((int) key) % (Hash.length);// for ints and doubles just places the key in the
											// modulus of the array length, pretty standard for
											// hash tables
	}

}