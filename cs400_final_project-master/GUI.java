
import java.io.File;
import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Filename:   GUI.java
 * Project:    Food Query and Meal Analysis
 * Course:     cs400 
 * Authors:    Dominic Bourget, David Waltz, Austin Muschott, Megan Stoffel, Matthew McJoynt
 * Due Date:   Wed. Dec 12 10 pm
 * 
 *	This class contains the GUI for the project.
 */
public class GUI extends Application {


	// contains all the food items

    static FoodData data = new FoodData();
    
    // contains all the food items just in the search vbox
    static List<FoodItem> searchList = new ArrayList<FoodItem>();
    
    // contains all the food items in the menu vbox
    static List<FoodItem> menuList = new ArrayList<FoodItem>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Food Query and Meal Analysis");
        primaryStage.setResizable(false);

        //////////////////////////// gridpane setup///////////////////////////////
        /*
         * The following sets up a series of row and columns in a gridpane to position the following
         * display elements
         */
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 1000, 700);
        scene.getStylesheets().add("application.css");

        // Grid column restrictions
        ColumnConstraints leftColumn1 = new ColumnConstraints();
        leftColumn1.setPercentWidth(8.33);
        ColumnConstraints leftColumn2 = new ColumnConstraints();
        leftColumn2.setPercentWidth(8.33);
        ColumnConstraints leftColumn3 = new ColumnConstraints();
        leftColumn3.setPercentWidth(8.33);
        ColumnConstraints leftColumn4 = new ColumnConstraints();
        leftColumn4.setPercentWidth(8.33);
        ColumnConstraints middleColumn1 = new ColumnConstraints();
        middleColumn1.setPercentWidth(16.66);
        ColumnConstraints middleColumn2 = new ColumnConstraints();
        middleColumn2.setPercentWidth(16.66);
        ColumnConstraints rightColumn1 = new ColumnConstraints();
        rightColumn1.setPercentWidth(16.66);
        ColumnConstraints rightColumn2 = new ColumnConstraints();
        rightColumn2.setPercentWidth(16.66);
        gridPane.getColumnConstraints().addAll(leftColumn1, leftColumn2, leftColumn3, leftColumn4,
            middleColumn1, middleColumn2, rightColumn1, rightColumn2);

        /*
         * these colored triangles provide background color they over lap a little and extend past
         * the boarders of the scene to ensure that there is no part of the scene that is not
         * covered
         */
        Rectangle right = new Rectangle(2000, 2000);
        right.getStyleClass().add("rightPane");
        gridPane.add(right, 6, 0, 1, 6);

        Rectangle mid = new Rectangle(338, 2000);
        mid.getStyleClass().add("middlePane");
        gridPane.add(mid, 4, 0, 1, 6);

        Rectangle left = new Rectangle(335, 2000);
        left.getStyleClass().add("leftPane");
        gridPane.add(left, 0, 0, 1, 6);


        gridPane.getStyleClass().add("middleCol");

        /*
         * the following line of code displays the grid pane lines when uncommented this helps in UI
         * design
         */
        // gridPane.setStyle("-fx-grid-lines-visible: true");// for debugging only

        /*
         * these row restrictions are added to the grid pane to complete a system of rows and
         * columns that can be used in the grid pane to layout the user interface
         */
        RowConstraints topSpacer = new RowConstraints();
        topSpacer.setPercentHeight(10);
        RowConstraints topRow = new RowConstraints();
        topRow.setPercentHeight(15);
        RowConstraints searchRow = new RowConstraints();
        searchRow.setPercentHeight(5);
        RowConstraints nutriFilterRow = new RowConstraints();
        nutriFilterRow.setPercentHeight(5);
        RowConstraints middleRow = new RowConstraints();
        middleRow.setPercentHeight(50);
        RowConstraints bottomRow1 = new RowConstraints();
        bottomRow1.setPercentHeight(5);
        RowConstraints bottomRow2 = new RowConstraints();
        bottomRow2.setPercentHeight(10);
        gridPane.getRowConstraints().addAll(topSpacer, topRow, searchRow, nutriFilterRow, middleRow,
            bottomRow1, bottomRow2);

        ///////////////// Beginning of left most box, search fields and such//////////////////

        // labels the project so the user knows what program they are running
        Label projectTitle = new Label();
        projectTitle.setText("Food Query and Meal Analysis");
        projectTitle.getStyleClass().add("mainHeader");
        GridPane.setHalignment(projectTitle, HPos.CENTER);

        gridPane.add(projectTitle, 0, 1, 4, 1);// node, colindex, rowindex, colspan, rowspan

        // Vbox for toggling buttons
        VBox vboxNutrition = new VBox();
        vboxNutrition.getStyleClass().add("vbox");
        vboxNutrition.setId("vbox-radio");

        /*
         * a ToggleGroup is used to contain all the possible nutrient filters so that only one may
         * be selected at a time and added to the list of nutrition rules used in filtering.
         */
        ToggleGroup nutrition = new ToggleGroup();

        RadioButton nutritionFilter = new RadioButton("Nutrition Filter On?");
        nutritionFilter.setId("vbox-radio");
        gridPane.add(nutritionFilter, 0, 3, 4, 1);

        // the following are selectors for nutrients the user wishes to create a rule with
        RadioButton calories = new RadioButton("Calories");
        RadioButton carbs = new RadioButton("Carbs");
        RadioButton fat = new RadioButton("Fat");
        RadioButton protein = new RadioButton("Protein");
        RadioButton fiber = new RadioButton("Fiber");

        // adds the selector buttons to toggle group nutrition
        calories.setToggleGroup(nutrition);
        calories.setSelected(true);// starts this selector off as selected
        carbs.setToggleGroup(nutrition);
        fat.setToggleGroup(nutrition);
        protein.setToggleGroup(nutrition);
        fiber.setToggleGroup(nutrition);

        /*
         * inputAlphaSearch is the main search box where the user can type in a name to filter by
         * search
         */
        TextField inputAlphaSearch = new TextField();
        inputAlphaSearch.setMaxHeight(20);
        inputAlphaSearch.setMaxWidth(325);
        inputAlphaSearch.setPromptText("Type Food Item Name Here");
        inputAlphaSearch.setFocusTraversable(false);
        inputAlphaSearch.getStyleClass().add("searchBox");
        gridPane.add(inputAlphaSearch, 0, 2, 3, 1);
        GridPane.setHalignment(inputAlphaSearch, HPos.CENTER);

        vboxNutrition.getChildren().addAll(calories, carbs, fat, protein, fiber);
        gridPane.add(vboxNutrition, 0, 4, 2, 1);

        // vbox for comparators, holds the comparator symbols for nutrition rules
        VBox vboxComparators = new VBox();
        vboxComparators.getStyleClass().add("vbox");
        vboxComparators.setId("vbox-radio");

        // creates a separate toggle group for comparators so that only one can be selected at a
        // time when adding a rule
        ToggleGroup comparator = new ToggleGroup();

        RadioButton lessThanEqualTo = new RadioButton("<=");
        RadioButton greaterThanEqualTo = new RadioButton(">=");
        RadioButton equalTo = new RadioButton("==");

        lessThanEqualTo.setToggleGroup(comparator);
        lessThanEqualTo.setSelected(true);
        greaterThanEqualTo.setToggleGroup(comparator);
        equalTo.setToggleGroup(comparator);

        // adds comparators to the displayed Vbox
        vboxComparators.getChildren().addAll(lessThanEqualTo, greaterThanEqualTo, equalTo);
        gridPane.add(vboxComparators, 1, 4);

        // a text box where the user types the value they wish to use with the rule
        TextField inputValue = new TextField();
        inputValue.setMaxHeight(20);
        inputValue.setMaxWidth(200);
        inputValue.setPromptText("Value");
        inputValue.setFocusTraversable(false);
        gridPane.add(inputValue, 2, 3);
        GridPane.setValignment(inputValue, VPos.TOP);

        // button that adds the user created rule to the list of rules for the search query to
        // follow
        Button addRule = new Button();
        addRule.setText("Add Rule");
        addRule.getStyleClass().add("buttonStyle2");
        gridPane.add(addRule, 3, 3);
        GridPane.setHalignment(addRule, HPos.CENTER);
        GridPane.setValignment(addRule, VPos.TOP);

        // button that clears all selected rules from the rules search list
        Button clearRule = new Button();
        clearRule.setText("Clear Rules");
        clearRule.getStyleClass().add("buttonStyle2");
        gridPane.add(clearRule, 0, 4, 2, 1);
        GridPane.setHalignment(clearRule, HPos.CENTER);
        GridPane.setValignment(clearRule, VPos.BOTTOM);

        // button that applies both a name search and nutrients search if nutrients search is
        // selected
        Button search = new Button();
        search.setText("Search");
        search.getStyleClass().add("buttonStyle2");
        gridPane.add(search, 3, 2);
        GridPane.setHalignment(search, HPos.CENTER);
        GridPane.setValignment(search, VPos.CENTER);

        // Text field that reads in the file name for a user to save the food data to or read food
        // data from
        TextField addFile = new TextField();
        addFile.setMaxHeight(20);
        addFile.setMaxWidth(150);
        addFile.setPromptText("File Name");
        addFile.setFocusTraversable(false);
        gridPane.add(addFile, 2, 5, 2, 1);
        GridPane.setHalignment(addFile, HPos.CENTER);

        // button that allows the user to manually add a food item to the database
        Button addManual = new Button();
        addManual.setMaxHeight(20);
        addManual.setMaxWidth(150);
        addManual.setText("Add Item Manually");
        addManual.getStyleClass().add("buttonStyle2");
        gridPane.add(addManual, 0, 6, 2, 1);
        GridPane.setHalignment(addManual, HPos.CENTER);

        // button that allows user to add food data from the file specified in the TextField
        // "addFile"
        Button addFromFile = new Button();
        addFromFile.setMaxHeight(20);
        addFromFile.setMaxWidth(150);
        addFromFile.setText("Add Item From File");
        addFromFile.getStyleClass().add("buttonStyle2");
        gridPane.add(addFromFile, 2, 6, 2, 1);
        GridPane.setHalignment(addFromFile, HPos.CENTER);

        // allows the user to write the food data to a file specified in the TextField "addFile"
        Button save = new Button("Save");
        save.setMaxHeight(20);
        save.setMaxWidth(150);
        save.getStyleClass().add("buttonStyle2");
        gridPane.add(save, 0, 5, 2, 1);
        GridPane.setHalignment(save, HPos.CENTER);

        //////////////// Beginning of Results Box///////////////////

        // header for the search results field so the user knows what is being displayed
        Label searchResults = new Label();
        searchResults.setText("Search Results");
        searchResults.getStyleClass().add("header");
        gridPane.add(searchResults, 4, 0, 2, 1);

        // Vbox that will contain the results of the search query
        VBox results = new VBox();

        results.setPadding(new Insets(15, 15, 15, 15));// offset of actual boxes
        results.getStyleClass().add("vbox");
        results.setId("vbox-list");

        // scrollable field to store the results of a search
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(results);
        gridPane.add(scrollPane, 4, 1, 2, 4);

        // button that adds the food items selected from the search results to the menu list
        Button addToMenu = new Button("Add Item To Menu");
        addToMenu.setMaxHeight(20);
        addToMenu.setMaxWidth(150);
        gridPane.add(addToMenu, 5, 6);
        GridPane.setHalignment(addToMenu, HPos.CENTER);


        ////////////////// Beginning of Menu Box//////////////////////////

        // label that lets the user know this area of the GUI is the menu
        Label menu = new Label();
        menu.setText("Menu");
        menu.getStyleClass().add("header");
        gridPane.add(menu, 6, 0, 2, 1);

        // vbox that contains the menu items in the form of check boxes
        VBox menuBox = new VBox();// menu box

        menuBox.setPadding(new Insets(15, 15, 15, 15));// offset of the actual boxes
        menuBox.getStyleClass().add("vbox");
        menuBox.setId("vbox-list");

        // scrollable object that allows the user to scroll through all the items in the menu, it
        // only appears if there are enough items on the menu
        ScrollPane scrollMenu = new ScrollPane();
        scrollMenu.setContent(menuBox);
        gridPane.add(scrollMenu, 6, 1, 2, 4);

        // analyze mean button, this button analyzes the contents of the menu
        Button analyzeMeal = new Button("Analyze Meal");
        addToMenu.setMaxHeight(20);
        addToMenu.setMaxWidth(150);
        gridPane.add(analyzeMeal, 7, 6);
        GridPane.setHalignment(analyzeMeal, HPos.CENTER);

        // removeFromMenu button removes checked objects from the menu list
        Button removeFromMenu = new Button("Remove Item");
        addToMenu.setMaxHeight(20);
        addToMenu.setMaxWidth(150);
        gridPane.add(removeFromMenu, 6, 6);
        GridPane.setHalignment(removeFromMenu, HPos.CENTER);


        primaryStage.setScene(scene);
        primaryStage.show();

        ///////////////////////////// Event Handlers/////////////////////////////


        /*
         * lambda expression that add functionality to the save button. If the save button is
         * pressed it saves the contents of the data base to the file specified in the "filename"
         * text box
         */
        save.setOnAction(e -> {
            String filename = addFile.getText();
            data.saveFoodItems(filename);
        });

        /*
         * lambda expression that adds functionality to addFromFile button. If the addFromFile
         * button is pressed it loads the food items found in the destination specified in the text
         * box "filename" into the B+ tree.
         */
        addFromFile.setOnAction(e -> {
            String fileName = addFile.getText();
            data.loadFoodItems(fileName);
        });

        // this Vbox will be used to display the rules that are currently being applied to the
        // search query
        VBox rulesList = new VBox();
        rulesList.getStyleClass().add("Vbox");
        gridPane.add(rulesList, 2, 4, 2, 1);// node, colindex, rowindex, colspan, rowspan
        Label ruleTitle = new Label("Rules:");
        GridPane.setValignment(rulesList, VPos.BOTTOM);

        // a list of rules of type string that will be passed into the filter by nutrients method
        List<String> filter = new ArrayList<>();

        /*
         * lambda expression that adds functionality to the addRule button so the user can apply
         * nutrients rules to the search query
         */
        addRule.setOnAction(e -> {
            rulesList.getChildren().clear(); // clears the current display to rewrite it

            // creates a rule of type string through concatenation according to what buttons are
            // selected in the GUI
            if (nutritionFilter.isSelected()) {
                rulesList.getChildren().add(ruleTitle);
                String rule = "";
                if (calories.isSelected()) {
                    rule = "calories ";
                } else if (carbs.isSelected()) {
                    rule = "carbohydrate ";
                } else if (fat.isSelected()) {
                    rule = "fat ";
                } else if (protein.isSelected()) {
                    rule = "protein ";
                } else if (fiber.isSelected()) {
                    rule = "fiber ";
                }

                // checks the comparitors
                if (lessThanEqualTo.isSelected()) {
                    rule = rule + "<= ";
                } else if (greaterThanEqualTo.isSelected()) {
                    rule = rule + ">= ";
                } else if (equalTo.isSelected()) {
                    rule = rule + "== ";
                }

                // adds the value inputed by user in GUI
                rule = rule + inputValue.getText();
                if (!filter.contains(rule)) {
                    filter.add(rule);
                }

                // add rules to Vbox in check box form to display in GUI
                for (int i = 0; i < filter.size(); i++) {
                    if (filter.get(i) != null) {
                        CheckBox temp = new CheckBox(filter.get(i).toString());
                        temp.getStyleClass().add("rules");
                        rulesList.getChildren().add(temp);
                    }
                }

            }
        });

        /*
         * lambda expression to add functionality to a button that removes selected rules from the
         * search rules list
         */
        clearRule.setOnAction(e -> {

            int i = 1;
            while (i < rulesList.getChildren().size()) {
                CheckBox temp = new CheckBox();
                temp = (CheckBox) rulesList.getChildren().get(i);

                if (temp.isSelected()) {

                    rulesList.getChildren().remove(i); // remove the filter check box from vbox
                    int k = 0;
                    while (k < filter.size()) {

                        // parse through rule list "filter" to find rule to remove
                        if (filter.get(k).equals(temp.getText())) {

                            filter.remove(k);// removes rule from rule list if its selected

                        }
                        k++;
                    }
                } else {
                    i++;// accounts for shift in size when item removed from Vbox
                }
            }


            rulesList.getChildren().clear(); // removes current rules
            rulesList.getChildren().add(ruleTitle); // adds "rules" title back

            // for loop parses through rules list and display the updated rules

            for (int j = 0; j < filter.size(); j++) {

                if (filter.get(j) != null) {
                    CheckBox temp = new CheckBox(filter.get(j).toString());
                    temp.getStyleClass().add("rules");
                    rulesList.getChildren().add(temp);
                }
            }

        });

        /*
         * lambda expression that adds functionality to the search button. Applies the the search
         * query to the with name and/or nutrients filtering.
         */
        search.setOnAction(e -> {

            results.getChildren().clear();
            String foodName = "";
            List<FoodItem> filterName = null;// results of search query using name
            List<FoodItem> filterNutrients = null; // results of search query using nutrients
            List<FoodItem> finalResults = new LinkedList<FoodItem>(); // combined results of name
                                                                      // and nutrients search

            foodName = inputAlphaSearch.getText();// name to search
            filterName = data.filterByName(foodName);// apply search by name

            if (nutritionFilter.isSelected()) {// only applies nutrition filter if the user checked
                                               // the box that they want it
                filterNutrients = data.filterByNutrients(filter);// applies filter by nutrients

                // creates a food item for each item in the search results
                for (int i = 0; i < data.getAllFoodItems().size(); i++) {
                    FoodItem cur = data.getAllFoodItems().get(i);

                    // checks if temporary item also matches nutrients search and removes from
                    // search results if doesn't
                    if (filterName.contains((Object) cur) && filterNutrients.contains((Object) cur))
                        finalResults.add(cur);
                }
            } else
                finalResults = filterName;// assign finalResults to master results list

            searchList = finalResults; // assign final results of applied filters to master search
                                       // list

            searchList = searchList.stream()// uses stream to sort the food list items search
                                            // results alphabetically by name
                .sorted((Object1, Object2) -> Object1.getName().compareTo(Object2.getName()))
                .collect(Collectors.toList());


            // if the results of the search are not null they are displayed
            if (searchList != null) {

                for (int i = 0; i <= searchList.size(); i++) {
                    if (i < searchList.size()) {
                        // creates a check box item for each search results to be displayed
                        CheckBox test = new CheckBox(searchList.get(i).getName());
                        results.getChildren().add(test);
                    }
                }

            } else {
                System.out.println("no results displayed"); // prints to the console that there were
                                                            // no matching search results
            }
        });

        

        /*
         * lambda expression that adds functionality to addToMenu button. This button adds the
         * selected check boxes from the search results to the menu list.
         */
        addToMenu.setOnAction(e -> {
            int i = 0;
            while (i < results.getChildren().size()) {

                CheckBox temp = (CheckBox) results.getChildren().get(i);
                // if the checkbox in the search result is selected its added to the menu list in
                // the form of another check box
                if (temp.isSelected()) {

                    temp.setSelected(false);
                    FoodItem itemToAdd = searchList.get(i);
                    

                    boolean duplicate = false;
                    
                    for(int j = 0; j < menuList.size(); j++) {
                    	if(menuList.get(j).getID().equals(itemToAdd.getID())) {
                    		duplicate = true;
                    		break;
                    	}
                    	
                    		
                    }
                    
                    
                    if(!duplicate) {
                    	menuList.add(itemToAdd);

                    	CheckBox menuItem = new CheckBox(itemToAdd.getName());
                        menuBox.getChildren().add(menuItem);
                    }
                    
                    

                } else {
                    i++;// adding items to menu affects the number of items in this list so accounts
                        // for this by not incrementing counter when object added to menu list
                }
            }

        });

        /*
         * lambda expression that adds functionality to the removeFromMenu button. This button
         * removes selected items from the menu list
         */
        removeFromMenu.setOnAction(e -> {
            int i = 0;
            // loops through the menu box looking for selected boxes
            while (i < menuBox.getChildren().size()) {
                CheckBox temp = (CheckBox) menuBox.getChildren().get(i);
                if (temp.isSelected()) {// if the box is selected remove it from the menu list

                    FoodItem itemToRem = menuList.get(i);
                    menuList.remove(itemToRem);

                    menuBox.getChildren().remove(i);
                } else {
                    i++;// adding items to menu affects the number of items in this list so accounts
                        // for this by not incrementing counter when object added to menu list
                }
            }
        });

        /*
         * Lambda expression that adds functionality to the addManul button. This button allows the
         * user to manually add a food item to food list. This button triggers a pop up with its own
         * functionality, see AddManuallyPopup.java
         */
        addManual.setOnAction(e -> {
            AddManuallyPopup.display();
        });

        /*
         * Lambda expression that adds functionality to the analyzeMeal button. This button loads
         * all the items from the menu list into a new list and sends it to the pop up to be
         * analyzed. See AnalyzeMealPopup.java
         */
        analyzeMeal.setOnAction(e -> {
            List<FoodItem> mealToAnalyze = new ArrayList<FoodItem>();
            
            
            for(int i = 0; i < menuList.size(); i++) {
            	
            
       
                FoodItem itemToAnalyze = menuList.get(i);
                mealToAnalyze.add(itemToAnalyze);
            }
            
            AnalyzeMealPopup.display(mealToAnalyze);
        });

    }


    /*
     * Main method that runs that launches the GUI which runs various program functionalities
     */

    public static void main(String[] args) {

        launch(args);

    }
}
