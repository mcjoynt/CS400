import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Filename:   FoodData.java
 * Project:    Food Query and Meal Analysis
 * Course:     cs400 
 * Authors:    Dominic Bourget, David Waltz, Austin Muschott, Megan Stoffel, Matthew McJoynt
 * Due Date:   Wed. Dec 12 10 pm
 * 
 *
 *
 * This class represents the backend for managing all the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */

public class FoodData implements FoodDataADT<FoodItem> {

    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;


    /**
     * Public constructor
     */
    public FoodData() {
        foodItemList = new LinkedList<FoodItem>();
        indexes = new HashMap<String, BPTree<Double, FoodItem>>();
    }
    
    public void setFoodItemList(List<FoodItem> newResults){
        Iterator<FoodItem> foodIterator = newResults.iterator();
        while(foodIterator.hasNext())
        	addFoodItem(foodIterator.next());
    }
    
    public List<FoodItem> getFoodItemList() {
        return this.foodItemList;
    }


    /*
     * 
     * 
     * @param - file name containing food items to be added
     */
    @Override
    public void loadFoodItems(String filePath) {

        FileReader fr = null;
        BufferedReader br = null;

        try {

            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            String nextLine = br.readLine();
            
            // iterate through each line of the input file
            while (nextLine != null && !nextLine.equals("")) {

                String[] dataMembers = nextLine.split(",");
                FoodItem foodItem = new FoodItem(dataMembers[0], dataMembers[1]); // initialize food item

                // i+= 2 is used because every even index is a nutrition type, and the odd index is the value
                for (int i = 2; i < dataMembers.length; i += 2) {
                	
                	// i is the type, i + 1 is the value
                    foodItem.addNutrient(dataMembers[i], Double.parseDouble(dataMembers[i + 1]));
                }
                
                // make sure an item isn't added twice
                boolean duplicate = false;
                
                for(int i = 0; i < foodItemList.size(); i++) {
                	if(foodItemList.get(i).getID().equals(foodItem.getID())) {
                		duplicate = true;
                		break;
                	}
                		
                }
                if(!duplicate)
                	addFoodItem(foodItem);
                
                nextLine = br.readLine();
                


            }

        } catch (IOException e) {
            
            System.out.println("File IOException occured");
        }
        catch(Exception e){
        	System.out.println("Exception occured");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     * @param substring is the substring that we are searching for in the name of the food items
     * @return List<FoodItem> is the list of items containing the substring, ignoring capital and lowercase letters
     *
     * The filterByName method is called in the GUI.java class when the user searches for a name, the list is returned to
     * GUI.java and is filtered back into the list of food items
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
    	
        List<FoodItem> newItems = new LinkedList<FoodItem>();

        Iterator<FoodItem> throughFood = foodItemList.iterator();
        
        while (throughFood.hasNext()) {
        	
            FoodItem cur = throughFood.next();
            String lowercase = cur.getName().toLowerCase();
            String subLowercase = substring.toLowerCase();

            if (lowercase.contains(subLowercase))
                newItems.add(cur);
        }
        
    
        // sort the list by name
        newItems = newItems.stream().sorted((Object1, Object2) -> Object1.getName().
        		compareTo(Object2.getName())).collect(Collectors.toList());
        
        
        return newItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     * @param rules is the list of rules in the form <nutrient> <comparator> <value> that need to be filtered through
     * @return List<FoodItem> is the list of items that follow each of the rules passed through the rules parameter
     *
     * filterByNutrients serves essentially the same purpose as filterByName except it filters by the items' nutrients.
     * If multiple rules are applied, it only returns the list of items that meet every rule.
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
    	
        List<FoodItem> finalList = new LinkedList<FoodItem>();
    	boolean first = true;
    	
    	Iterator<String> rulesIterator = rules.iterator();
    	
    	String nutrient;
    	String comparator;
    	String value;
    	
    	while(rulesIterator.hasNext()) {
    		String curRule = (String) rulesIterator.next();
    		String[] split = curRule.split(" ");
    		
    		nutrient = split[0];
            comparator = split[1];
            value = split[2];
            double realValue = Double.parseDouble(value);
            
            BPTree<Double, FoodItem> nutrientList = indexes.get(nutrient);
			List<FoodItem> curRuleList;
			if(nutrientList != null)
				curRuleList = nutrientList.rangeSearch(realValue, comparator);
			else
				curRuleList = new LinkedList<FoodItem>();

			if(first)
				finalList = curRuleList;
				
			Iterator<FoodItem> through = curRuleList.iterator();
			while (through.hasNext()) {
				FoodItem curItem = through.next();

				if (!finalList.contains((Object) curItem))
					finalList.remove(curItem);
			}
			
			for(int j = 0; j < finalList.size(); j++) {
				if(!curRuleList.contains((Object) finalList.get(j)))
					finalList.remove(finalList.get(j));
			}
            
            first = false;
    	}
    	
    	GUI.searchList = finalList;
    	
        return finalList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	
        foodItemList.add(foodItem);
        
        Set<String> nutrients = foodItem.getNutrients().keySet();
    	Iterator<String> nutrientIterator = nutrients.iterator();
    	while(nutrientIterator.hasNext()) {
    		String curNode = nutrientIterator.next();
    		if(indexes.containsKey(curNode)) //nutrient already exists
    			indexes.get((Object)curNode).insert(foodItem.getNutrientValue(curNode), foodItem);
    		else { //nutrient does not already exist
    			BPTree<Double, FoodItem> newTree = new BPTree<Double, FoodItem>(5);
    			newTree.insert(foodItem.getNutrientValue(curNode), foodItem);
    			
    			indexes.put(curNode, newTree);
    		}
    	}
    }

    /*
     * (non-Javadoc)
     * 
     * @return - the list of food items
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }


    @Override
    public void saveFoodItems(String filename) {

        File file = new File(filename);
        PrintWriter fo = null;

        try {
        	
            fo = new PrintWriter(file);
            
            
            // gets a sorted list of all food items in the database
            List<FoodItem> sorted = GUI.data.foodItemList.stream().sorted((Object1, Object2) -> Object1.getName()
            		.compareTo(Object2.getName())).collect(Collectors.toList());
            
            // writes the name of each food item to the output file.
            for(int i = 0; i < sorted.size(); i++) {
            	
        		fo.write(sorted.get(i).getName());

            	if(i != sorted.size() - 1 )
            		fo.write(", ");
            	
            }
            	
            	
            fo.close();
             

        } catch (IOException e) {
            
            System.out.println("error saving file, IOException occured");
        }
        catch(Exception e) {
        	System.out.println("error saving file");
        }        

    }

}
