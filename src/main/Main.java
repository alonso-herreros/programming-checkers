package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static byte[][] STARTING_BOARD = {
    		{0,2,0,2,0,2,0,2},
    		{2,0,2,0,2,0,2,0},
    		{0,2,0,2,0,2,0,2},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{1,0,1,0,1,0,1,0},
    		{0,1,0,1,0,1,0,1},
    		{1,0,1,0,1,0,1,0}
    };

    static int mode;
    static byte[][] board = new byte[8][8];
    static byte[][][] boardHistory = new byte[4][8][8];
    static String[] names = new String[2];
    static int playing = 0;

    static int[][] lastPlay = new int[2][2];


    public static void main(String[] args) throws InterruptedException {
    	
    	do {
	        Renderer.renderTitle();
	        mode = Integer.valueOf(Prompter.promptStart());
    	}
    	while (mode<1 || 3<mode);

        Renderer.clear();
        names = Prompter.promptNames();
        
        Renderer.animateCoinFlip();
        byte coinFlip = randomizeStartingPlayer();
        Prompter.setNames(names);
        Prompter.promptPlay(coinFlip, playing);
        
        board = STARTING_BOARD.clone();

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


    static int move(String moveStr) {
    	int[][] moves = parseMove(moveStr); // Parse input
    	int[] pos = moves[0]; // Position starts at first entry
    	byte piece = pieceAt(pos); // Store type of piece

    	switch (ownerRel(piece, playing)) {
    	case -1: return -2; // No piece (or out of bounds)
    	case 1:  return -3; // Not your piece
    	}

    	int[][] captured = new int[moves.length-1][2]; // Max amount of captures is one per move
    	int c = 0; // Captured count
    	
    	// Huffing flag
    	int huff = moveChoiceCount(playing)[1]>0? 1:0;

    	// When moving into an enemy piece, next move must be capture
    	int condition = 1; // Allow 

    	for (int i = 1; i<moves.length; i++) {
    		if (condition == 0) { // No more moves
    			return -4; // Too many moves
    		}
    		
    		int[] move = moves[i];
    		int code = validateMove(pos, move, piece);
    		switch (code) {
    		case 2: // Add the midpoint to the capture list
    			captured[c++] = new int[] {(pos[0]+move[0])/2, (pos[1] + move[1])/2};
    			condition = 1; // Allow next move
    			huff = 0; // If you just captured, huffing flag is reset
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
    		huff = availableMoveCount(pos, piece)[1]>0? 1:huff;
    		
			if(mode>1 && move[0] == (playing==0? 0 : 7)) {
				piece += 2; // Become king
			}
    	}

    	if (condition == 2) { // Pending capture
    		return -25; // ERR: Occupied. Moved into an enemy piece without capturing it
    	}

    	// Save board history
    	for (int i=boardHistory.length-1; i>0; i--) {
    		boardHistory[i] = boardHistory[i-1];
    	}
    	boardHistory[0] = new byte[8][8];
    	for (int row=0; row<board.length; row++) {
    		boardHistory[0][row] = board[row].clone();
    	}

    	// Changes to the board are only made here, after passing all error checks.
    	for (int i = 0; i<c; i++) {
    		board[captured[i][0]][captured[i][1]] = 0; // Remove captured pieces
    	}
    	board[moves[0][0]][moves[0][1]] = 0; // Remove piece from original location
    	board[pos[0]][pos[1]] = piece; // Place piece at final locattion

    	// Update last play
    	lastPlay = new int[][] {pos,{huff,0}};

    	return 0;
    }

    static int huff() {
    	if (mode < 3) {
    		return 2; // Not the right gamemode
    	}
    	
    	if (lastPlay[1][0]==1) { // It's a huffing flag that was set during the last move
    		board[lastPlay[0][0]][lastPlay[0][1]] = 0; // Offending piece removed.
    		return 1;
    	}
    	return 0;
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

    static byte validateInput(String input) {
    	if (input.length() < 1) { // No input
    		return -1;
    	}

    	switch (input.charAt(0)) {
    	case '(':
        	if (Pattern.compile("(\\(\\d,\\s*\\d\\)\\s*){2,}").matcher(input).matches()) {
        		return 1; // Move
    		}
        	break; // It will return -2 at the end if not valid
    	case 'h':
    		if (input.equals("help")) { // Also accept "help" instead of "?"
    			return 8;
    		} // If it's not "help", fall-through and assume it was huff
    	case 'H':
    		return 2;
    	case 's':
    	case 'S':
    		return 3;
    	case '?':
    		return 8;
    	case 'e':
    	case 'E':
    		if (input.equalsIgnoreCase("EXIT")) {
    			return 16;
    		}
    	}
		return -2;
    }

    static int validateMove(int[] pos, int[] move) {
		return validateMove(pos, move, pieceAt(pos));
    }
    static int validateMove(int[] pos, int[] move, byte piece) {
		int[] rel = {move[0]-pos[0], move[1]-pos[1]};

		if (ownerRel(pieceAt(pos), ownerOf(piece)) == 1) {
			return -3; // ERR: Piece is in fact owned by the opponent
		}
		if (pieceAt(move) == -2 || piece == -2) {
			return -9; // ERR: Out of bounds
		}
		if (Math.abs(rel[0]) != Math.abs(rel[1])) {
			return -10; // ERR: Movement is not diagonal
		}
		if (piece <= 2 && rel[0] * (ownerOf(piece)==0?1:-1) >0) {
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

    static int[][] parseMove(String moveStr) {
    	Matcher coordsMatcher = Pattern.compile("\\((\\d),\\s*(\\d)\\)").matcher(moveStr);

    	// Count '(' to know how many moves there are
    	int[][] moves = new int[(int) moveStr.chars().filter(c -> c=='(').count()][2];

    	// Not your usual for loop, but it's fine.
    	for (int i = 0; coordsMatcher.find(); i++) {
    		moves[i][0] = Integer.valueOf(coordsMatcher.group(1))-1;
    		moves[i][1] = Integer.valueOf(coordsMatcher.group(2))-1;
    	}
    	return moves;
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

    public static int ownerRel(byte piece, int player) {
    	int owner = ownerOf(piece);
    	return (owner<0 || player==0) ? owner : owner==0?1:0;
    }
    public static int ownerOf(byte piece) {
    	return (piece - 1) % 2;
    }

    public static byte pieceAt(int[] pos) {
    	return pieceAt(pos[0], pos[1]);
    }
    public static byte pieceAt(int row, int col) {
    	if (row<0 || board.length<=row || col<0 || board[0].length<=col) {
    		return -2;
    	}
    	return board[row][col];
    }

    static byte randomizeStartingPlayer() {
    	byte starter = (byte) (Math.random()*2);
    	String starterName = names[starter];
    	names[1] = names[starter==0? 1:0];
    	names[0] = starterName;
    	return starter;
    }

    public static int opponent() {
    	return playing==0? 1 : 0;
    }
}