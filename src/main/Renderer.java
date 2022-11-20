package main;

public class Renderer {

    static final String[] TITLE = {
        " _____   _    _   _____   _____   _   _    _____   _____   _____ ",
        "/  ___\\ | |  | | | ____| /  ___\\ | | / /  | ____| |  _  \\ / ____\\",
        "| /     | |__| | | |__   | /     | |/ /   | |__   | |_| | | |___ ",
        "| |     |  __  | |  __|  | |     |    \\   |  __|  |    _/  \\___ \\",
        "| \\____ | |  | | | |___  | \\____ |  /\\ \\  | |___  | |\\ \\   ___| |",
        "\\_____/ |_|  |_| |_____| \\_____/ |_|  \\_\\ |_____| |_| \\_\\ /_____/",
        "                                                                 "
    };

    static final String[][] COIN_FLIP = {
    	{
    		"     ",
    		"     ",
    		"     ",
    		"    _",
    		"=========",
    		"Tossing a coin to decide who goes first.",
    	},
    	{
    		"     ",
    		"     ",
    		"    /",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first.",
    	},
    	{
    		"     ",
    		"     ",
    		"    |",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first.",
    	},
    	{
    		"     ",
    		"    \\",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first.",
    	},
    	{
    		"     ",
    		"    -",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first..",
    	},
    	{
    		"     ",
    		"    /",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first..",
    	},
    	{
    		"    |",
    		"     ",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first..",
    	},
    	{
    		"    \\",
    		"     ",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first..",
    	},
    	{
    		"    -",
    		"     ",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first...",
    	},
    	{
    		"    \\",
    		"     ",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first...",
    	},
    	{
    		"    -",
    		"     ",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first...",
    	},
    	{
    		"    /",
    		"     ",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first...",
    	},
    	{
    		"     ",
    		"    |",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first.",
    	},
    	{
    		"     ",
    		"    \\",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first.",
    	},
    	{
    		"     ",
    		"    -",
    		"     ",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first.",
    	},
    	{
    		"     ",
    		"     ",
    		"    /",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first.",
    	},
    	{
    		"     ",
    		"     ",
    		"    |",
    		"     ",
    		"=========",
    		"Tossing a coin to decide who goes first..",
    	},
    	{
    		"     ",
    		"     ",
    		"     ",
    		"    \\",
    		"=========",
    		"Tossing a coin to decide who goes first..",
    	},
    	{
    		"     ",
    		"     ",
    		"     ",
    		"    /",
    		"=========",
    		"Tossing a coin to decide who goes first..",
    	},
    	{
    		"     ",
    		"     ",
    		"     ",
    		"    \\",
    		"=========",
    		"Tossing a coin to decide who goes first..",
    	},
    	{
    		"     ",
    		"     ",
    		"     ",
    		"    /",
    		"=========",
    		"Tossing a coin to decide who goes first...",
    	},
    	{
    		"     ",
    		"     ",
    		"     ",
    		"    \\",
    		"=========",
    		"Tossing a coin to decide who goes first...",
    	},
    	{
    		"     ",
    		"     ",
    		"     ",
    		"    /",
    		"=========",
    		"Tossing a coin to decide who goes first...",
    	},
    	{
    		"     ",
    		"     ",
    		"     ",
    		"    _",
    		"=========",
    		"Tossing a coin to decide who goes first...",
    	},
    };

    static final int[] TILE_SIZE = {4, 11};
    static final String[][] TILES = {
		{
            "@@@@@@@@@@@",
            "@@@@@@@@@@@",
            "@@@@@@@@@@@",
            "@@@@@@@@@@@"
		},
		{
		
            "           ",
            "           ",
            "           ",
            "           "
		},
        {
            "  _______  ",
            " /   b   \\ ", 
            "(\\_______/)",
            " \\_______/ "
        },
        {
            "  _______  ",
            " / % w % \\ ", 
            "(\\%_%_%_%/)",
            " \\_%_%_%_/ "
        },
        {
            " /   B   \\ ", 
            "(\\_______/)", 
            "(\\_______/)",
            " \\_______/ "
        },
        {
            " /% % % %\\ ",
            "(\\_%_W_%_/)",
            "(\\%_%_%_%/)",
            " \\_%_%_%_/ "
        }
    };
    static final String[][] BORDERS = {
		{
			"| |",
			"| |",
			"| |",
			"| |",
		},
		{
			"           ",
			"-----------"
		},
		{
			"| |",
			"| |",
			"| |",
			"| |",
		},
		{
			"-----------",
			"           ",
			"-----------"
		},
		{
			"  +",
			" /\u0000",
			"/ \u0000"
		},
		{
			"+  ",
			"\u0000\\ ",
			"\u0000 \\"
		},
		{
			"| +",
			"|/ ",
			"+--"
		},
		{
			"+ |",
			" \\|",
			"--+"
		}
    };

    
    public static void renderTitle() {
        draw(TITLE);
    }

