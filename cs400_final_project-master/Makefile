run:
	java -jar executable.jar
	jar -cMf food.zip .


nosrc: cs400_final_project/GUI.java
	javac -d . cs400_final_project/*.java
	jar -cfm executable.jar manifest.txt *.class *.txt application.css test.csv


srcbin: src/cs400_final_project/GUI.java
	javac -d bin src/cs400_final_project/*.java
	jar -cfm executable.jar manifest_srcbin.txt -C bin cs400_final_project

clean:
	\rm *.class

cleanbin:
	\rm bin/cs400_final_project/*.class


