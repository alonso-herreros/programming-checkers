package finalProject;
import java.util.Scanner;

public class FinalProject {

	static Scanner input = new Scanner(System.in);//*STATIC

	//The variables that are going to be created as static can be used by any method (function) of this class
	static char[][] charsInBoard = new char [8][8];
	static int turn; // This is 1 when it's the white player's turn and 2 when it's the black player's turn
	static int mode;//Depending on the number that the user writes, it will have a different value (1,2 or 3)
	static int [] last2Coordinates=new int[2];//Static because it is going to be used in different functions of this class (For example in huffing and main)
	//Creation of a new method that is going to populate charsInBoard following the rules given.
	static boolean movementOfCapture=false;
	public static void initializeCharsOfBoard() { //The () is empty because it doesn't depend on anything.This method is just going to show the initial board
		//but it is not going to do another function.
		//We are going to do 2 for-loops so we can access to all the 64 cells (8x8 matrix=64 cells).
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
		turn = 2; // Black pieces start!
		int thereIsAWinner=0;//It starts from 0 as at first there is no winner.If its final value is 1, means that the white pieces have won, and if its
		//final value is 2, it means that the black pieces have won.
		String errorMessage="";//Empty because at first we don't want to say the player that they (he/she) have done something wrong without being true.
		do {//Repeats turns until there is a winner
			boolean nextTurn=false;
			boolean hasMadeAMovement=false;
			String movement; //Ask for a movement
			boolean validMovement;//Check if it's valid
			int[] coordinates = new int[6];//6 because we need 6 coordinates.

			thereIsAWinner = checkWinner(); //ThereIsAWinner variable can only have as values 0,1 or 2 , with the same conditions as checkWinner function, that's why they have the same value.
			if(!(thereIsAWinner==0)) {
				drawBoard();
				if(thereIsAWinner==1) {
					System.out.println("WHITE'S PIECES WIN!!!!!!");
				}else {
					System.out.println("BLACK'S PIECES WIN!!!!!!");
				}
				break;//If there is a winner is 0, it will go out of the if as there is no winner and they have to keep playing.
			}


			do {//Loop until there's a change of turn

				do {//Until there is a valid movement or the player ends the turn.

					drawBoard(); //For each movement , the board is updated, it is going to show that new movement.
					if (errorMessage.length()!=0) { // If there is an error message that we stored
						System.out.println(errorMessage);
						errorMessage=""; //Because we don't want to show any error message the next time, it has to be empty again	
					}
					if(hasMadeAMovement==true) { // If the player has made a movement and we are here again, that means they made a capture movement, and we will tell them how to continue.
						System.out.println("After capturing a piece, you may capture another piece. If you can't, end your turn by pressing Enter.");
					}
					
					movement = askForMovement(); //askForMovement asks for a movement and return that movement. That's why movement = askForMovement.
					validMovement = checkFormatOfMovement(movement);//checkFormatOfMovement checks if that movement is valid and the result is stored in the validMovement variable.

					if (validMovement) { //If not valid movement, goes to while and as it's still not valid, a new movement will be asked.If it's valid , it will enter in the if.
						coordinates=interpretationOfStringMovementAsNumbers(movement); //coordinates is going to hold the position and the movement as numbers.This conversion has been done in the interpretationOfStringMovementAsNumbers function
						validMovement=validMovementInTheBoard(coordinates);//If the coordinates given by the player are also valid in the board, the result of if it's valid or not is stored in the validMovement variable

						if(hasMadeAMovement==true) {
							if((coordinates[0]!=last2Coordinates[0])||(coordinates[1]!=last2Coordinates[1])) {//If it's not moved the same piece because wanted to not end its turn but must happens...
								validMovement=false;
								errorMessage = "You can't move a different piece!!!! ";
							}
							if(movement.length()==10){//If the movement length is not 15 (want to capture) but wants to keep playing, the turn must end, they can't continue playing
								validMovement=false;
								errorMessage = "If you can keep capturing you MUST do it. If not, end your turn (press enter) ";
							}
						}

						if(validMovement) {
							char initialPiece = charsInBoard[coordinates[0]][coordinates[1]]; // We need this so we can check if the piece has become a king or not
							makeTheMovement(coordinates);
							if(movement.length()==10) {//If only wants to move , the last 2 coordinates will be the second and third (taking into account that for us the first one is the 0)
								last2Coordinates[0]=coordinates[2];
								last2Coordinates[1]=coordinates[3];
							}else {//If wants to capture, the last 2 coordinates, will be the fourth and fifth
								last2Coordinates[0]=coordinates[4];
								last2Coordinates[1]=coordinates[5];
							}
							hasMadeAMovement=true;//If it's a valid movement and the player has made a movement , it will be drawn in the board
							if(movement.length()==10) {//If the player has only move the piece but not capture an opponent's piece, his turn ends.
								nextTurn=true;
							}
							if(charsInBoard[last2Coordinates[0]][last2Coordinates[1]] != initialPiece) { // If the piece at the final position is different from the piece at the initial position, that means it has become a king and it can't continue moving.
								nextTurn=true;
							}
							
						}else if(errorMessage.length()==0){//If there wasn't before an error message or if we hadn't specified why is wrong. It will happen...
							errorMessage="Not a valid movement. ";
						}
					}else {
						if ((mode==3)&&(movement.equalsIgnoreCase("Huffing"))&&(hasMadeAMovement==false)) { //If the player has written huffing without doing before any movement , the function huffing is checked
							if(huffing()==true) {
								charsInBoard[last2Coordinates[0]][last2Coordinates[1]]=' ';//If there is huffing, the piece that was moved in the last turn (whose current position is the last coordinates given in the previous turn(which has been the last one because the current player first said 'huffing' instead of moving )) is now eliminated, so its value is empty
								// In this special case we will not do anything about validMovement because we need to ask the player again what is their move
							}
							else {//If it's not true (there is no huffing)
								errorMessage = "The opponent's move was correct. ";
							}
						}else if((endOfTurn(movement)==true)&&(hasMadeAMovement==true)){
							nextTurn=true;//to enter in the if, endOfTurn must be true (and hasMadeAMovement is true), so nextTurn is also true.
						}else {
							errorMessage = "That's not an option!!!! ";
						}
					}

					//What does this "do-while" do? First the program asks for a movement to the user. That movement is stored in the variable movement,
					//which we have declared before the loop that is a string. Then, using the method checkFormatOfMovement, checks that the movement given by the user
					//is valid or not, and it is stored in the variable validMovement, which is a boolean because we declared it before the loop and it's going to say if that
					//movement is valid or not.If valid movement, it is going to be converted into coordinates, thanks to the function interpretationOfStringMovementAsNumbers.
					//That coordinates are going to be checked if they represent a movement that is legal. If not a valid movement, it's check if the player doesn't want to
					//make a turn or if it's not valid.If it's still their turn because maybe they can keep capturing, if it's not moved the same piece , it can't be done or if there is a piece
					//that can capture but the movement is not for capturing , it's not valid.

				}
				while (!(validMovement)&&(nextTurn==false)); //If not valid and don't want to end the turn, enters again the do part, ask for another new movement until it's valid or the turn ends
				//If it's valid or nextTurn is true, exits while
			}
			while (nextTurn==false);
			if(turn==1) {
				turn=2;
			}else { // We don't need to check turn==2 because it's the only other option
				turn=1;
			}

		}while(thereIsAWinner==0);//The loop will be repeated while thereIsAWinner is 0, which means while there is no winner.
	}

