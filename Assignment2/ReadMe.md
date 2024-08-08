1.Compile your Java files into bytecode. This can be done using the javac command in your terminal. For example, if your main class is in GA.java, you would use:
javac GA.java

2.Create a manifest file. This tells the JAR file which class contains the main method to run. You can create a text file named Manifest.txt with the following content:
Main-Class: GA

3.Create the JAR file. You can use the jar command for this. If your compiled class file is GA.class and your manifest file is Manifest.txt, you would use:
jar cvfm GA.jar Manifest.txt GA.class

4.Now you can run your JAR file using the java command:
java -jar GA.jar <input_file>

Please note that this process assumes that all of your .java files are in the same directory and that you're running these commands from that directory. If your project has a different structure, you may need to adjust these commands accordingly.

FOR GA.java run this is the terminal:
1. javac GA.java
2. jar cvfe GA.jar GA GA.class
3. java -jar GA.jar <input_file>

clean directory:
rm *.class GA.jar

for the GA_SA.java
change the Manifest.txt to: Main-Class: GA_SA
run these commands:
1. javac GA_LS.java
2. jar cvfe GA_LS.jar GA_LS GA_LS.class
3. java -jar GA_LS.jar f1_l-d_kp_10_269
