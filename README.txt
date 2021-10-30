QUICK SCHEDULER README
------------------------------
Application:    Quick Scheduler
Version:        0.1
Author:         Samuel Benning
Created For:    C195 - Software II Advanced Java Concepts

IDE:            IntelliJ IDEA 2021.1.1
Java Version:   Java SE 11.0.12
JavaFX SDK:     11.0.12
JDBC Driver:    mysql-connector-java-8.0.25

Date:           2021-10-26
Email:          sben180@wgu.edu


APPLICATION DESCRIPTION/PURPOSE
------------------------------
The purpose of the application is to provide basic appointment scheduling and updating functionality by adding, updating,
and deleting records from a MySQL database using a pre-defined database schema. The application allows the user to update,
add, and delete both appointment records and customer records and view the data in a graphical user interface (GUI). In
addition, a some sorting appointment filtering functionality is provided, as well as several pre-defined reports that
the user can generate.

Log-in functionality is also provided to verify user credentials. The log-in screen can also be translated into French
depending on system language settings.

HOW TO RUN
------------------------------
The program must be run from the IntelliJ IDE. For best compatibility, use 2021.1.1.
To set up the project, follow these steps:

Set up the MySQL Driver:
* Download mysql-connector-8.0.25. Can be obtained from https://dev.mysql.com/downloads/connector/j/
    Note: you must use 8.0.25. Using a different version can lead to unexpected behavior
* Add mysql connector to modules:
    Navigate to File -> Project Structure -> modules -> dependencies. Right click mysql-connector-8.0.25 and select edit.
    Click the + sign, navigate to the location of mysql-connector-8.0.25, and select it to add the file path. Remove the
    old file path.

Set up JavaFX:
* Install JavaFX SDK 11.0.12. Download from https://gluonhq.com/products/javafx/
* Follow the same steps to add JavaFX to modules as you did to add mysql connector.

Before running for the first time:
* Build Project (Ctrl + F9)
* Navigate to src/Main.java and enter Ctrl + Shift + F10 to run main method. The program should run
* After running for the first time, IntelliJ will be able to run normally.

ADDITIONAL REPORT
-----------------------------
Name of report: User database entries

The report provides a combo box list of all users stored in the database for the application user to choose from. After
making a selection, a Table View containing the Appointment_IDs and Titles of all appointments associated with that user
is generated and displayed in the Reports tab.
