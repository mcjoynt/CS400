import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Filename: GraphImpl.java Project: p4 Course: cs400 Authors: Matthew McJoynt
 * and Austin Muschott Due Date: November 19, 2018
 * 
 * T is the label of a vertex, and List<T> is a list of adjacent vertices for
 * that vertex.
 *
 * Additional credits: none
 *
 * Bugs or other notes: none
 *
 * @param <T>
 *            type of a vertex
 */
public class GraphImpl<T> implements GraphADT<T> {

	// YOU MAY ADD ADDITIONAL private members
	// YOU MAY NOT ADD ADDITIONAL public members

	/**
	 * Store the vertices and the vertice's adjacent vertices
	 */
	public Map<T, List<T>> verticesMap;

	/**
	 * Construct and initialize and empty Graph
	 */
	public GraphImpl() {
		verticesMap = new HashMap<T, List<T>>();
		// you may initialize additional data members here
	}

	/**
	 * @param vertex
	 *            is the vertex being added to the graph
	 * 
	 *            Adds the vertex to the graph.
	 */
	public void addVertex(T vertex) {
		if (vertex == null)
			return;
		Iterator<T> list = getAllVertices().iterator();
		while (list.hasNext()) {
			if (list.next().equals(vertex))
				return;
		}
		List<T> emptyList = new ArrayList<T>();
		verticesMap.put(vertex, emptyList);
	}

	/**
	 * @param vertex
	 *            is the vertex being removed from the graph
	 * 
	 *            Removes a vertex from the graph and all edges that it connected to
	 */
	public void removeVertex(T vertex) {
		if (vertex == null)
			return;
		if (!getAllVertices().contains(vertex))
			return;
		for (T someVertex : verticesMap.keySet())
			removeEdge(someVertex, vertex);
		verticesMap.remove(vertex);
	}

	/**
	 * @param vertex1
	 *            is the first vertex of the edge
	 * @param vertex2
	 *            is the second vertex of the edge
	 * 
	 *            Adds an edge to the graph
	 */
	public void addEdge(T vertex1, T vertex2) {
		if (vertex1 == null || vertex2 == null)
			return;
		if (!hasVertex(vertex1) || !hasVertex(vertex2))
			return;
		if (getAdjacentVerticesOf(vertex1).contains(vertex2))
			return;
		verticesMap.get(vertex1).add(vertex2);
	}

	/**
	 * @param vertex1
	 *            is the first vertex of the edge being removed
	 * @param vertex2
	 *            is the second vertex of the edge being removed
	 * 
	 *            Removes an edge from the graph
	 */
	public void removeEdge(T vertex1, T vertex2) {
		if (vertex1 == null || vertex2 == null)
			return;
		if (!getAllVertices().contains(vertex1) || !getAllVertices().contains(vertex2))
			return;
		if (!getAdjacentVerticesOf(vertex1).contains(vertex2))
			return;

		verticesMap.get(vertex1).remove(vertex2);
	}

	/**
	 * @return the set of all vertices in the map
	 * 
	 *         Returns a set that contains all vertices in the graph
	 */
	public Set<T> getAllVertices() {
		return verticesMap.keySet();
	}

	/**
	 * @return list of prerequisites of vertex
	 * @param vertex
	 *            is the vertex whose prerequisites we are looking for
	 * 
	 *            Returns the prerequisites of a given vertex
	 */
	public List<T> getAdjacentVerticesOf(T vertex) {
		if (verticesMap.get(vertex) != null)
			return verticesMap.get(vertex);
		List<T> ope = new ArrayList<T>();
		return ope;
	}

	/**
	 * @return true if map contains vertex, false otherwise
	 * @param vertex
	 *            is the vertex whose existence in the map we are checking for
	 *            Returns whether or not the map contains a given vertex
	 */
	public boolean hasVertex(T vertex) {
		Iterator<T> list = getAllVertices().iterator();
		while (list.hasNext()) {
			if (list.next().equals(vertex))
				return true;
		}
		return false;
	}

	/**
	 * @return the order of the map
	 * 
	 *         Returns the order of the map (number of vertices in the map)
	 */
	public int order() {
		return getAllVertices().size();
	}

	/**
	 * @return the size of the map
	 * 
	 *         Returns the size of the map (number of edges in the map)
	 */
	public int size() {
		int sum = 0;
		for (T vertex : verticesMap.keySet())
			sum += getAdjacentVerticesOf(vertex).size();
		return sum;
	}

	/**
	 * Prints the graph for the reference DO NOT EDIT THIS FUNCTION DO ENSURE THAT
	 * YOUR verticesMap is being used to represent the vertices and edges of this
	 * graph.
	 */
	public void printGraph() {

		for (T vertex : verticesMap.keySet()) {
			if (verticesMap.get(vertex).size() != 0) {
				for (T edges : verticesMap.get(vertex)) {
					System.out.println(vertex + " -> " + edges + " ");
				}
			} else {
				System.out.println(vertex + " -> " + " ");
			}
		}
	}
}
