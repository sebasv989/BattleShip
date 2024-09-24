Battleship Game - PvP Edition

This is a command-line implementation of the classic Battleship game, allowing two players to engage in a strategic battle. Each player places their ships on their own 10x10 grid and then takes turns guessing the location of the opponent's ships. The goal is to sink all of the opponent's ships before they sink yours!

Features

PvP Gameplay: Two players compete against each other, taking turns to attack.
Ship Placement: Players place 5 ships of varying lengths (Aircraft Carrier, Battleship, Submarine, Cruiser, Destroyer) on their grid.
Real-Time Feedback: Players receive immediate feedback after each shot—hit, miss, or ship sunk.
Fog of War: Opponent's ships are hidden until hit, providing a strategic element to the game.
Turn-Based Play: The console clears between each turn to prevent players from seeing the opponent's board.
Victory Condition: The game ends when one player sinks all of the opponent's ships, printing a winning message.

How to Play

Both players place their ships on a 10x10 grid by entering the start and end coordinates for each ship.
Players take turns to shoot at the opponent's grid by specifying a coordinate (e.g., A5).
The game will display hits, misses, and the state of both grids during each turn.
The first player to sink all the opponent's ships wins the game.

Technologies Used

Java: Core language used for game logic and structure.
Command-line Interface: Simple and interactive console-based UI for the game.
