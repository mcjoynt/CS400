import javafx.scene.shape.Rectangle;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * Filename:   AnalyzeMealPopup.java
 * Project:    Food Query and Meal Analysis
 * Course:     cs400 
 * Authors:    Dominic Bourget, David Waltz, Austin Muschott, Megan Stoffel, Matthew McJoynt
 * Due Date:   Wed. Dec 12 10 pm
 * 
 *
 */

public class AnalyzeMealPopup {
	
	public static void display(List<FoodItem> mealToAnalyze) {
		String totalMeal = "";
		Double totalCalories = 0.0;
		Double totalCarbs = 0.0;
		Double totalProtein = 0.0;
		Double totalFiber = 0.0;
		Double totalFat = 0.0;
		
		for (int i = 0; i < mealToAnalyze.size(); i++) {
			FoodItem test = mealToAnalyze.get(i);
			if (i == mealToAnalyze.size()-1) {
				totalMeal += test.getName();
				totalCalories += test.getNutrients().get("calories");
				totalCarbs += test.getNutrients().get("carbohydrate");
				totalProtein += test.getNutrients().get("protein");
				totalFiber += test.getNutrients().get("fiber");
				totalFat += test.getNutrients().get("fat");
			}else {
				totalMeal += test.getName() + ", ";
				totalCalories += test.getNutrients().get("calories");
				totalCarbs += test.getNutrients().get("carbohydrate");
				totalProtein += test.getNutrients().get("protein");
				totalFiber += test.getNutrients().get("fiber");
				totalFat += test.getNutrients().get("fat");
			}
		}
		
		Label labelMeal = new Label("Nutritional Facts: "); 
		Label labelCalories = new Label("Total Calories: " + totalCalories);  
		Label labelCarbs = new Label("Total Carbohydrates: " + totalCarbs);
		Label labelProtein = new Label("Total Protein: " + totalProtein);
		Label labelFiber = new Label("Total Fiber: " + totalFiber);
		Label labelFat = new Label("Total Fat: " + totalFat);
		
		GridPane gridPane = new GridPane();
		ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50);
        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(leftColumn, rightColumn);
      
        RowConstraints topHeader = new RowConstraints();
        topHeader.setPercentHeight(20);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(20);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(60);
        gridPane.getRowConstraints().addAll(topHeader, row1, row2);
        
        //gridPane.setStyle("-fx-grid-lines-visible: true");// for debugging only

        
        Rectangle header = new Rectangle(500, 60);
        header.getStyleClass().add("middlePane");
        gridPane.add(header, 0, 0, 1, 1);
        
        Rectangle main = new Rectangle(500, 200);
        main.getStyleClass().add("leftPane");
        gridPane.add(main, 0, 1, 1, 2);
        
        // Title of popup
        Label title = new Label("Analyze Meal Results");
        title.getStyleClass().add("header");
        gridPane.add(title, 0, 0, 1, 1);
        GridPane.setHalignment(title, HPos.CENTER);
        
        // Vbox for items in meal 
        VBox vboxTop = new VBox();
        vboxTop.getStyleClass().add("vbox");
        vboxTop.setId("vbox-analyze");
        
        vboxTop.getChildren().addAll(labelMeal);
        gridPane.add(vboxTop, 0, 1, 2, 1);
        
        // Vbox for left side
        VBox vboxLeft = new VBox();
        vboxLeft.getStyleClass().add("vbox");
        vboxLeft.setId("vbox-analyze");
        
        vboxLeft.getChildren().addAll(labelCalories, labelCarbs, labelProtein);
        gridPane.add(vboxLeft, 0, 2, 1, 1);
        
     // Vbox for right side 
        VBox vboxRight = new VBox();
        vboxRight.getStyleClass().add("vbox");
        vboxRight.setId("vbox-analyze");
        
        vboxRight.getChildren().addAll(labelFiber, labelFat);
        gridPane.add(vboxRight, 1, 2, 1, 1);
     
		Stage popup = new Stage();
		Scene scene = new Scene(gridPane, 500, 250);
		scene.getStylesheets().add("application.css");
		
		popup.setScene(scene);
		popup.setResizable(false);
		popup.showAndWait();    
	}
}
