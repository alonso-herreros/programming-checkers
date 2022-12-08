import java.util.Scanner;

public class FinalProject {
	
	static Scanner input = new Scanner(System.in);

    static int mode;
    static char[][] board = new char[8][8];
    static int player = 0;

    static boolean huffing;

    public static void main(String[] args) throws InterruptedException {
    	
    	startScreen();

        resetBoard();

        drawBoard();

        boolean endGame = false;
        
        do {
        	processTurn();
        }
        while (!endGame);
        
        int endTurnCode;
        do {
	        Renderer.renderBoard(board);
	        String input = Prompter.promptTurn(playing);
    		endTurnCode = 1;

        	while (endTurnCode == 1) {
    	        switch (validateInput(input)) {
    	        case 1:  // Move!
    	        	int moveCode = move(input);
    	        	if (moveCode == 0) {
    	        		endTurnCode = 2; // Next turn
    	        	}
    	        	else { // Error codes are negative
    	        		input = Prompter.promptInvalid(-moveCode);
    	        	}
    	        	break;
    	        case 2:  // Huff
            		Prompter.promptHuff(playing, huff());
            		endTurnCode = 3; // Exit validation loop, but don't change player
            		break;
    	        case 3:  // Surrender
    	        	if (Prompter.promptSurrender()) {
    	        		endTurnCode = -opponent();
        	        	break;
    	        	}
    	        	endTurnCode = 3;
    	        	break;
    	        case 8:  // How to play
    	        	input = Prompter.promptHtp();
    	        	break;
    	        case 16: // Exit
    	        	if (Prompter.promptExit()) {
    	            	Prompter.promptEnd();
    	        		return;
    	        	}
    	        	endTurnCode = 3;
    	        	break;
    	        case -1: // No input: try again
    	        	endTurnCode = 3;
    	        	break;
    	        case -2: // Invalid input
    	        	input = Prompter.promptInvalid();
    	        	break;
    	        }
    	        if (endTurnCode > 0) { // If no other exit code is detected
    	        	endTurnCode -= checkWinner();
    	        }
    	        if(checkStall()) { // -2 is a draw
    	        	endTurnCode = Prompter.promptStall()? -2 : endTurnCode;
    	        }
        	}
        	if (endTurnCode == 2) {
            	playing = opponent();
        	}
        }
        while (endTurnCode > 0); 

        Renderer.renderBoard(board);
    	Prompter.promptEnd(-endTurnCode);
    }

    
    private static void processTurn() {
    	boolean endTurn = false;
    	boolean validChoice = false;
    	String choice;
    	do {
    		do {
				choice = input.nextLine();
				int[][] moveList = parseMove(choice);
				if (moveList.length < 2) { // Not valid
					continue;
				}
				validChoice = processMovement(moveList);
    		}
    		while (!validChoice);
    	}
    	while (!endTurn);
	}


	static void nextTurn() { //DONE
    	if (player=='w') {
    		player = 'b';
    	}
    	else
    		player = 'w';
	}