    public static void renderBoard(byte[][] board) { // It's not good code, but it works
    	int[] border = {
				BORDERS[0][0].length(),
				BORDERS[1].length,
				BORDERS[2][0].length(),
				BORDERS[3].length
    	};
    	
    	int height = board.length*TILE_SIZE[0]+ border[1]+border[3];
    	int width = board[0].length*TILE_SIZE[1]+ border[0]+border[2];
    	
        char[][] display = new char[height][width];
        
    	for (int i = 0; i < board.length; i++) {
    		
    		overlay(BORDERS[0], display, border[1]+i*TILE_SIZE[0], 0);
    		overlay(BORDERS[2], display, border[1]+i*TILE_SIZE[0], width-border[2]);
    		display[border[1]+i*TILE_SIZE[0]+2][1] = String.valueOf(i+1).charAt(0);
    		display[border[1]+i*TILE_SIZE[0]+2][width-2] = String.valueOf(i+1).charAt(0);
    		
    		for (int j = 0; j < board[i].length; j++) {
    			if (i == 0) {
    	    		overlay(BORDERS[1], display, 0, border[0]+j*TILE_SIZE[1]);
    	    		overlay(BORDERS[3], display, height-border[3], border[0]+j*TILE_SIZE[1]);
    	    		display[0][border[0]+j*TILE_SIZE[1]+5] = String.valueOf(j+1).charAt(0);
    	    		display[height-2][border[0]+j*TILE_SIZE[1]+5] = String.valueOf(j+1).charAt(0);
    			}
    			
    	        overlay(tileAt(board,i,j), display, border[1]+i*TILE_SIZE[0], border[0]+j*TILE_SIZE[1]);
    		}
    	}
    	
        for (int i = 0; i<2; i++) {
            for (int j = 0; j<2; j++) {
            	String[] corner = BORDERS[4+2*i+j];
            	overlay(corner, display, 1+i*(height-corner.length-1), j*(width-corner[0].length()));
            }
        }
        
    	draw(display);
    }


    public static void animateCoinFlip() 
    	throws InterruptedException {
    	animate(COIN_FLIP, 90);
    }


    public static void clear() {
    	for (int i = 0; i < 64; i++) {
    		System.out.println();
    	}
    }

    static void animate(String[][] anim, int frameTime)
    	throws InterruptedException {
    	for (int i = 0; i<anim.length; i++) {
    		draw(anim[i]);
    		Thread.sleep(frameTime);
    	}
    }

    static void draw(String[] strArray) {
    	clear();
    	for (int i = 0; i < strArray.length; i++) {
    		System.out.println(strArray[i]);
    	}
    }
    static void draw(char[][] charMatrix) {
    	clear();
    	for (int i = 0; i < charMatrix.length; i++) {
    		System.out.println(String.valueOf(charMatrix[i]));
    	}
    }

    static void overlay(String[] layer, char[][] base, int py, int px) {
    	for (int i = 0; i<layer.length; i++) {
    		for (int j = 0; j<layer[i].length(); j++) {
    			char c = layer[i].charAt(j);
    			if(c != 0) {
    				base[i+py][j+px] = c;
    			}
    		}
    	}
    }
    static void overlay(char[][] layer, char[][] base, int py, int px) {
    	for (int i = 0; i<layer.length; i++) {
    		for (int j = 0; j<layer[i].length; j++) {
        		base[i+py][j+px] = layer[i][j];
    		}
    	}
    }

    static String[] tileAt(byte[][] board, int y, int x) {
    	return TILES[board[y][x] == 0 ? (y+x)%2 : board[y][x]+1];
    }

    static char[][] toCharMatrix(String[] strArray) {
    	char[][] charMatrix = new char[strArray.length][strArray[0].length()];
    	for (int i = 0; i < strArray.length; i++) {
    		charMatrix[i] = strArray[i].toCharArray();
    	}
    	return charMatrix;
    }
    static String[] toStrArray(char[][] charMatrix) {
    	String[] strArray = new String[charMatrix.length];
    	for (int i = 0; i < strArray.length; i++) {
    		strArray[i] = String.valueOf(charMatrix[i]);
    	}
    	return strArray;
    }
}
