
The following is a summary of assumptions this team made about the project description:

Load and Save File:

	It is assumed that the user will type in a file name of a ".csv" file in the working directory to load the food data into the program

	When the user hits "save" the file name including a file type like  ".csv" that is inputed by the user in the "File Name" text box will be the name of the save file created in the working directory and will over write any existing files by the same name

Nutrition filtering and rules:
	
	The user can add rules to the list of nutrition rules only if the nutrition filter toggle switch is on labeled "Nutrition Filter On?"
	
	The user can add conflicting rules such as "Fat == 3" and "Fat >= 4" and the search results will simply be empty as there are no food items that can match both these criterial

	check the check box of rules one wishes to remove and hit the "Clear Rules" button to remove them from the list of rules being applied to the search query

ADD Item

	From file: the "Add Item From File" loads a ".csv" file specified by the user in the "File Name" text box that is in the working directory

	Add manually: the "Add item Manually" button brings up a pop up that the user can input a food item manually once submitted via the popup the item is added to the backend data structure

Add and remove from menu

	The "Add Item To Menu" button adds the checked check boxes in the search results list to the menu without removing them from the search results

	The "Remove Item" button removes the check check boxes from the menu 
	
Add Item Manually
	This allows the user to manually add an item, however it will not be displayed in the search table immediately. It will appear in the search table (if it is supposed to) when "search" is pressed again.
	
Search
	To display all items in the database, just press "search" with nothing in the text field