	static void drawBoard() { // DONE
    	System.out.println("    1   2   3   4   5   6   7   8");
    	System.out.println("  _________________________________");
    	for (int i = 0; i < board.length; i++) {
    		System.out.print(i+"|");
    		for (int j = 0; j < board[i].length; j++) {

        		System.out.print(" "+board[i][j]+" |");
    		}
    		System.out.println();
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
    	while (!(1 < mode && mode < 3));
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

    	if (playablePiece(start[0], start[1])!=1) {
    		return false;
    	}
    	
    	//TODO:
    	//huffing = moveChoiceCount(playing)[1]>0? 1:0;

    	for (int i = 1; i<moveList.length; i++) {
    		
    		if (validateOneMovement(moveList[i], moveList[i+1])) {
    			
    		}
    		
    		int[] move = moves[i];
    		int code = validateMove(pos, move, piece);
    		switch (code) {
    		case 2: // Add the midpoint to the capture list
    			captured[c++] = new int[] {(pos[0]+move[0])/2, (pos[1] + move[1])/2};
    			condition = 1; // Allow next move
    			huffing = 0; // If you just captured, huffing flag is reset
    			break;
    		case 5: // Moving into an enemy piece
    			condition = 2; // Next move must be a capture move
    			continue;
			default:
				condition = 0; // Don't allow further movement
				if (code<0) { // Error codes
					return code;
				}
    		}
    		
    		pos = move;
    		huffing = availableMoveCount(pos, piece)[1]>0? 1:huffing;
    		
			if(mode>1 && move[0] == (playing==0? 0 : 7)) {
				piece += 2; // Become king
			}
    	}

    	if (condition == 2) { // Pending capture
    		return -25; // ERR: Occupied. Moved into an enemy piece without capturing it
    	}
    	// Changes to the board are only made here, after passing all error checks.
    	for (int i = 0; i<c; i++) {
    		board[captured[i][0]][captured[i][1]] = 0; // Remove captured pieces
    	}
    	 = 0; // Remove piece from original location
    	board[moveList[0]][moveList[1]] = board[moveList[0][0]][moveList[0][1]]; // Place piece at final locattion

    	// Update last play
    	lastPlayedPiece = new int[][] {pos,{huff,0}};

    	return 0;
    }

    
    static boolean validateOneMovementSimulate(int[] position, int[] movement, char simulationPiece) {
    	char storedPiece = board[position[0]][position[1]];
    	board[position[0]][position[1]] = simulationPiece;
    	boolean isValid = validateOneMovement(position, movement);
    	board[position[0]][position[1]] = storedPiece;
    	return isValid;
    	
    }
    
    static boolean validateOneMovement(int[] pos, int[] move) {
		int[] path = {move[0]-pos[0], move[1]-pos[1]};
	
		if ((path[0] != 1 && path[0] != -1) || (path[1] != 1 && path[1] != -1)) {
			return false;
		}
		if (rank(pos[0], pos[1]) == 1) {
			if (path[j]!=)
			return -17; // ERR: Backwards movement
		}
		
		switch (Math.abs(rel[0])) {
		case 0:
			return -11; // ERR: No movement
		case 1:
			switch (ownerRel(pieceAt(move), ownerOf(piece))) {
			case -1:
				return 0; // Regular movement
			case 0:
				return -25; // ERR: Occupied
			case 1:
				if (pieceAt(pos[0]+rel[0]*2, pos[1]+rel[1]*2) == 0) {
					return 5; // Possible capture move (moving into piece)
				}
				else return -25; // ERR: Occupied
			}
		case 2:
			if (pieceAt(move) != 0) {
				return -25; // ERR: Occupied
			}
			switch (ownerRel(pieceAt(pos[0]+rel[0]/2,pos[1] + rel[1]/2), ownerOf(piece))) {
			case -1:
				return -12; // ERR: Capturing empty = too far
			case 0:
				return -27; // ERR: Capturing your own 
			case 1:
				return 2;   // Capture
			}
		default: // Anything bigger than 2
			return -12; // ERR: Way too far
		}
	}


	static boolean huff() {
    	if (mode < 3) {
    		return false;
    	}
    	
    	if (lastPlay[1][0]==1) { // It's a huffing flag that was set during the last move
    		board[lastPlay[0][0]][lastPlay[0][1]] = ' '; // Offending piece removed.
    		return true;
    	}
    	return false;
    }

    static boolean checkStall() {
    	for (int i=3; i<boardHistory.length; i++) {
    		if (java.util.Arrays.deepEquals(board, boardHistory[i])) {
    			return true;
    		}
    	}
    	return false;
    }

    public static int checkWinner() {
    	int[] choiceCounts = {moveChoiceCount(0)[0], moveChoiceCount(1)[0]};
    	return choiceCounts[0]==0? 2 : choiceCounts[1]==0? 1 : 0;
    }

    static int[][][] moveChoices(int player) {
    	int[][][] choicesTmp = new int[12][2][2];
    	int m = 0; // Counter for movable pieces
    	for (int i=0; i<board.length; i++) {
    		for (int j=0; j<board[i].length; j++) {
    			if(ownerOf(pieceAt(i,j)) == player) {
        			int[] moveCount = availableMoveCount(i,j);
        			if (moveCount[0] != 0) {
        				choicesTmp[m++] = new int[][] {{i,j}, moveCount};
        			}
    			}
    		}
    	}
    	int[][][] choices = new int[m][2][2];
    	for (int i=0; i<m; i++) {
    		choices[i] = choicesTmp[i];
    	}
    	return choices;
	}

    static int[] moveChoiceCount(int player) {
    	int[][][] choices = moveChoices(player);
    	int maxCaptures = 0;
    	for (int i=0; i<choices.length; i++) {
    		maxCaptures = Math.max(maxCaptures, choices[i][1][1]);
    	}
    	return new int[] {choices.length, maxCaptures};
    }

    static int[][][] availableMoves(int row, int col) {
    	return availableMoves(new int[] {row, col});
    }
    static int[][][] availableMoves(int[] pos) {
    	int[][][] movesTmp = new int[4][2][2]; // Max number of available moves is 4
    	int k = 0; // Counter for valid moves
    	for (int i=-1; i<2; i+=2) {
    		for (int j=-1; j<2; j+=2) {
    			int[] move = {pos[0]+i, pos[1]+j};
    			switch (validateMove(pos, move)) {
    			case 5: // Could capture if it jumped over this piece
    				move[0] += i;
    				move[1] += j;
    				// The capture indicator is the max amout of captures going this route
    				movesTmp[k][1][0] += 1+availableMoveCount(move, pieceAt(pos))[1];
    			case 0: // Good enough move
    				movesTmp[k++][0] = move;
    			} // Ignore error cases
    		}
    	}
    	int[][][] moves = new int[k][2][2];
    	for (int i=0; i<k; i++) {
    		moves[i] = movesTmp[i];
    	}
    	return moves;
	}

    static int[] availableMoveCount(int row, int col) {
    	return availableMoveCount(new int[] {row,col});
    }
    static int[] availableMoveCount(int[] pos) {
    	int[][][] moves = availableMoves(pos);
    	int maxCaptures = 0;
    	for (int i=0; i<moves.length; i++) {
    		maxCaptures = Math.max(maxCaptures, moves[i][1][0]);
    	}
    	return new int[] {moves.length, maxCaptures};
    }
    static int[] availableMoveCount(int[] pos, byte piece) {
    	// TODO: This works, but it's just so wrong...
    	byte realPiece = pieceAt(pos); // should be 0 every time, but just in case
    	board[pos[0]][pos[1]] = piece; 
    	int[] moveCount = availableMoveCount(pos);
    	board[pos[0]][pos[1]] = realPiece;
    	return moveCount;
    }

    public static int playablePiece(int i, int j) { //DONE
    	switch (board[i][j]) {
    	case 'w':
    	case 'W':
    		if (player=='w') {
    			return 1;
    		}
    		else return -1;
    	case 'b':
    	case 'B':
    		if (player == 'b') {
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