# Implementation

## UML class diagram for the app:
> **_NOTE:_**
> This is a prediction, the names and functions will change along with the progress in the app.

![image](readmeMaterials/boulderingApp.drawio.png)
## Brief explanation of the classes

> Fun fact, at 12/4/2024 there were written 1542 lines of code in total

In the **domain package** I have these following classes:
* **[AddImageApp](src/main/java/Bouldering_app/domain/AddImageApp.java)** -> Opens a gui interface to choose from your computer an Image, and we will **make a copy and move it to the images folder**.
* **[Ascent](src/main/java/Bouldering_app/domain/Ascent.java)** -> Contains a Route, datetime when is added and attempts (used further for liveGrade, attempts of a route, user profile stats).
* **[Climber](src/main/java/Bouldering_app/domain/Climber.java)** -> Important user (used in Sign-Up and Log in, creates ascents).
* **[EditImage](src/main/java/Bouldering_app/domain/editImage.java)** -> Opens a gui interface that opens an image specified, and add to it some arrows after a complex algorithms (well talk about it later).
* **[Grade](src/main/java/Bouldering_app/domain/Grade.java)** -> For each route we will have a grade (difficulty), each of them is part of a general grading with colors.
* **[ImageViewer](src/main/java/Bouldering_app/domain/ImageViewer.java)** -> Opens a gui interface that only shows an image specified.
* **[Password_hashing](src/main/java/Bouldering_app/domain/Password_hashing.java)** -> Passwords from sign-up are hashed, and we have a function for authenticate (verify string from log in with the hashed password).
* **[Route](src/main/java/Bouldering_app/domain/Route.java)** -> One of the primarily classes. A bouldering place consists of Routes.
* **[Setter](src/main/java/Bouldering_app/domain/Setter.java)** -> Like an admin for the app, can add routes and archive routes.
* **[User](src/main/java/Bouldering_app/domain/User.java)** -> This is the SuperClass from Route and Setter, it has only the name and password.

In the **services package** I have these following classes:

* **[RouteService](src/main/java/Bouldering_app/services/RouteService.java)** -> This service stores all the routes, so you can add routes, archive, choose a route (terminal view), show image of a route, add an ascent (for a climber)
* **[UserInteractionService](src/main/java/Bouldering_app/services/UserInteractionService.java)** -> An Interface for printing the profile of a user (climber or setter)
* **[UserService](src/main/java/Bouldering_app/services/UserService.java)** -> This service stores all the users, has the Sign-Up and Log In methods + show Ascents

The **[Main](src/main/java/Bouldering_app/Main.java)** class we have an unregistered main page, setter main page and a climber main page. For the current user logged in we use an Int (ID of the user) and we call in the Userservice and RouteService by this id.

## How the edit image feature is made?
* **[EditImage](src/main/java/Bouldering_app/domain/editImage.java)** -> This class is the most complex one. Firstly, It create a label to display the image using JLabel. 
The idea is to add arrows on the boulders of the ascent. In this way the users will know where to go.
Firstly we will create a new image using the Canny edge detector algorithm from OpenCV.
After that, we will wait for the user to press on the image, if it does, we want to add an arrow to the closest edge to the point.
To find the closest edge, we will do a BFS (from the point we will go UP DOWN LEFT RIGHT), like a virus that spread uniform on the image, untill it finds an edge.
Having the 2 points, we know the direction of the arrow, and we will draw it. (we can find the angle of the arrow by using the arctan function).
From this point, we play with angles to find how to draw the lines of the arrow.

## What happens when a climber adds an ascent?
* add Ascent to the database (with the route, datetime, attempts)
* Update the stats of the user (we will check the [importance](src/main/java/Bouldering_app/domain/Grade.java) of the Grade that the route has 
and in stats we use function update to make this formula: current stats + the importance of the route * the specific stat (strength, technique, etc.) / 10 )
* update the average grade of the user (Takes all the ascents of the user, and makes the average of the grades of the routes)
* update the attempts of the route (increment the attempts of the route)
* update the live grade of the route (takes all the ascents of the route, then look at the users grades, and makes the average of the grades of the users)

## Transition from Object structures to database storage
* Firstly I have created singleton classes that are handling queries with the database : [DatabaseAscent](src/main/java/Bouldering_app/databaseConnections/DatabaseAscent.java),
[DatabaseRoute](src/main/java/Bouldering_app/databaseConnections/DatabaseRoute.java),
[DatabaseUser](src/main/java/Bouldering_app/databaseConnections/DatabaseUser.java),
[DatabaseStats](src/main/java/Bouldering_app/databaseConnections/DatabaseStats.java).

* [Main](src/main/java/Bouldering_app/Main.java) wasn't changed, but the services classes were changed to use the database classes.
* We have a bunch of CRUD operations for the database, such as :
    * For **user**: create, read, update, delete
    * For **setter**: create, read, delete
    * For **climber**: create, read, update, delete
    * For **route**: create, read, update, delete
    * For **ascent**: create, read, delete
    * For **stats**: create, read, update, delete on cascade

* The hardest moment was when I have to add the ascent to the database, because I had to update the stats of the user, the attempts of the route, the liveGrade of the route, and the average grade of the user.
Also, the transition was one like 2 puzzle game: I have succeeded to add to database the User Setter and climber, and the rest of the code to work. Then I have slowly moved to routes and ascents.
