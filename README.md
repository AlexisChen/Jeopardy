# Jeopardy


This java program implements a Jeopardy game.
-using multi-threading and networking for game communication.


Deploy:

import the folder:

	src->main->Main.java to start the game


log in:	
	-user need to first register for a username and password
	-if the user has done so, he/she needs to be logged in first
	
game mode:
	-single play: player will play by himself/herself
	-host a game: player wait for other players to join his/her game
	-join a game: player can choose to join game hosted by others.
	
game play:
	players will take turns to answer questions on the question board
	a correctly answered question will add points to the player
	an incorrectly answered question will cause points deduction.
	
	-the player to be choose questions first is picked randomly. 
	-when a player choose a question, there will be a count down
	-other player won't be able to answer question during this period
	-when a player answer the question wrong, 
	there will be a buzz in period for all other players,
	player who presses buzz in first will be the next to answer the question.
	-if all players answer the question incorrectly, 
	they will be taken to the main gameplay board
	
	after all the questions have been answered, there will be a final jeopardy question:
	-players can bet the points from 1 to the points they have till this time,
	-after all players have bet and answered the question,
	the answer will show on the screen and final scores will be calculated.
	
	players can choose to exit, log out, from the tool bar at the top.
	
	
