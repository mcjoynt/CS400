
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Filename: CourseSchedulerUtil.java Project: p4 Authors: Debra Deppeler, Matthew McJoynt, Austin Muschott
 * 
 * Use this class for implementing Course Planner
 * 
 * @param <T>
 *            represents type
 */

public class CourseSchedulerUtil<T> {

	// can add private but not public members

	/**
	 * Graph object
	 */
	private GraphImpl<T> graphImpl;

	/**
	 * constructor to initialize a graph object
	 */
	public CourseSchedulerUtil() {
		this.graphImpl = new GraphImpl<T>();
	}

	/**
	 * createEntity method is for parsing the input json file
	 * 
	 * @return array of Entity object which stores information about a single course
	 *         including its name and its prerequisites
	 * @throws Exception
	 *             like FileNotFound, JsonParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Entity[] createEntity(String fileName) throws Exception {
		Object parser = new JSONParser().parse(new FileReader(fileName));

		JSONObject jsonObject = (JSONObject) parser;
		JSONArray courses = (JSONArray) jsonObject.get("courses");
		Entity[] allEntities = new Entity[courses.size()];

		for (int i = 0; i < courses.size(); i++) {
			Entity<T> createdEntity = new Entity<T>();
			JSONObject subObj = (JSONObject) courses.get(i);
			Object name = subObj.get("name");
			JSONArray prerequisites = (JSONArray) subObj.get("prerequisites");

			createdEntity.setName((T) name);
			T[] prerek = (T[]) new Object[prerequisites.size()];

			for (int t = 0; t < prerequisites.size(); t++) {
				Object temp = prerequisites.get(t);
				prerek[t] = (T) temp;
			}

			createdEntity.setPrerequisites(prerek);

			if (allEntities[allEntities.length - 1] != null) {
				Entity[] newAllEntities = new Entity[(allEntities.length) * 2];

				for (int sub = 0; sub < allEntities.length; sub++)
					newAllEntities[sub] = allEntities[sub];

				allEntities = newAllEntities;
			}
			allEntities[i] = createdEntity;
		}

		return allEntities;
	}

	/**
	 * Construct a directed graph from the created entity object
	 * 
	 * @param entities
	 *            which has information about a single course including its name and
	 *            its prerequisites
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void constructGraph(Entity[] entities) {
		for (int i = 0; i < entities.length; i++) {
			T cur = (T) entities[i].getName();

			Object[] pre = entities[i].getPrerequisites();

			graphImpl.addVertex(cur);
			for (int j = 0; j < pre.length; j++) {
				graphImpl.addVertex((T) pre[j]);
				if(pre[j] != null)
					graphImpl.addEdge(cur, (T) pre[j]);
			}
		}

	}

	/**
	 * Returns all the unique available courses
	 * 
	 * @return the list of all available courses
	 */
	public Set<T> getAllCourses() {
		return graphImpl.getAllVertices();
	}

	/**
	 * To check whether all given courses can be completed or not
	 * 
	 * @return boolean true if all given courses can be completed, otherwise false
	 * @throws Exception
	 */
	public boolean canCoursesBeCompleted() throws Exception {
		GraphImpl<T> copy = new GraphImpl<T>();
		Iterator<T> iterator1 = graphImpl.getAllVertices().iterator();
		while(iterator1.hasNext()) {
			T currentNode = iterator1.next();
			copy.addVertex(currentNode);
		}
		
		Iterator<T> iterator2 = graphImpl.getAllVertices().iterator();
		while(iterator2.hasNext()) {
			T main = iterator2.next();
			List<T> adj = graphImpl.getAdjacentVerticesOf(main);
			Iterator<T> iterator3 = adj.iterator();
			while(iterator3.hasNext()) {
				T curNode = iterator3.next();
				copy.addEdge(main, curNode);
			}
		}
		
		Set<T> graphSet = copy.getAllVertices();// getAllCourses but maintains independence
		Object[] allVerts = graphSet.toArray();
		Stack<T> st = new Stack<T>();

		int i = 0;
		int count = 0;
		int max = allVerts.length * allVerts.length;
		while (copy.size() > 0) {
			if(i >= allVerts.length)
				i = 0;
			T currentClass = (T)allVerts[i];
			// stack.add(currentClass);
			if (!graphImpl.getAdjacentVerticesOf(currentClass).isEmpty()) {
				List<T> subClasses = copy.getAdjacentVerticesOf(currentClass);
				Object[] preReqs = subClasses.toArray();
				for (int j = 0; j < preReqs.length; j++) {
					T currentSubClass = (T)preReqs[j];
					if (st.contains(currentSubClass)) { // assuming no shared prerequisites
						return false;
					} else if (copy.getAdjacentVerticesOf(currentSubClass).isEmpty()
							|| allVisited(currentSubClass, st)) {
						st.add(currentSubClass); // mark as visited
						copy.removeVertex(currentSubClass);
						graphSet = copy.getAllVertices();// getAllCourses but maintains independence
						allVerts = graphSet.toArray();
						break;
					}
				}
			}
			i++;
			count++;
			if(count > max)
				return false;
		}

		return true;
	}

