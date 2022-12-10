# Project: Checkers

## Programming I final project, group 95-65
### Members:
- 100496646 Claudia Múñez Bravo (group 65)
- 100493990 Alonso Herreros Copete (group 95)


### Options included:
- Mode selection is allowed: basic, intermediate or advanced.
- All modes are implemented.

### Design decisions and how to play:
- In intermediate mode, pieces that reach the opposite end of the board are turned into kings, which
  are able to move backwards.
- In advanced mode, a player may type "`huffing`" before they move a piece if they think the other player
  has made an error, which could be either finishing their turn early when they could continue capturing
  or making a regular move (not capturing) when they could have used another piece to capture.
  If it's true that the previous player has made this mistake, their last played piece is removed.

- Movements must be introduced following this format: "`(row1,col1)(row2,col2)`"
  Here, `row1` and `col1` represent the row and column of the piece that is going to be moved, while `row2` and
  `col2` represent the row and column of the square that the piece will move into.
  There should be no spaces in the movement string.
  If the player wants to capture the piece at the position (row2,col2), they should append a third pair of
  coordinates (row3,col3), where `row3` and `col3` are the row and column of the square the piece will end at.
  This would result in an input of a string in this format: "`(row1,col1)(row2,col2)(row3,col3)`"

- After a player captures an opponent's piece, they get a chance to move if they can capture again with the
  piece that they just moved. They must then input another movement, or press <kbd>Enter</kbd> to end their turn if
  they can't make another capture movement.

- The winner is checked at the beginning of each turn. If the player whose turn just began has no pieces
  or none of their pieces can move, they lose, and the opponent is the winner. If a player ends up at a
  position where they can't move at the end of their turn, there is still a chance that the opponent's 
  next move may allow them to move once again, so a player can't lose until their turn begins again.
  This also means that a player can't win until their turn ends.