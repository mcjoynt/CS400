# cs400_final_project
* See asignment details in [canvas](https://canvas.wisc.edu/courses/121155/pages/program-design-problem-statement-food-query-and-meal-analysis)
* [Submit Page](https://canvas.wisc.edu/courses/121155/assignments/367238)

## Helpful Resources
* GUI CSS Javadocs
  * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html
  
## Team
* Austin Muschott Git: badgerbricker Email: muschott@wisc.edu
* David Waltz Git: dwaltzCS Email: dwaltz@wisc.edu
* Dominic Bourget Git: dominicbourget Email: dbourget@wisc.edu 
* Mathew McJoynt Git: mcjoynt Email: mmcjoynt@wisc.edu
* Megan Stoffel Git: mstoffel2 Email: mstoffel2@wisc.edu

## Set up git from Eclipse
1. Using "git bash" command line (needs to be installed on windows) or "Terminal" on Mac/Linux navigate to your eclipse work place and create a new git repo (command: "git init")
1. clone the final project (command: git clone git@github.com:badgerbricker/cs400_final_project.git)
1. Navigate to Project Explorer
1. Right click in project explorer->import
1. Choose "Git"
1. Add local repository you created in command line should be titled "cs400_final_project"
1. click through the setup menu until you see the option to "create with new project wizard" and select that
1. pick "JavaFX Project" from the list
1. Click "Finish"

You should now see all the git files in your project explorer, they might be inside another folder titled "cs400_final_project".

To push to git just use git bash command line on windows or Terminal on Mac

Read more on git and git commands here: [Git Guide](http://git.huit.harvard.edu/guide/)

## Make file
Make file works but you have to run it in the *parent directory* of the "cs400_fina_project" directory.

creates a zip and executable file for the program to run independent of the workspace. Includes any ".txt" ".css" ".class" and ".csv" files in the zip.

Commands:
* make nosrc -this compiles everything and packages it into an executable
* make run -this runs the program and also saves it to a zip file named food.zip
* make clean -this clears all the .class files from the current directory, very helpful to keep things neat once everything is compiled

The following commands have not been implemented correctly but arent necessary so long as your work is not in separate source and bin folders
* cleanbin -cleans the .class files in the "bin" file
* srcbin -compiles and creates an executable file when the user has files in separate bin and source folders
