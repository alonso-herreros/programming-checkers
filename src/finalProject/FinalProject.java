package finalProject;

public class FinalProject {

//The variable that is going to be created can be used by any method (function) of this class
static char[][] charsInBoard = new char [8][8]; 

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
drawBoard();

}
public static void drawBoard() {
System.out.println("     1     2     3     4     5     6     7     8  ");
System.out.println("   _________________");


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

System.out.println("\n  |__|_|_|_|_|_|_|__|");
//\n is used so just below the line which contains the character it is going to appear the bottom part of each cell

}
}
}