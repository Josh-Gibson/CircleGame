# CircleGame
Simple circle game simulation thing


Copyright © 2021 JoshGibsonGames

Welcome to this little circle simulation demo! This isn't as much of a game as it is a screensaver. There are no controls, and all you really
do is watch the balls bounce around.

==How to Build ==
Go to CircleGame.java and with cmd execute these commands

- javac -d . *.java (Compiles all java files into classes)
- java jggames.CircleGame (runs Circlegame.class and starts the game)

For jar file, run:
- jar cmfv CircleGame.mf  CircleGame.jar jggames/*.class (creates jar
- java -jar CircleGame.jar (run the .jar file)


== How to Run ==

Click on CircleGame.jar. If there is trouble running, files may have been misplaced or java version is incompatible. See IMPORTANT_INFO.txt for more help.


== How to Play ==

The circles are self propelling. Set up a scenerio in options.txt and run the simulation, see how it goes. There are two main gamemodes:
Extinction and Elimination.

#Please write these in all lowercase or else the game won't work.


== Extinction ==

In this gamemode, all the circles are created with a color. If two circles touch, one randomly wins the "battle," and puts their color and ID on the other circle.
The game is over when all circles are one color. At the end of the match, a list will be printed that contains all of
the circles, and whether they survived or died.


== Elimination ==

In this gamemode, all the circles are created with a unique color and ID. If two circles touch, one randomly wins the "battle," and kills the other circle. 
The dead circle stops moving. The game ends when there is only one circle remaining. At the end of the match, a list will be printed that contains all of
the circle's kills, and whether they survived or died.