	public static void drawBoard() {
		System.out.println("     1     2     3     4     5     6     7     8  ");
		System.out.println("   _______________________________________________");


		for(int i=0; i<8; i++) {
			System.out.println("  |     |     |     |     |     |     |     |     |");

			System.out.print(i+1 + " |"); //Print line number and the first |

			//System.out.println("  |     |  b  |     |  b  |     |  b  |     |  b  |");
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
		// 2 - intermediate(kings)
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
			System.out.print("It's the white player's turn. ");
		}else {
			System.out.print("It's the black player's turn. ");
		}
		System.out.println("Make your movement!");
		System.out.print("Remember to do it in this form (row1,col1)(row2,col2). ");//(row1,col1) is the initial position of the piece and and (row2,col2) the last one
		if(mode==3) {
			System.out.print("If you want to huff, write: huffing ");
		}
		System.out.println();
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

		for (int i=0;i<repetitions;i++) {//How the movement must be written and the range between those coordinates must be
			if (!(movement.charAt(i*5)=='(' && movement.charAt(i*5+1)>='1'&& movement.charAt(i*5+1)<='8' && movement.charAt(i*5+2)==','
					&& movement.charAt(i*5+3)>='1' && movement.charAt(i*5+3)<='8'&& movement.charAt(i*5+4)==')')) {
				return false;
			}
		}

		return true; //It will only return true if the conditions of the first if and the for are fulfilled.
	}


