import java.util.Scanner;

public class Checkers {
	
	static Scanner input = new Scanner(System.in);

    static int mode;
    static char[][] board = new char[8][8];
    static int player;
    static int winner;

    static boolean huffing;
    static int[] lastPosition = new int[2];
    static boolean lastCaptured = false;
    

    public static void main(String[] args) {
    	
    	startScreen();

        resetBoard();

        boolean endGame = false;
        player = 1;
        
        do {
        	processTurn();
        	changeTurn();
        }
        while (!endGame);
        
        drawBoard();
        if (winner == 1) {
        	System.out.println("White is the winner.");
        }
        else {
        	System.out.println("Black is the winner.");
        }
    }

    
    private static void processTurn() {
    	boolean validMove = false;
    	String choice;
    	boolean wrong= false;
		do {
	        drawBoard();

			if (huffing) {
				System.out.println("Your opponent's piece was removed.");
			}
			else if(wrong) {
				System.out.println("That wasn't a valid move.");
			}
	        if(player == 1)
	        	System.out.println("It's white's turn.");
	        else
	        	System.out.println("It's black's turn.");

			wrong = false;
			System.out.println("Make your move in the format '([1..8],[1..8])  ([1..8],[1..8]) ...'");
			choice = input.nextLine();

			int[][] moveList = parseMove(choice);
			if (moveList.length < 2) { // Not valid
				wrong = true;
				continue;
			}
			validMove = processMovement(moveList);
			wrong = !validMove && !huffing;
		}
		while (!validMove);
	}


	static void changeTurn() { //DONE
    	if (player=='w') {
    		player = 'b';
    	}
    	else
    		player = 'w';
	}


	static void drawBoard() { // DONE
    	System.out.println("   1   2   3   4   5   6   7   8");
    	System.out.println(" _________________________________");
    	for (int i = 0; i < board.length; i++) {
    		System.out.print((i+1)+"|");
    		for (int j = 0; j < board[i].length; j++) {
        		System.out.print(" "+board[i][j]+" |");
    		}
    		System.out.println();
        	System.out.println(" |___|___|___|___|___|___|___|___|");
		}
	}


	static void startScreen() { // DONE
    	do {
	        System.out.println("Choose a game mode:\n" +
				"1) Basic mode\n" +
				"2) Intermediate mode\n" +
				"3) Advanced mode");
	        mode = input.nextInt();
	        input.nextLine();
    	}
    	while (!(1 <= mode && mode <= 3));
	}


