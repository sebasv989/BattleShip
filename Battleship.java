import java.util.*;

public class Battleship {

    private final char[][] board;  // Player's full board (shows ships)
    private final char[][] fogOfWar;  // Opponent's board (hides ships)
    private final int SIZE = 10; // Board size is 10x10

    private final Map<String, List<String>> ships; // Stores ships and their coordinates
    private final Set<String> alreadyShotCordinates; // Tracks already shot coordinates


    // Constructor initializes the game boards
    public Battleship() {
        board = new char[SIZE][SIZE];
        fogOfWar = new char[SIZE][SIZE];
        ships = new HashMap<>();
        alreadyShotCordinates = new HashSet<>();

        // Initialize both boards with '~' (fog of war symbol)
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '~';
                fogOfWar[i][j] = '~';
            }
        }
    }

    // Method to print the board
    public void printBoard(char[][] board) {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            char rowLabel = (char) ('A' + i); // Row labels from A to J
            System.out.print(rowLabel + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Parse coordinates like 'A1' into row and column indices
    public int[] parseCoordinates(String coord) {
        int row = coord.charAt(0) - 'A';  // Convert letter to row index
        int col = Integer.parseInt(coord.substring(1)) - 1;  // Convert number to column index
        return new int[]{row, col};
    }

    // Place all ships on the board for a given player
    private void placeAllShips(String playerName) {

        Scanner scanner = new Scanner(System.in);

        // Display the board for the player to place ships
        System.out.println(playerName + " , place your ships on the game field:\n");
        this.printBoard(this.board);

        // Define ships with their lengths
        String[][] ships = {{"Aircraft Carrier", "5"}, {"Battleship", "4"}, {"Submarine", "3"}, {"Cruiser", "3"}, {"Destroyer", "2"}};

        // Loop through each ship to place them on the board
        for (String[] ship : ships) {
            boolean placed = false;
            while (!placed) {
                System.out.println("\nEnter the coordinates of the " + ship[0] + " (" + ship[1] + " cells):\n");
                String start = scanner.next();
                String end = scanner.next();
                placed = this.placementValidation(start, end, Integer.parseInt(ship[1]), ship[0]); // Validate and place the ship
                if (placed) {
                    this.printBoard(this.board);// Print the updated board after placing the ship
                }
            }
        }

        // Clear the console after placing all ships
        this.clearConsole();

    }

    // Check if a ship is too close to other ships
    private boolean proximityCheck(int startRow, int startCol, int endRow, int endCol) {
        // Determine the range of rows and columns that the ship occupies
        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        int minCol = Math.min(startCol, endCol);
        int maxCol = Math.max(startCol, endCol);

        // Check the surrounding cells of the ship (including diagonally adjacent cells)
        for (int i = Math.max(0, minRow - 1); i <= Math.min(SIZE - 1, maxRow + 1); i++) {
            for (int j = Math.max(0, minCol - 1); j <= Math.min(SIZE - 1, maxCol + 1); j++) {
                if (board[i][j] == 'O') {
                    return true;  // Another ship is too close
                }
            }
        }

        return false;
    }

    // Validate and place a ship on the board
    public boolean placementValidation(String start, String end, int shipLength, String shipName) {
        int[] startCoord = parseCoordinates(start); // Parse start coordinates
        int[] endCoord = parseCoordinates(end); // Parse end coordinates

        int startRow = startCoord[0];
        int startCol = startCoord[1];
        int endRow = endCoord[0];
        int endCol = endCoord[1];

        // Check if coordinates are out of bounds
        if (startRow < 0 || startRow >= SIZE || startCol < 0 || startCol >= SIZE ||
                endRow < 0 || endRow >= SIZE || endCol < 0 || endCol >= SIZE) {
            System.out.println("\nError! Coordinates are out of bounds: " + start + " to " + end );
            return false;
        }

        // Check if the ship is placed horizontally or vertically (no diagonal ships allowed)
        if (startRow != endRow && startCol != endCol) {
            System.out.println("\nError! Ship must be placed horizontally or vertically: " + start + " to " + end );
            return false;
        }

        // Calculate the length of the ship
        int length = startRow == endRow ? Math.abs(endCol - startCol) + 1 : Math.abs(endRow - startRow) + 1;

        // Check if length matches the required ship length
        if (length != shipLength) {
            System.out.println("\nError! The " + shipName + " must be " + shipLength + " cells long, but provided length is " + length );
            return false;
        }

        // Check if the ship is placed too close to another ship
        if (proximityCheck(startRow, startCol, endRow, endCol)) {
            System.out.println("\nError! You placed it too close to another one. Try again: " + start + " to " + end );
            return false;
        }

        // Place the ship on the board
        List<String> shipCoordinates = new ArrayList<>();
        if (startRow == endRow) {  // Horizontal ship
            for (int i = Math.min(startCol, endCol); i <= Math.max(startCol, endCol); i++) {
                board[startRow][i] = 'O';  // Place ship on the player's board
                shipCoordinates.add("" + (char)('A' + startRow) + (i + 1));
            }
        } else {  // Vertical ship
            for (int i = Math.min(startRow, endRow); i <= Math.max(startRow, endRow); i++) {
                board[i][startCol] = 'O';  // Place ship on the player's board
                shipCoordinates.add("" + (char)('A' + i) + (startCol + 1));
            }
        }
        ships.put(shipName, shipCoordinates);

        return true;
    }

    // Check if a shot is valid based on the coordinates
    public boolean invalidShot(String coord) {

        int[] shotCoord = parseCoordinates(coord);
        int row = shotCoord[0];
        int col = shotCoord[1];

        // Check if the shot is out of bounds
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            System.out.println("Error! You entered wrong coordinates! Try again:");
            return false;
        }
        return true;
    }

    // Check if a specific ship has been sunk
    public boolean isSunk(String shipName) {
        return ships.get(shipName).isEmpty();
    }

    // Check if all ships have been sunk
    public boolean allShipsSunk() {
        for (List<String> coords : ships.values()) {
            if (!coords.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Take a shot and update the board accordingly
    public boolean takeShot(String coord) {
        int[] shotCoord = parseCoordinates(coord);
        int row = shotCoord[0];
        int col = shotCoord[1];

        // Check if the shot is out of bounds
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            System.out.println("Error! You entered wrong coordinates! Try again:");
            return false;
        }

        // Check if the shot hits a ship
        if (board[row][col] == 'O') {
            board[row][col] = 'X';  // Mark hit on the player's board
            fogOfWar[row][col] = 'X';  // Mark hit on the fog of war board

            for (Map.Entry<String, List<String>> entry : ships.entrySet()) {
                List<String> shipCoordinates = entry.getValue();
                if (shipCoordinates.contains(coord)) {
                    shipCoordinates.remove(coord);
                    System.out.println("\nYou hit a ship!");
                    if (isSunk(entry.getKey())) {
                        System.out.println("You sank a ship!");
                    }
                    break;
                }
            }
            return true; // Hit
        } else if (board[row][col] == '~') {
            board[row][col] = 'M';  // Mark miss on the player's board
            fogOfWar[row][col] = 'M';  // Mark miss on the fog of war board
            System.out.println("You missed!");
        }
        return true;
    }

    // Manage a player's turn
    public boolean playTurn(Battleship opponentPlayer, String playerName, Scanner scanner) {
        System.out.println(playerName + "\n, it's your turn:\n");
        printBoard(opponentPlayer.fogOfWar);// Display opponent's fog of war
        System.out.println("---------------------");
        printBoard(this.board); // Display player's own board

        while (true) {
            System.out.println("\nTake a shot!\n");
            String shot = scanner.next();


            boolean shotResult = opponentPlayer.takeShot(shot); // Try to take the shot
            boolean validShot = opponentPlayer.takeShot(shot); // Validate the shot

            if (!validShot) {
                System.out.println("Error! You entered wrong coordinates! Try again.");
                continue; // Ask for new coordinates if invalid
            }

            if (shotResult) {
                break; // Proceed if the shot was valid
            }
        }

        // Check if the player has sunk all the opponent's ships
        if (opponentPlayer.allShipsSunk()) {
            System.out.println("You sank the last ship. You won. Congratulations!");
            return true;
        }

        this.clearConsole(); // End the game
        return false; // Continue the game

    }

    // Clear the console between turns
    private void clearConsole() {
        System.out.println("Press Enter and pass the move to another player");
        new Scanner(System.in).nextLine(); // Wait for Enter key press

        System.out.println();
    }

    // Main method to run the game
    public static void main(String[] args) {
        Battleship player1 = new Battleship();
        Battleship player2 = new Battleship();
        Scanner scanner = new Scanner(System.in);

        // Players place their ships
        player1.placeAllShips("Player 1");
        player2.placeAllShips("Player 2");


        // Game loop to take shots
        boolean gameOver = false;
        while (!gameOver) {

            gameOver = player1.playTurn(player2, "Player 1", scanner); // Player 1's turn
            if (gameOver) break;

            gameOver = player2.playTurn(player1, "Player 2", scanner); // Player 2's turn

        }

        scanner.close(); // Close the scanner after the game ends
    }
}
