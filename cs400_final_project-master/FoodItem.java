import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a food item with all its properties.
 * 
 * @author aka
 */
public class FoodItem {
    // The name of the food item.
    private String name;

    // The id of the food item.
    private String id;

    // Map of nutrients and value.
    private HashMap<String, Double> nutrients;
    
    /**
     * Constructor
     * @param name name of the food item
     * @param id unique id of the food item
     * 
     * Initializes the id and name to what is passed in as a parameter, and nutrients is initialized to null
     */
    public FoodItem(String id, String name) {
        this.id = id;
        this.name = name;
        nutrients = new HashMap<String, Double>();
    }
    
    /**
     * Gets the name of the food item
     * 
     * @return name of the food item
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique id of the food item
     * 
     * @return id of the food item
     */
    public String getID() {
        return id;
    }
    
    /**
     * Gets the nutrients of the food item
     * 
     * @return nutrients of the food item
     */
    public HashMap<String, Double> getNutrients() {
        return nutrients;
    }

    /**
     * @param name is the name of the nutrient whose value we are adding or updating
     * @param value is the value of the given nutrient we are adding or updating
     * 
     * Adds a nutrient and its value to this food. 
     * If nutrient already exists, updates its value.
     */
    public void addNutrient(String name, double value) {
    	nutrients.put(name, value);
    }

    /**
     * @param name is the name of the nutrient whose value we are returning
     * 
     * Returns the value of the given nutrient for this food item. 
     * If not present, then returns 0.
     */
    public double getNutrientValue(String name) {
        if(!nutrients.containsKey((Object)name))
        	return 0;
        return nutrients.get((Object)name).doubleValue();
    }
    
}
