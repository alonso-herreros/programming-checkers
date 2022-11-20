package main;
import java.util.Scanner;

public class Prompter {
    static final Scanner input = new Scanner(System.in);
    
    static final String START_MSG =
		"Select a game mode:\n" +
		"1. Basic (no kings, no huffing)\n" +
		"2. Intermediate (no huffing)\n" +
		"3. Advanced (full game)\n";
    static final String NAME_MSG = "%s player's name: ";
    static final String PLAY_MSG = "The result was %s, so %s is black.\n";
    static final String TURN_MSG = "It's %s's turn (%s pieces). ";
    static final String MOVE_MSG = "Enter your move or '?' for help.\n";
    static final String EXIT_MSG = "Do you really want to exit? (y/n) ";
    static final String SURRENDER_MSG = "Are you sure you want to surrender? (y/n) ";
    static final String STALL_MSG = "We've already been here. Do you want to call it a draw? (y/n) ";
    
    static final String[] INVALID_MSGS = {
		"That is not an option.\n",
		"You can't make that move.\n", //1
		"There is no piece there.\n",  //2
		"That is not your piece.\n",   //3
		"That's too many moves.\n",    //4
		"", "", "", "", //5-8
		"That movement is out of bounds.\n", //9
		"Movement must be diagonal.\n",              //10
		"You can't move to the same square.\n",      //11
		"That piece can't move that far.\n", //12
		"", "", "", "", //13-16
		"That piece can't move backwards.\n", // 17
		"", "", "", "", "", "", "", //18-24
		"There is already a piece in that square.\n", //25
		"You can't capture that piece.\n", //26
		"You can't capture your own pieces.\n", //27
    };
    static final String[] HUFF_MSGS = {
    		"There is no reason for that. %s's move was valid. Press Enter to continue.\n",
    		"That's correct. %s's piece will be removed. Press Enter to continue.\n",
    		"Huffing is only available in advanced mode. Press Enter to continue.\n"
    };

    static final String HTP_MSG = 
    		"How to play:\n" +
        	"-Enter your move following this syntax: (fromY, fromX) (moveY, moveX)\n" + 
        	" You may append as many moves as you want: (fromY, fromX) (move1Y, move1X) (move2Y, move2X) ...\n" +
	    	" Movement coordinates are absolute.\n" + 
	    	"-Enter 'H' to huff the other player (advanced mode only).\n" +
        	"-Enter '?' to show this message.\n" +
    		"-Enter 'S' to surrender.\n"+
        	"-Enter 'EXIT' to quit.\n";
    static final String END_MSG = "The game has ended. ";
    static final String WIN_MSG = "%s is the winner!";
    static final String DRAW_MSG = "It's a draw!";

	static String[] names = new String[2];
    
	public static void setNames(String[] finalNames) {
		names = finalNames;
	}

	public static void info(String message) {
		System.out.printf(message);
		input.nextLine();
	}
	
    public static String promptStart() {
        System.out.printf(START_MSG);
        return input.nextLine();
        
    }

    public static String[] promptNames() {
    	String[] tmpNames = new String[2];
    	for (int i = 0; i<2; i++) {
    		System.out.printf(NAME_MSG, i==0? "First":"Second");
    		tmpNames[i] = input.nextLine();
    	}
    	return tmpNames;
    }
    
    public static void promptPlay(byte coinFlip, int playing) {
    	info(String.format(PLAY_MSG, coinFlip==0? "heads":"tails", names[playing]));
    }

    public static String promptMove() {
    	System.out.printf(MOVE_MSG);
    	String line = input.nextLine();
    	return line.length()>0? line : " ";
    }

    public static String promptTurn(int playing) {
    	System.out.printf(TURN_MSG, names[playing], playing==0?"black":"white");
    	return promptMove();
    }

    public static boolean promptSurrender() {
    	System.out.printf(SURRENDER_MSG);
    	return input.nextLine().equals("y");
    }

    public static boolean promptExit() {
    	System.out.printf(EXIT_MSG);
    	return input.nextLine().equals("y");
    }

    public static boolean promptStall() {
    	System.out.printf(STALL_MSG);
    	return input.nextLine().equals("y");
    }
    
    public static String promptInvalid() {
    	return promptInvalid(0);
    }
    public static String promptInvalid(int code) {
    	System.out.printf(INVALID_MSGS[code].length()>0? INVALID_MSGS[code] : INVALID_MSGS[0]);
    	return promptMove();
    }

    public static void promptHuff(int playing, int code) {
    	info(String.format(HUFF_MSGS[code], names[playing==0?1:0]));
    }

    public static String promptHtp() {
    	System.out.printf(HTP_MSG);
    	return promptMove();
    }

    public static void promptEnd(int code) {
    	promptEnd();
    	if (code > 1) {
    		info(DRAW_MSG);
    	}
    	else {
    		info(String.format(WIN_MSG, names[code]));    		
    	}
    }
    public static void promptEnd() {
    	System.out.printf(END_MSG);
    }

}
