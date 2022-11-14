package main;

public class Main {

	public static void main(String[] args) {
		drawBoard();
	}
	
	public static void drawBoard() {
		System.out.println("    1    2    3    4    5    6    7    8   ");
		System.out.println("   _______________________________________ ");
		for (int i=0; i<8; i++) {
			System.out.println("  |    |    |    |    |    |    |    |    |");
			System.out.print(i+1);
			System.out.println(" |    |    |    |    |    |    |    |    |");
			System.out.println("  |____|____|____|____|____|____|____|____|");
		}
	}

}
