package finalProject;
import java.util.Scanner;

public class FinalProject {

	static Scanner input = new Scanner(System.in);//*STATIC

	//The variable that is going to be created can be used by any method (function) of this class
	static char[][] charsInBoard = new char [8][8];
	static int turn;
	static int mode;//Depending on the number that the user writes it will have a different value (1,2 or 3)
	//Creation of a new method that is going to populate charsInBoard following the rules given
	public static void initializeCharsOfBoard() { //The () is empty because it doesn't depend on anything.This method is just going to show the initial board
		//but it is not going to do another function.
		//We are going to do 2 fors so we can access to all the 64 cells (8x8 matrix=64 cells).
		for (int i=0; i<charsInBoard.length; i++) {
			for (int j=0; j<charsInBoard[i].length; j++) {
				if ((i+j)%2==0) { //For alternating squares, being i+j even
					charsInBoard[i][j]=' '; //Assign a space to the element at the (i,j)
				}
				else {
					if (i<=2) {
						charsInBoard[i][j]='b'; //Because the first 3 rows are going to contain the b pieces
					}else if (i>=5){
						charsInBoard[i][j]='w'; //Because the last 3 rows are going to contain the w pieces
					}else
						charsInBoard[i][j]=' '; //Because the middle rows are going to be empty in the initial board

				}
			}
		}
	}

	public static void main(String args[]) {
		initializeCharsOfBoard(); //First because we can't draw the board if we don't have the values
		start();
		selectMode();
		drawBoard();
		turn = 1;
		String movement; //Ask for a movement
		boolean validMovement ; //Check if it's valid
		do {
			movement = askForMovement(); //askForMovement asks for a movement and return that movement. That's why movement = askForMovement.
			validMovement = checkFormatOfMovement (movement);
			//What does this "do-while" do? First the program asks for a movement to the user. That movement is stored in the variable movement,
			//which we have declared before the loop that is a string. Then, using the method checkFormatOfMovement, checks that the movement given by the user
			//is valid or not, and it is stored in the variable validMovement, which is a boolean because we declared it before the loop and it's going to say if that
			//movement is valid or not
		}
		while (!(validMovement)); //If not valid , enters again the do part ,ask for another new movement until it's valid
		//If it's valid, exits while

	}
	public static void drawBoard() { //TERMINADO!!!!!!
		System.out.println("     1     2     3     4     5     6     7     8  ");
		System.out.println("   _______________________________________________");


		for(int i=0; i<8; i++) {
			System.out.println("  |     |     |     |     |     |     |     |     |");

			System.out.print(i+1 + " |"); //Print line number and the first |

			//System.out.println(" |    | b  |    | b  |    | b  |    | b  |");
			//We are going to create a for so everything is going to be repeated but it will
			//have a different letter(b or w) depending on each case.

			for(int j=0; j<8; j++) {
				System.out.print("  " + charsInBoard[i][j] + "  |"); //It will print the char depending on its position (i,j).That's why we
				//have [i][j]
			}

			System.out.println("\n  |_____|_____|_____|_____|_____|_____|_____|_____|");
			//\n is used so just below the line which contains the character it is going to appear the bottom part of each cell

		}
	}

	public static void start() {
		// Until enter is pressed the game has not started
		System.out.println("Please enter to start");

		input.nextLine();

	}

	public static void selectMode() {
		// Mode selection
		// 1- basic mode
		// 2- intermediate
		// 3- advanced
		System.out.println("CHOOSE A GAME OPTION:");
		System.out.println("1)Basic Mode");
		System.out.println("2)Intermediate Mode");
		System.out.println("3)Advanced Mode");
		do {
			mode = input.nextInt();
			if((mode<1)||(mode>3)) {
				System.out.println("Please introduce a valid number.");
			}
		}
		while((mode<1)||(mode>3));
		//Until the number introduced is 1,2 or 3, it will keep asking for a number
	}
	public static String askForMovement() {
		if (turn==1) {
			System.out.println("It's the white player's turn. ");
		}else {
			System.out.println("It's the black player's turn. ");
		}
		System.out.println("Make your movement ! \n Remember to do it in this form (row1,col1)(row2,col2)"); //(row1,col1) is the initial position of the piece and
		//(row2,col2) is the final position of the piece.
		String movement = input.nextLine(); //Read the movement of the player
		return movement; //Return the movement to the main method, that will then be stored in the variable called movement of the main method
	}
	public static boolean checkFormatOfMovement(String movement) {
		int repetitions;
		if (movement.length()==10) {
			repetitions=2; //Because for a movement at least we need 4 coordinates. Example:"(3,4)(4,5)"
		}else if (movement.length()==15) {
			repetitions=3; //Because if the user wants to capture , 6 coordinates will be needed."(3,4)(4,5)(5,6)"
		}else {
			return false; //If the user only writes 2 coordinates or more than 6
		}


		for (int i=0;i<repetitions;i++) {
			if (!(movement.charAt(i*5)=='(' && movement.charAt(i*5+1)>='1'&& movement.charAt(i*5+1)<='8' && movement.charAt(i*5+2)==','
					&& movement.charAt(i*5+3)>='1' && movement.charAt(i*5+3)<='8'&& movement.charAt(i*5+4)==')')) {
				return false;
			}
		}

		return true; //It will only return true if the conditions of the first if and the for are fulfilled.
	}
	public static void validMovementInTheBoard(String movement){ //Inside the function we need to check the movement given by the user,which is at the main function so that's why is inside the(),because we need that movement declared in the main function
		int[] coordinates = new int[2]; //2 because we need 2 numbers in order to create a coordinate. }
	}
} 