	static void resetBoard() { // DONE
    	board = new char[][]{
    		{' ', 'b', ' ', 'b', ' ', 'b', ' ', 'b'},
    		{'b', ' ', 'b', ' ', 'b', ' ', 'b', ' '},
    		{' ', 'b', ' ', 'b', ' ', 'b', ' ', 'b'},
    		{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
    		{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
    		{'w', ' ', 'w', ' ', 'w', ' ', 'w', ' '},
    		{' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w'},
    		{'w', ' ', 'w', ' ', 'w', ' ', 'w', ' '},
    	};
    }


    static int validateFormat(String line) { // DONE
    	// return how many moves there are
    	if (line.length() < 11) { // Not enough characters
    		return 0; 
    	}

    	int n=0;
    	while (line.length()-n*6 >= 5) { // There are moves left to check
    		// Check all characters are correct
    		if (line.charAt(n*6) != '(' || line.charAt(n*6+1) < '1' || line.charAt(n*6+1) > '8' || line.charAt(n*6+2) != ',' || line.charAt(n*6+3) < '1' || line.charAt(n*6+3) > '8' || line.charAt(n*6+4) != ')') {
    			return 0;
    		}
    		n++;
    	}
    	if (line.length()-n*6 > 0) { // Leftover characters? We don't want that
    		return 0;
    	}
    	
    	return n; // There are n pairs of coordinates!
    }

    
    static int[][] parseMove(String line) { //DONE
    	int moveCount = validateFormat(line);
    	int[][] moves = new int[moveCount][2];

    	for (int i = 0; i<moves.length; i++) {
    		moves[i][0] = line.charAt(i*6+1)-'1';
    		moves[i][1] = line.charAt(i*6+3)-'1';
    	}

    	return moves;
    }

    static boolean processMovement(int[][] moveList) {
    	int[] start = moveList[0]; // First entry is the initial position

    	huffing = false;
    	lastCaptured = false;

    	if (playablePiece(start[0], start[1])!=1) {
    		if (mode == 3 && playablePiece(start[0], start[1])==2 && validateOneMovement(moveList[0], moveList[1])) {
    			if (lastPosition == start || (lastPosition != start && lastCaptured == false && playablePiece(moveList[1][0], moveList[1][1]) == 1)) {
    				board[lastPosition[0]][lastPosition[1]] = ' '; // manual huff
    				huffing = true;
    			}
    		}
    		return false;
    	}


    	boolean canMoveAgain = true;
    	for (int i = 0; i<moveList.length-1; i++) {
    		if (!canMoveAgain) {
    			return false;
    		}
    		if (!validateOneMovementSimulate(moveList[i], moveList[i+1], board[start[0]][start[1]])) {
    			return false;
    		}
    		if (playablePiece(moveList[i][0], moveList[i][1]) == -1) {
    			if (i<moveList.length-2) {
    				if(moveList[i+1][0] != (moveList[i+2][0]+moveList[i][0])/2 || moveList[i+1][1] != (moveList[i+2][1]+moveList[i][1])/2) {
    					return false;
    				}
    				lastCaptured = true;
    				i++;
    			}
    			else return false;
    		}
    		else canMoveAgain = false;
    		lastPosition = moveList[i+1];
    	}

    	board[lastPosition[0]][lastPosition[1]] = board[moveList[0][0]][moveList[0][1]];
    	for (int i = 0; i<moveList.length-1; i++) {
    		board[moveList[i][0]][moveList[i][1]] = ' '; // Remove intermediate steps
    	}

		if(mode>1) {
			if (color(lastPosition[0], lastPosition[1]) == 'w' && lastPosition[0] == 0) {
				board[lastPosition[0]][lastPosition[1]] = 'W';
			}else if (color(lastPosition[0], lastPosition[1]) == 'b' && lastPosition[0] == 7) {
				board[lastPosition[0]][lastPosition[1]] = 'B';
			}
		}

    	return true;
    }

    
    static boolean validateOneMovementSimulate(int[] position, int[] movement, char simulationPiece) { //DONE
    	char storedPiece = board[position[0]][position[1]];
    	board[position[0]][position[1]] = simulationPiece;
    	boolean isValid = validateOneMovement(position, movement);
    	board[position[0]][position[1]] = storedPiece;
    	return isValid;
    	
    }
    
    static boolean validateOneMovement(int[] position, int[] destination) {//DONE
    	if (board[position[0]][position[1]] == ' ') {
    		return false;
    	}
    	
		int[] path = {destination[0]-position[0], destination[1]-position[1]};

		if ((path[0] != 1 && path[0] != -1) || (path[1] != 1 && path[1] != -1)) {
			return false;
		}

		if (rank(position[0], position[1]) == 1) {
			if((color(position[0], position[1]) == 'w' && path[0] == 1) || (color(position[0], position[1]) == 'b' && path[0] == -1)) {
				return false;
			}
		}

		if (playablePiece(destination[0], destination[1]) == 1) {
			return false;
		}
		
		if (playablePiece(destination[0], destination[1]) == 2 && board[destination[0]+path[0]][destination[1]+path[1]] != ' ') {
			return false;
		}
		
		return true;
	}


    public static boolean checkWinner() {
    	
    	int movementsWhites = 0;
    	int movementsBlacks = 0;
    	for (int i=0; i<board.length; i++) {
    		for (int j=0; j<board[i].length; j++) {
    			int[] position = {i,j};
    			int[][] destinations = {
    					{i+1, j+1},
    					{i+1, j-1}, 
    					{i-1, j+1}, 
    					{i-1, j-1}, 
    			};
    			for (int k=0; k<destinations.length; k++) {
    				if (destinations[i][0] < 0 || destinations[i][0]>7 || destinations[i][1] < 0 || destinations[i][1]>7) {
    					continue;
    				}
        			if(validateOneMovement(position, destinations[i])) {
            			if (color(position[0], position[1]) == 'w') {
            				movementsWhites++;
            			}
            			else movementsBlacks++;
        			}
    			}
    		}
    	}
    	if (movementsWhites == 0) {
    		winner = 2;
    		return true;
    	}
    	else if (movementsBlacks == 0) {
    		winner = 1;
    		return true;
    	}
    	
    	return false;
    }

    public static int playablePiece(int i, int j) { //DONE
    	switch (board[i][j]) {
    	case 'w':
    	case 'W':
    		if (player==1) {
    			return 1;
    		}
    		else return -1;
    	case 'b':
    	case 'B':
    		if (player == 2) {
    			return 1;
    		}
    		else return -1;
		default:
			return 0;
    	}
    }


    public static int rank(int i, int j) { //DONE
    	char piece = board[i][j];
    	if (piece == 'w' || piece == 'b') {
    		return 1;
    	}
    	else if (piece == 'W' || piece == 'B') {
    		return 2;
    	}
    	else return 0;
    }
    
    public static char color(int i, int j) { //DONE
    	char piece = board[i][j];
    	if (piece == 'w' || piece == 'W') {
    		return 'w';
    	}
    	else if (piece == 'b' || piece == 'B') {
    		return 'b';
    	}
    	else return ' ';
    }

}