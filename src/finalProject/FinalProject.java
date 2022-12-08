package finalProject;
import java.util.Scanner;

public class FinalProject {
	//TODO
	//Recordar por que en algunos casos le resto '1' como por ejemplo en interpretationofstringmovement
	//Relativos

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
		turn = 1;
		int thereIsAWinner=0;//It starts from 0 as at first there is no winner.If its final value is 1, means that the white pieces have won, and if its
		//final value is 2, it means that the black pieces have won.

		do {//Repeats turns until there is a winner
			boolean nextTurn=false;
			boolean hasMadeAMovement=false;
			String movement; //Ask for a movement
			boolean validMovement;//Check if it's valid
			int[] coordinates = new int[6];//6 because we need 6 coordinates.

			thereIsAWinner = checkWinner(); //ThereIsAWinner variable can only have as values 0,1 or 2 , with the same conditions as checkWinner function, that's why they have the same value.
			if(!(thereIsAWinner==0)) {
				if(thereIsAWinner==1) {
					System.out.println("WHITE'S PIECES WIN!!!!!!");
				}else {
					System.out.println("BLACK'S PIECES WIN!!!!!!");
				}
				break;//If there is a winner is 0, it will go out of the if as there is no winner.
			}

			do {//Loop until there's a change of turn
				do {//Until there is a valid movement or the player end the turn.
					drawBoard();
					movement = askForMovement(); //askForMovement asks for a movement and return that movement. That's why movement = askForMovement.
					validMovement = checkFormatOfMovement(movement);

					if ((validMovement)) { //If not valid movement, goes to while and as it 's still not valid, a new movement will be asked.
						coordinates=interpretationOfStringMovementAsNumbers(movement); //coordinates is going to hold the position and the movement as numbers.This conversion has been done in the interpretationOfStringMovementAsNumbers function
						validMovement=validMovementInTheBoard(coordinates);
						if(validMovement) {
							makeTheMovement(coordinates);
							hasMadeAMovement=true;//If it's a valid movement and the player has made a movement , it will be drawn in the board
							if(movement.length()==10) {//If the player has only move the piece but not capture an opponent's piece, his turn ends.
								nextTurn=true;
							}
						}
					}else if (hasMadeAMovement==true){ //If it's not a valid movement and the player has made a movement (which is required)
						nextTurn=endOfTurn(movement);// it checks the function endOfTurn.
					}
					//What does this "do-while" do? First the program asks for a movement to the user. That movement is stored in the variable movement,
					//which we have declared before the loop that is a string. Then, using the method checkFormatOfMovement, checks that the movement given by the user
					//is valid or not, and it is stored in the variable validMovement, which is a boolean because we declared it before the loop and it's going to say if that
					//movement is valid or not.If valid movement, it is going to be converted into coordinates, thanks to the function interpretationOfStringMovementAsNumbers.
					//That coordinates are going to be checked if they represent a movement that is legal. If not a valid movement, it's check if the player doesn't want to
					//make a turn or if it's not valid.
				}
				while (!(validMovement)&&(nextTurn==false)); //If not valid , enters again the do part ,ask for another new movement until it's valid
				//If it's valid, exits while

			}
			while (nextTurn==false);
			if(turn==1) {
				turn=2;
			}else if(turn==2) {
				turn=1;
			}
			//TODO
			//mensajes para jugadores*20m VIERNES

			//huffing*1:30h
		}while(thereIsAWinner==0);//The loop will be repeated while thereIsAWinner is 0, which means while there is no winner.
	}
	public static void drawBoard() {
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
		// 1 - basic mode
		// 2 - intermediate(queens)
		// 3 - advanced(huffing)
		System.out.println("CHOOSE A GAME OPTION:");
		System.out.println("1)Basic Mode");
		System.out.println("2)Intermediate Mode");
		System.out.println("3)Advanced Mode");
		do {
			mode = input.nextInt();
			input.nextLine(); //So it doesn't read the enter, and it doesn't prompt twice.
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
		//This is not going to determine if the movement can be done or not. It's just going to confirm that the player has written its movement correctly
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


	public static boolean validMovementInTheBoard(int [] coordinates){
		//Inside the function we need to check the movement given by the user,which is at the main function so that's why is inside the(),because we need that movement declared in the main function.
		//But now as it depends on the coordinates , which are now declared in the main function, where we have also made the conversion form movement into coordinates, now it depends on the value of the coordinates, and not the movement
		//int[] coordinates = new int[6]; //6 because we need 6 coordinates.
		//coordinates=interpretationOfStringMovementAsNumbers(movement); //coordinates is going to hold the position and the movement as numbers.This conversion has been done in the interpretationOfStringMovementAsNumbers function


		int[] relative = new int [2];
		relative [0] = coordinates [2] - coordinates [0]; //i
		relative [1] = coordinates [3] - coordinates [1]; //j


		if (whosePiece(coordinates[0],coordinates[1])!=1){ //If it's not the piece of the player who is playing it will return false
			return false ;
		}
		if ((Math.abs(relative[0])!=1)||(Math.abs(relative[1])!=1)){
			return false;
		}
		if((charsInBoard[coordinates[0]][coordinates[1]]=='b')&&(relative[0]<1)) {
			return false;
		}
		if((charsInBoard[coordinates[0]][coordinates[1]]=='w')&&(relative[0]>1)) {
			return false;
		}

		if (whosePiece(coordinates[2],coordinates[3])==1) { //The player can't move the piece into a position where there is already another piece of the same player
			return false;
		}
		if (whosePiece(coordinates[2],coordinates[3])==2) {
			if (!((whosePiece(coordinates[4],coordinates[5])==0)&&(coordinates[0]+2*relative[0]==coordinates[4])&&(coordinates[1]+2*relative[1]==coordinates[5]))){
				return false;
			}

		}else if(!((coordinates[4]==0)&&(coordinates[5]==0))){ //If you don't want to capture the piece of the opponent but introduce 3 pairs of coordinates, it can't be done
			return false;
		}
		return true;

	}

	public static int[] interpretationOfStringMovementAsNumbers(String movement) {
		int[] interpretation = new int[6];
		interpretation[0]=Integer.valueOf(movement.charAt(1)-'1'); //Subtract '1' because the integer value of character 1 is 49, so as we want to make it 0, we have to do that
		interpretation[1]=Integer.valueOf(movement.charAt(3)-'1');
		interpretation[2]=Integer.valueOf(movement.charAt(6)-'1');
		interpretation[3]=Integer.valueOf(movement.charAt(8)-'1');
		if (movement.length()>10) {
			interpretation[4]=Integer.valueOf(movement.charAt(11)-'1');
			interpretation[5]=Integer.valueOf(movement.charAt(13)-'1');
		}
		return interpretation;
	}

	public static int playerPiece(int i,int j) {
		//This function says if the piece which is in the position given by the user is from Player 1, Player 2 or if that position is empty.
		if ((charsInBoard[i][j]=='b')||(charsInBoard[i][j]=='B')) {
			return 2;
		}else if ((charsInBoard[i][j]=='w')||(charsInBoard[i][j]=='W')) {
			return 1;
		}else
			return 0;
	}
	public static int whosePiece(int i,int j) {
		if (playerPiece(i, j)==0){ //If there is no piece it will return 0
			return 0;
		}else if(turn==playerPiece(i, j)) { //If is the turn of a player, and that piece is from that player it will return 1
			return 1;
		}else {//If is the turn of a player and that piece it is from the opponent it will return 2
			return 2;
		}
	}

	public static void makeTheMovement(int [] coordinates) {
		if (whosePiece(coordinates[2],coordinates[3])==2) { //This means that the player is going to capture an opponent's piece
			charsInBoard[coordinates[2]][coordinates[3]]=' '; //The position where there was the opponent's piece is not empty (because the player has capture that piece)
			charsInBoard[coordinates[4]][coordinates[5]]=charsInBoard[coordinates[0]][coordinates[1]]; //In the final position of the piece that has captured the opponent's piece, there must appear the piece which was at the initial position before capturing the opponent's piece
			queenPiece(coordinates[4],coordinates[5]);//If the player captures an opponent's piece and arrives at the initial row of the opponent's pieces, it will be converted into a queen.
			//In this case, as the final coordinates are the 4 and 5 ones , the function queenPiece has to only consider those coordinates in order to convert the pieces into queens, depending on if they are w or b.
		}else {
			charsInBoard[coordinates[2]][coordinates[3]]=charsInBoard[coordinates[0]][coordinates[1]];
			queenPiece(coordinates[2],coordinates[3]);//If the player captures an opponent's piece and arrives at the initial row of the opponent's pieces, it will be converted into a queen.
			//In this case, as the final coordinates are the 2 and 3 ones , the function queenPiece has to only consider those coordinates in order to convert the pieces into queens, depending on if they are w or b.
		}
		charsInBoard[coordinates[0]][coordinates[1]]=' '; //The piece which was at the initial position, as it has been moved, in that position now there is nothing.

	}
	public static void queenPiece(int i,int j) {
		//Check if the piece has to be converted into a queen piece and then it converts it.
		if ((charsInBoard[i][j]=='b')&&(i==7)){
			charsInBoard[i][j]='B';
		}else if ((charsInBoard[i][j]=='w')&&(i==0)) {
			charsInBoard[i][j]='W';
		}
	}
	public static boolean endOfTurn(String movement) {
		if (movement.length()==0) { //This means the player has no given a movement
			return true; //The function will return true as the turn has finished
		}else { //If the player has given a movement
			return false; //The function will return false as the turn has not finished.
		}
	}
	public static int checkWinner() {
		boolean movementInRange=true;
		boolean winsBlackPieces=true;
		boolean winsWhitePieces=true;

		if(turn==1) {
			winsWhitePieces = false;
		}else {
			winsBlackPieces = false;
		}

		for(int i=0;i<charsInBoard.length;i++) {
			for(int j=0;j<charsInBoard[i].length;j++) {
				int[][] possibleCoordinatesForMovement= {
						{i,j,i+1,j+1,0,0},
						{i,j,i-1,j-1,0,0},
						{i,j,i+1,j-1,0,0},
						{i,j,i-1,j+1,0,0},

						{i,j,i+1,j+1,i+2,j+2},
						{i,j,i-1,j-1,i-2,j-2},
						{i,j,i+1,j-1,i+2,j-2},
						{i,j,i-1,j+1,i-2,j+2},
				};
				for(int k=0;k<possibleCoordinatesForMovement.length;k++) {
					for(int p=0;p<possibleCoordinatesForMovement[k].length;p++) {
						//For each k it is going to check if all the p values are between the range that has the board
						if((possibleCoordinatesForMovement[k][p]>=0)&&(possibleCoordinatesForMovement[k][p]<=7)) {
							movementInRange=true;
						}else { //If it's not between the range of the board
							movementInRange=false;
							break;//It gets out of the loop that depends on p
						}

					}
					if(movementInRange==true){//If the movement is between the range , it will be checked in the validMovementInTheBoard function.
						//If in any value of p there is a movement that it's not between the range of the board,the entire movement can't be done, it doesn't go to the validMovementInBoard function to be checked because
						//it is already known that it is not valid.
						validMovementInTheBoard(possibleCoordinatesForMovement[k]);
						if(validMovementInTheBoard(possibleCoordinatesForMovement[k])==true) {
							if((charsInBoard[i][j]=='b')||(charsInBoard[i][j]=='B')) {
								winsWhitePieces = false;

							}else {
								winsBlackPieces = false;
							}

						}
					}
				}

			}

		}
		if(winsWhitePieces==true) {
			return 1;
		}else if(winsBlackPieces==true) {
			return 2;
		}else {
			return 0;
		}
	}






} 