	/**
	 * @param cur the node whose prerequisites we are checking
	 * @param visited the stack of visited nodes
	 * @return true if all prerequisites have been visited
	 * 
	 * This is a private helper method for canCoursesBeCompleted() that returns true if
	 * all of a node's prerequisites have been visited
	 * */
	private boolean allVisited(T cur, Stack<T> visited) {
		List<T> prereqs = graphImpl.getAdjacentVerticesOf(cur);
		Iterator<T> it = prereqs.iterator();
		
		while(it.hasNext()) {
			T thisOne = it.next();
			if(!visited.contains(thisOne))
				return false;
		}
		
		return true;
	}

	/**
	 * The order of courses in which the courses has to be taken
	 * 
	 * @return the list of courses in the order it has to be taken
	 * @throws Exception
	 *             when courses can't be completed in any order
	 */
	public List<T> getSubjectOrder() throws Exception {
		if (!canCoursesBeCompleted())
			throw new Exception();

		Stack<T> stack = new Stack<T>();
		List<T> finalList = new LinkedList<T>();

		Set<T> vertices = getAllCourses();
		Object[] allVerts = vertices.toArray();

		boolean[] visited = new boolean[allVerts.length];

		for (int i = 0; i < allVerts.length; i++) {
			T cur = (T) allVerts[i];

			if (!hasPredecessors(cur)) {
				stack.push(cur);
				visited[i] = true;
				finalList.add(0, cur);
			}
		}

		while (!stack.isEmpty()) {
			T c = stack.peek();

			List<T> list = graphImpl.getAdjacentVerticesOf(c);
			Object[] arrList = list.toArray();

			boolean allSuccessorsVisited = true;

			int j = 0;
			for (int i = 0; i < visited.length; i++) {
				if (isSuccessor((T) allVerts[i], arrList) && !visited[i]) {
					allSuccessorsVisited = false;
					j = i;
					break;
				}
			}

			if (allSuccessorsVisited)
				c = stack.pop();
			else {
				stack.push((T) allVerts[j]);
				visited[j] = true;
				finalList.add(0, (T) allVerts[j]);
			}
		}

		return finalList;
	}

	/**
	 * @param vertex is the vertex we are checking to see if it is a successor of any nodes
	 * @param successors is the array of nodes that could succeed vertex
	 * @return true if vertex is a successor to any nodes
	 * */
	private boolean isSuccessor(T vertex, Object[] successors) {
		for (int i = 0; i < successors.length; i++) {
			T cur = (T) successors[i];
			if (cur.equals(vertex))
				return true;
		}

		return false;
	}

	/**
	 * @param cur is the node we are checking for predecessors
	 * @return true if cur has any predecessors
	 * */
	private boolean hasPredecessors(T cur) {
		Set<T> vertices = getAllCourses();
		Iterator<T> iterator = vertices.iterator();

		while (iterator.hasNext()) {
			T checkNode = iterator.next();

			if (!checkNode.equals(cur) && graphImpl.getAdjacentVerticesOf(checkNode).contains(cur))
				return true;
		}
		return false;
	}

	/**
	 * The minimum course required to be taken for a given course
	 * 
	 * @param courseName
	 * @return the number of minimum courses needed for a given course
	 */
	public int getMinimalCourseCompletion(T courseName) throws Exception {
		if (!canCoursesBeCompleted())
			throw new Exception();

		List<T> prerequisites = graphImpl.getAdjacentVerticesOf(courseName);
		int sum = 0;
		Iterator<T> iterator = prerequisites.iterator();
		while (iterator.hasNext()) {
			T currentEntity = iterator.next();
			if (graphImpl.getAdjacentVerticesOf(currentEntity) == null) {
				sum++;// base case no other prerequisites so count just self
			} else {
				sum++;
				sum = sum + getMinimalCourseCompletion(currentEntity);
			}
		}
		return sum;
	}

}