	public static boolean validMovementInTheBoard(int [] coordinates){
		//Inside the function we need to check the movement given by the user,which is at the main function so that's why is inside the(),because we need that movement declared in the main function.
		//But now as it depends on the coordinates , which are now declared in the main function, where we have also made the conversion from movement into coordinates, now it depends on the value of the coordinates, and not the movement
		//int[] coordinates = new int[6]; //6 because we need 6 coordinates.
		//coordinates=interpretationOfStringMovementAsNumbers(movement); //coordinates is going to hold the position and the movement as numbers.This conversion has been done in the interpretationOfStringMovementAsNumbers function


		int[] relative = new int [2];
		//If for example the movement is (2,3)(3,4) , relative [0] represents 3-2 and relative[1] represents 4-3.

		relative [0] = coordinates [2] - coordinates [0]; //i
		relative [1] = coordinates [3] - coordinates [1]; //j

		for(int p=0;p<coordinates.length;p++) {
			//Look for the 6 numbers that form the coordinates. If all of them are between 0 and 7 it will continue checking the rest of the conditions. If not, it will return false, so that movement is not valid in the board
			if((coordinates[p]<0)||(coordinates[p]>7)) {
				return false;
			}
		}
		if (whosePiece(coordinates[0],coordinates[1])!=1){ //If it's not the piece of the player who is playing it will return false
			return false ;
		}
		if ((Math.abs(relative[0])!=1)||(Math.abs(relative[1])!=1)){//The absolute value of the relatives must be 1 (meaning the value is 1 or -1) as the piece can only be moved 1 cell diagonally
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
		if (whosePiece(coordinates[2],coordinates[3])==2) { //If it's an opponent's piece
			if (!((whosePiece(coordinates[4],coordinates[5])==0)&&(coordinates[0]+2*relative[0]==coordinates[4])&&(coordinates[1]+2*relative[1]==coordinates[5]))){
				//This means that if the position formed by the coordinates 4 and 5 is empty, there is going to be a capture's movement.If this happens , the sum of the first coordinate of
				//the movement and 2 times the result of the relative[0] coordinate (coordinate[2]) - the first(coordinate[0]) has to be the same number as the coordinate [4].
				//And also, the sum of the coordinate[1] and 2 times the relative[1] (coordinate[1] - coordinate[3]) has to be the same number as the coordinate[5].
				//If it's not fulfilled one of them it will return false
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
		if (movement.length()>10) {//Means there is gonna to be a capture of a piece so the length of the movement has to be 15.
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
			charsInBoard[coordinates[2]][coordinates[3]]=' '; //The position where there was the opponent's piece is now empty (because the player has capture that piece)
			charsInBoard[coordinates[4]][coordinates[5]]=charsInBoard[coordinates[0]][coordinates[1]]; //In the final position of the piece that has captured the opponent's piece, there must appear the piece which was at the initial position before capturing the opponent's piece
			movementOfCapture=true;//If it's a movement in which the player captures an opponent's piece, it has been made a movement of capture so its value is true
			if (mode>1) {//Because if mode is 1 the pieces are not converted into kings
				kingPiece(coordinates[4],coordinates[5]);//If the player captures an opponent's piece and arrives at the initial row of the opponent's pieces, it will be converted into a king.
			}
			//In this case, as the final coordinates are the 4 and 5 ones , the function kingPiece has to only consider those coordinates in order to convert the pieces into kings, depending on if they are w or b.
		}else {
			charsInBoard[coordinates[2]][coordinates[3]]=charsInBoard[coordinates[0]][coordinates[1]];
			movementOfCapture=false;//If it's a movement in which the player doesn't capture an opponent's piece, it has not been made a movement of capture so its value is false
			if (mode>1) {//Because if mode is 1 the pieces are not converted into kings
				kingPiece(coordinates[2],coordinates[3]);//If the player captures an opponent's piece and arrives at the initial row of the opponent's pieces, it will be converted into a king.
			}
			//In this case, as the final coordinates are the 2 and 3 ones , the function kingPiece has to only consider those coordinates in order to convert the pieces into kings, depending on if they are w or b.
		}
		charsInBoard[coordinates[0]][coordinates[1]]=' '; //The piece which was at the initial position, as it has been moved, in that position now there is nothing.

	}
	public static void kingPiece(int i,int j) {
		//Check if the piece has to be converted into a king piece and then it converts it.
		if ((charsInBoard[i][j]=='b')&&(i==7)){//When any black piece reaches the row 7 in the board
			charsInBoard[i][j]='B';
		}else if ((charsInBoard[i][j]=='w')&&(i==0)) {//When any white piece reaches the row 0 in the board
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
		boolean winsBlackPieces=true;
		boolean winsWhitePieces=true;

		if(turn==1) { // A player can only lose at the beginning of their own turn (mabye they can't move but they can after the other player's move).
			// This also means you can't win until your turn is over.
			winsWhitePieces = false;
		}else {
			winsBlackPieces = false;
		}

		for(int i=0;i<charsInBoard.length;i++) {
			for(int j=0;j<charsInBoard[i].length;j++) {
				if((canCapture(i,j)==true)||(canMove(i,j)==true)) {
					if((charsInBoard[i][j]=='b')||(charsInBoard[i][j]=='B')) {
						winsWhitePieces = false;//Because one of the black pieces can move. If a piece can be moved, means they can continue playing so there is not a winner.

					}else {
						winsBlackPieces = false;//If it's the turn of the white pieces, and one of them can move, the black pieces can't have won
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
	public static boolean huffing() {
		//To check if the previous player could have moved a piece, we have to come back to their turn in order to check their validity of their movement
		if(turn==1) {
			turn=2;
		}else {
			turn=1;
		}//If the player has used a piece to capture and has end its turn when he/she could have captured another opponent's piece again
		if((movementOfCapture==true)&&(canCapture(last2Coordinates[0],last2Coordinates[1])==true)) {
			//Change again the turn to the player who has to move next . (Because the first change of player has been done before in order to check if their movement was valid or not).
			if(turn==1) {
				turn=2;
			}else {
				turn=1;
			}
			return true;

		}else if(movementOfCapture==false){//If the player has not made a movement to capture an opponent's piece
			for(int i=0;i<charsInBoard.length;i++) {
				for(int j=0;j<charsInBoard[i].length;j++) {
					if((whosePiece(i,j)==1)&&(canCapture(i,j)==true)) {//If it's the turn of a player and its piece can capture but hasn't made a movement to capture, there is huffing so it will return true
						//Change again the turn to the player who has to move next . (Because the first change of player has been done before in order to check if their movement was valid or not).
						if(turn==1) {
							turn=2;
						}else {
							turn=1;
						}
						return true;
					}
				}
			}

		}
		//Change again the turn to the player who has to move next . (Because the first change of player has been done before in order to check if their movement was valid or not).
		if(turn==1) {
			turn=2;
		}else {
			turn=1;
		}
		return false; //If it doesn't check any of the previous conditions, it's not huffing.


	}
	public static boolean canCapture(int i,int j) {
		int[][] possibleCoordinatesForCapture= {
				//If the player can capture , the second and last position could only have those 4 possible values
				{i,j,i+1,j+1,i+2,j+2},
				{i,j,i-1,j-1,i-2,j-2},
				{i,j,i+1,j-1,i+2,j-2},
				{i,j,i-1,j+1,i-2,j+2},
		};
		for(int k=0;k<possibleCoordinatesForCapture.length;k++) {
			//If it's a valid movement in the board it will be checked in that function
			if(validMovementInTheBoard(possibleCoordinatesForCapture[k])==true) {
				return true;
			}
		}
		return false;
	}
	public static boolean canMove(int i,int j) {
		int[][] possibleCoordinatesForMovement= {
				//If the player can move , the third position is 0,0 as it's not a capture movement
				{i,j,i+1,j+1,0,0},
				{i,j,i-1,j-1,0,0},
				{i,j,i+1,j-1,0,0},
				{i,j,i-1,j+1,0,0},
		};
		for(int k=0;k<possibleCoordinatesForMovement.length;k++) {
			if(validMovementInTheBoard(possibleCoordinatesForMovement[k])==true) {
				return true;
			}
		}
		return false;

	}
}