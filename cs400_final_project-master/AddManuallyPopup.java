import javafx.scene.shape.Rectangle;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.stage.Popup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
/**
 * Filename:   AddManuallyPopup.java
 * Project:    Food Query and Meal Analysis
 * Course:     cs400 
 * Authors:    Dominic Bourget, David Waltz, Austin Muschott, Megan Stoffel, Matthew McJoynt
 * Due Date:   Wed. Dec 12 10 pm
 * 
 *
 */

public class AddManuallyPopup {

	public static void display() {
		TextField name = new TextField();
		TextField ID = new TextField();
		TextField calories = new TextField();
		TextField carbs = new TextField();
		TextField protein = new TextField();
		TextField fiber = new TextField();
		TextField fat = new TextField();
		Label labelName = new Label("Name:");
		Label labelID = new Label("ID:");
		Label labelCalories = new Label("Calories:");
		Label labelCarbs = new Label("Carbohydrates:");
		Label labelProtein = new Label("Protein:");
		Label labelFiber = new Label("Fiber:");
		Label labelFat = new Label("Fat:");
		
		Button addItemBtn = new Button("Submit");
		addItemBtn.getStyleClass().add("buttonStyle3");	
        
		// Start of GridPane
		GridPane gridPane = new GridPane();
		ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50);
        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(leftColumn, rightColumn);
      
        RowConstraints topHeader = new RowConstraints();
        topHeader.setPercentHeight(20);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(65);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(15);
        gridPane.getRowConstraints().addAll(topHeader, row1, row2);
        
        Rectangle header = new Rectangle(600, 120);
        header.getStyleClass().add("middlePane");
        gridPane.add(header, 0, 0, 1, 1);
        
        Rectangle main = new Rectangle(600, 320);
        main.getStyleClass().add("leftPane");
        gridPane.add(main, 0, 1, 1, 2);
        
//        gridPane.setStyle("-fx-grid-lines-visible: true");// for debugging only	
		
        // Title of popup
        Label title = new Label("Add Food Item Manually");
        title.getStyleClass().add("header");
        gridPane.add(title, 0, 0, 2, 1);
        GridPane.setHalignment(title, HPos.CENTER);
        
        // Vbox for left side
        VBox vboxLeft = new VBox();
        vboxLeft.getStyleClass().add("vbox");
        vboxLeft.setId("vbox-manual");
        
        vboxLeft.getChildren().addAll(labelName, name, labelCalories, calories, labelProtein, protein, labelFat, fat);
        gridPane.add(vboxLeft, 0, 1, 1, 2);
        
        // Vbox for right side 
        VBox vboxRight = new VBox();
        vboxRight.getStyleClass().add("vbox");
        vboxRight.setId("vbox-manual");
        
        vboxRight.getChildren().addAll(labelID, ID, labelCarbs, carbs, labelFiber, fiber, addItemBtn);
        gridPane.add(vboxRight, 1, 1, 1, 1);
        
        // Add button to gridpane
        gridPane.add(addItemBtn, 0, 2, 2, 1);
        GridPane.setHalignment(addItemBtn, HPos.CENTER);
        
        
        gridPane.setStyle("application.css");
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Add Item Manually");
		Scene popup = new Scene(gridPane, 600, 400);
		
		popup.getStylesheets().add("application.css");
        primaryStage.setScene(popup);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        /////////Event Handlers/////////
		addItemBtn.setOnAction(e -> {
			boolean validForm = true;
			
			// validation checks for fields 
			if (name.getText().trim().isEmpty()) {
				validForm = false;
				name.getStyleClass().add("text-field-error");
			}
			if (ID.getText().trim().isEmpty()) {
				validForm = false;
				ID.getStyleClass().add("text-field-error");
			}
			if (calories.getText().trim().isEmpty()) {
				validForm = false;
				calories.getStyleClass().add("text-field-error");
			}
			if (carbs.getText().trim().isEmpty()) {
				validForm = false;
				carbs.getStyleClass().add("text-field-error");
			}
			if (protein.getText().trim().isEmpty()) {
				validForm = false;
				protein.getStyleClass().add("text-field-error");
			}
			if (fiber.getText().trim().isEmpty()) {
				validForm = false;
				fiber.getStyleClass().add("text-field-error");
			}
			if (fat.getText().trim().isEmpty()) {
				validForm = false;
				fat.getStyleClass().add("text-field-error");
			}
			// Checking for a valid form
			if (!validForm) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("Please fill out all information. "
						+ "Information missing is highlighted in red.");
				errorAlert.showAndWait();
			}else {
				double caloriesTotal = Double.parseDouble(calories.getText());
				double carbsTotal = Double.parseDouble(carbs.getText());
				double proteinTotal = Double.parseDouble(protein.getText());
				double fiberTotal = Double.parseDouble(fiber.getText());
				double fatTotal = Double.parseDouble(fat.getText());

				FoodItem newFoodItem = new FoodItem(ID.getText(), name.getText());
				newFoodItem.addNutrient("calories", caloriesTotal);
				newFoodItem.addNutrient("carbohydrate", carbsTotal);
				newFoodItem.addNutrient("protein", proteinTotal);
				newFoodItem.addNutrient("fiber", fiberTotal);
				newFoodItem.addNutrient("fat", fatTotal);
				
				
					
					 // make sure an item isn't added twice
	                boolean duplicate = false;
	                
	                for(int i = 0; i < GUI.data.getFoodItemList().size(); i++) {
	                	if(GUI.data.getAllFoodItems().get(i).getID().equals(newFoodItem.getID())) {
	                		duplicate = true;
	                		break;
	                	}
	                		
	                }
	                if(!duplicate)
	    				GUI.data.addFoodItem(newFoodItem);

	                
				
				
				// Close window after submit is pressed
	            
				primaryStage.close();
			}
		});
	}
}
