import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    public static final int BOARD_SIZE = 11;
    private ConcretePiece[][] board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<ConcretePiece>[][] BoardsPositions = new ArrayList[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<Position> RememberPosition = new ArrayList<>(); //which soldier stepped on position
    private final Position [][] stepedOnPosition = new Position[BOARD_SIZE][BOARD_SIZE]; // an array who will eventually remember which soldiers stepped on a specific square
    private final ConcretePlayer Attacker;
    private final ConcretePlayer Defender;
    private boolean AttackerTurn;
    public static boolean IsWinner;
    private boolean gameFinished;
    private ArrayList<Position> KillsList = new ArrayList<>(); //the pawn got killed
    private ArrayList<ConcretePiece> pieces = new ArrayList<>();
    private ArrayList<ConcretePiece> piecesBoard = new ArrayList<>();

    private final Stack<Game_Copy> undoStack;

    GameLogic() {
        this.Defender = new ConcretePlayer(true); // player 1
        this.Attacker = new ConcretePlayer(false); //player 2 in the settings (but he starts first)
        this.undoStack = new Stack<>();
        reset();

    }

    public boolean move(Position a, Position b) {
        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();
        String Aname = board[aX][aY].getPlayersName();

        boolean AttackerrTurn = isSecondPlayerTurn();
        boolean Isdefender = getPieceAtPosition(a).getOwner().isPlayerOne();
        if ((AttackerrTurn && Isdefender) || (!AttackerrTurn && !Isdefender)) // if collide with turns
            return false;
        if (!canMove(a, b))
            return false;
        saveGameState(); // saving the current state so we can use it to do undo method later
        boolean contains = false;
        for (int i = 0; i < BoardsPositions[b.getX()][b.getY()].size(); i++) {
            if (BoardsPositions[bX][bY].get(i).getPlayersName().equals(Aname))
            {
                contains = true;
                break;
            }
        }
        if (!contains) {
            BoardsPositions[bX][bY].add(board[aX][aY]);
        }

        board[aX][aY].addMoves(b); // remembering the moves
//checking all the possible kills
        if (!(board[aX][aY] instanceof King)){
            if (bX != 0) {
                if (bX- 1 == 0 || (board[bX - 2][bY] != null && (board[bX - 2][bY] instanceof Pawn) &&
                        board[bX - 2][bY].getOwner().isPlayerOne() == board[aX][aY].getOwner().isPlayerOne()) || isPawnAtCorner(new Position(bX-2, bY))) {
                    if (board[bX-1][bY] instanceof Pawn && board[bX-1][bY].getOwner().isPlayerOne() != board[aX][aY].getOwner().isPlayerOne()) {
                        KillsList.add(new Position(bX-1,bY));
                        ((Pawn) board[aX][aY]).kill();
                        for (int i = 0; i < piecesBoard.size(); i++)
                            if (board[bX-1][bY] == piecesBoard.get(i)) {
                                piecesBoard.remove(i);
                            }
                        board[bX-1][bY] = null; //killing the piece
                    }
                }
            }
            if (bX+1 == 10 || (bX+2 < BOARD_SIZE && board[bX+2][bY] != null && board[bX+2][bY] instanceof Pawn &&
                    board[bX+2][bY].getOwner().isPlayerOne() == board[aX][aY].getOwner().isPlayerOne()) || isPawnAtCorner(new Position(bX+2,bY)))
                if (board[bX+1][bY] instanceof Pawn && board[bX+1][bY].getOwner().isPlayerOne() != board[aX][aY].getOwner().isPlayerOne()) {
                    KillsList.add(new Position(bX+1,bY));
                    ((Pawn) board[aX][aY]).kill();
                    for (int i = 0; i < piecesBoard.size(); i++) {
                        if (board[bX+1][bY] == piecesBoard.get(i))
                            piecesBoard.remove(i);
                    }
                    board[bX+1][bY] = null;
                }

            if (bY != 0) {
                if (bY-1 == 0 || (board[bX][bY-2] != null && board[bX][bY-2] instanceof Pawn &&
                        board[bX][bY-2].getOwner().isPlayerOne() == board[aX][aY].getOwner().isPlayerOne()) || isPawnAtCorner(new Position(bX, bY - 2))) {
                    if (board[bX][bY-1] instanceof Pawn && board[bX][bY-1].getOwner().isPlayerOne() != board[aX][aY].getOwner().isPlayerOne()) {
                        KillsList.add(new Position(bX,bY-1));
                        ((Pawn) board[aX][aY]).kill();
                        for (int i = 0; i < piecesBoard.size(); i++) {
                            if (board[bX][bY-1] == piecesBoard.get(i))
                                piecesBoard.remove(i);
                        }
                        board[bX][bY-1] = null;
                    }
                }
            }
            if (bY+1 == 10 || (bY+2< BOARD_SIZE && board[bX][bY+2] != null && board[bX][bY+2] instanceof Pawn &&
                    board[bX][bY+2].getOwner().isPlayerOne() == board[aX][aY].getOwner().isPlayerOne()) || isPawnAtCorner(new Position(bX,bY+2))) {
                if (board[bX][bY+1] instanceof Pawn && board[bX][bY+1].getOwner().isPlayerOne() != board[aX][aY].getOwner().isPlayerOne()) {
                    KillsList.add(new Position(bX,bY+1));
                    ((Pawn) board[aX][aY]).kill();
                    for (int i = 0; i < piecesBoard.size(); i++) {
                        if (board[bX][bY+1] == piecesBoard.get(i))
                            piecesBoard.remove(i);
                    }
                    board[bX][bY+1] = null;
                }
            }
        }
        ConcretePiece MovingPiece = board[aX][aY];
        board[bX][bY] = MovingPiece;
        board[aX][aY] = null;
        this.AttackerTurn = !this.AttackerTurn; //shift turns

        ConcretePlayer gameFinished = WhoWon(b); //who won
        if (this.gameFinished) {
            if (gameFinished.getType().equals("defender")) {
                this.Defender.upgradeWins();
                IsWinner = false;
                printComperatorResults();
            } else {
                if (gameFinished.getType().equals("attacker")) {
                    this.Attacker.upgradeWins();
                    IsWinner = true;
                }
                printComperatorResults();
            }

            reset();
        }
        return true;
    }

    public ConcretePiece getPieceAtPosition(Position position) {
        return this.board[position.getX()][position.getY()];
    }

    public ConcretePlayer getFirstPlayer() {
        return this.Defender;
    }

    public ConcretePlayer getSecondPlayer() { // the second player is player2, who is starting the game
        return this.Attacker;
    }

    public Boolean gameFinished() {
        return this.gameFinished;
    }
    public boolean isSecondPlayerTurn() {
        return this.AttackerTurn;
    }
    public void reset() {
        //intilize all positions on board to null
        this.AttackerTurn = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = null;
            }
        }
        ConcretePlayer defender = new ConcretePlayer(true);
        //creates attackers and defenders on the board
        for (int i = 3; i <= 7; i++) {
            board[0][i] = new Pawn(defender);
            board[i][0] = new Pawn(defender);
            board[10][i] = new Pawn(defender);
            board[i][10] = new Pawn(defender);
        }
        board[1][5] = new Pawn(defender);
        board[5][1] = new Pawn(defender);
        board[9][5] = new Pawn(defender);
        board[5][9] = new Pawn(defender);

        ConcretePlayer attacker = new ConcretePlayer(false);
        for (int i = 4; i <= 6; i++) {
            board[4][i] = new Pawn(attacker);
            board[6][i] = new Pawn(attacker);
        }
        board[3][5] = new Pawn(attacker);
        board[7][5] = new Pawn(attacker);
        board[5][3] = new Pawn(attacker);
        board[5][4] = new Pawn(attacker);
        board[5][6] = new Pawn(attacker);
        board[5][7] = new Pawn(attacker);
        board[5][5] = new King(attacker);

        // will help in the comparators
        for (int i = 0; i < BoardsPositions.length; i++)
            for (int j = 0; j < BoardsPositions[i].length; j++) {
                BoardsPositions[i][j] = new ArrayList<>();
            }
        for (int i = 0; i < stepedOnPosition.length; i++) {
            for (int j = 0; j < stepedOnPosition[i].length; j++) {
                stepedOnPosition[i][j] = new Position(i, j);
            }
        }
//creates players on board with a name and a number for identification
        int counterD = 1, counterA = 1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null) {
                    BoardsPositions[i][j].add(board[i][j]);
                    pieces.add(board[i][j]);
                    piecesBoard.add(board[i][j]);
                    Position p = new Position(i, j);
                    board[i][j].addMoves(p);

                    if (board[j][i] instanceof Pawn) {
                        if (board[j][i].getOwner().isPlayerOne()) {
                            board[j][i].setPlayersName("D" + counterD);
                            counterD++;
                        } else {
                            if (!board[j][i].getOwner().isPlayerOne()) {
                                board[j][i].setPlayersName("A" + counterA);
                                counterA++;
                            }
                        }
                    }
                    else {
                        board[j][i].setPlayersName("K" + counterD);
                        counterD++;
                    }
                }
            }
        }
    }
    //this will help us in the undo
    private void restoreGameState(Game_Copy snapshot) {
        this.board = snapshot.getBoardCopy();
        this.BoardsPositions = snapshot.getPositionsCopy();
        this.RememberPosition = new ArrayList<>(snapshot.getRememberPositionCopy());
        this.AttackerTurn = snapshot.isAttackerTurn();
        this.gameFinished = snapshot.isGameFinished();
        this.KillsList = new ArrayList<>(snapshot.getKillsListCopy());
        this.pieces = new ArrayList<>(snapshot.getPiecesCopy());
        this.piecesBoard = new ArrayList<>(snapshot.getPiecesBoardCopy());

    }
    private ConcretePiece[][] copyBoard(ConcretePiece[][] original) { //creating a copy of the current pieces
        ConcretePiece[][] copy = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, BOARD_SIZE);
        }
        return copy;
    }

    private ArrayList<ConcretePiece>[][] copyPositions(ArrayList<ConcretePiece>[][] original) { //creates a copy of the current positions
        ArrayList[][] copy = new ArrayList[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                copy[i][j] = new ArrayList<>(original[i][j]);
            }
        }
        return copy;
    }

    // this creates a "snapshot" of the boards game, so when we use it in move method-before the move we save the last move so we can Restore it
    private void saveGameState() {
        ConcretePiece[][] boardCopy = copyBoard(this.board);
        ArrayList<ConcretePiece>[][] positionsCopy = copyPositions(this.BoardsPositions);
        ArrayList<Position> rememberPositionCopy = new ArrayList<>(this.RememberPosition);
        boolean attackerTurnCopy = this.AttackerTurn;
        boolean gameFinishedCopy = this.gameFinished;
        ArrayList<Position> killsListCopy = new ArrayList<>(this.KillsList);
        ArrayList<ConcretePiece> piecesCopy = new ArrayList<>(this.pieces);
        ArrayList<ConcretePiece> piecesBoardCopy = new ArrayList<>(this.piecesBoard);

        Game_Copy snapshot = new Game_Copy(boardCopy, positionsCopy, rememberPositionCopy,
                attackerTurnCopy, gameFinishedCopy, killsListCopy, piecesCopy, piecesBoardCopy);

        undoStack.push(snapshot);
    }
    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Game_Copy previousState = undoStack.pop();
            restoreGameState(previousState);
        }
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public boolean isGameFinished() {
        return gameFinished();
    }

    private boolean isEmptyAtPosition(Position pos) {
        return getPieceAtPosition(pos) == null;
    }

    private Position kingsPosition(ConcretePiece[][] whereIsKing) { // checks where is the king
        for (int i = 0; i < whereIsKing.length; i++)
            for (int j = 0; j < whereIsKing.length; j++)
                if (whereIsKing[i][j] != null && whereIsKing[i][j] instanceof King) {
                    Position p = new Position(i, j);
                    return p;
                }
        return null;
    }


    private boolean canMove(Position start, Position end) {
        if (isEmptyAtPosition(start) ||
                !isEmptyAtPosition(end) ||
                ((start.getX() != end.getX()) && (start.getY() != end.getY())) || //not at the same line-not possible
                ((start.getX() == end.getX()) && (start.getY() == end.getY())) || // the same spot
                ((board[start.getX()][start.getY()] == null) || //there's  no piece to move
                        (board[end.getX()][end.getY()] != null))){ // the place is already taken

            return false;}
        if (board[start.getX()][start.getY()] instanceof Pawn && isPawnAtCorner(end)) //only a king can step on corners
            return false;

        if(!isPathClearInRow(start,end) || !isPathClearInCol(start,end)) { //checks if there are pieces on my way
            return false;
        }
        return true;
    }
    private boolean isPathClearInRow(Position start, Position end) { // checks if the row I want to go in is clear from pawns
        if (start.getX() == end.getX()) {
            int step = (start.getY() < end.getY()) ? 1 : -1;
            for (int i = start.getY() + step; i != end.getY(); i += step) {
                if (board[start.getX()][i] != null) return false;
            }
        }
        return true;
    }
    private boolean isPathClearInCol(Position start, Position end) { // checks if the column I want to go in is clear from pawns
        if (start.getY() == end.getY()) {
            int step = (start.getX() < end.getX()) ? 1 : -1;
            for (int i = start.getX() + step; i != end.getX(); i += step) {
                if (board[i][start.getY()] != null) return false;
            }
        }
        return true;
    }
    public boolean isPawnAtCorner(Position end) { // if it is in corner
        return (end.getX() == 0 && end.getY() == 0)
                || (end.getX() == 0 && end.getY() == 10) ||
                (end.getX() == 10 && end.getY() == 0) || (end.getX() == 10 && end.getY() == 10);
    }

    private ConcretePlayer WhoWon(Position b) { //defines the winner
        Position KING = kingsPosition(board); // finds where is the king
        ConcretePlayer Winner = null;
        if (allAttackerPiecesEaten() || isPawnAtCorner(KING)) { // if defenders ate all attackers or the king got to a corner spot
            Winner = this.Defender;
            this.gameFinished = true;
        } else {
            Position leftKing = new Position(KING.getX() - 1, KING.getY());
            Position rightKing = new Position(KING.getX() + 1, KING.getY());
            Position downKing = new Position(KING.getX(), KING.getY() + 1);
            Position upKing = new Position(KING.getX(), KING.getY() - 1);
            if (KING.getX() == 0) {
                if (KING.getY() == 1) {
                    if ((Objects.equals(pawnsOwner(rightKing), "red")) && //"red" symbols attacker
                            (Objects.equals(pawnsOwner(downKing), "red"))) {
                        Winner = this.Attacker;
                        this.gameFinished = true;
                    }
                }
                else {
                    if (KING.getY() == 9) {
                        if (Objects.equals(pawnsOwner(rightKing), "red") &&
                                Objects.equals(pawnsOwner(upKing), "red")) {
                            Winner = this.Attacker;
                            this.gameFinished = true;
                        }
                    } else {
                        if (Objects.equals(pawnsOwner(rightKing), "red") &&
                                Objects.equals(pawnsOwner(downKing), "red") && Objects.equals(pawnsOwner(upKing), "black")) {
                            Winner = this.Attacker;
                            this.gameFinished = true;
                        }
                    }
                }
            }
            else {
                if (KING.getX() == 10) {
                    if (KING.getY() == 1) {
                        if (Objects.equals(pawnsOwner(leftKing), "red") &&
                                Objects.equals(pawnsOwner(downKing), "red")) {
                            Winner = this.Attacker;
                            this.gameFinished = true;
                        }
                    }
                    else {
                        if (KING.getY() == 9) {
                            if (Objects.equals(pawnsOwner(leftKing), "red") &&
                                    Objects.equals(pawnsOwner(upKing), "red")) {
                                Winner = this.Attacker;
                                this.gameFinished = true;
                            }
                        }
                        else {
                            if (Objects.equals(pawnsOwner(leftKing), "red") &&
                                    Objects.equals(pawnsOwner(downKing), "red") &&
                                    Objects.equals(pawnsOwner(upKing), "red")) {
                                Winner = this.Attacker;
                                this.gameFinished = true;
                            }
                        }
                    }
                }
                else {
                    if (KING.getY() == 0) {
                        if (KING.getX() == 1) {
                            if (Objects.equals(pawnsOwner(rightKing), "red") &&
                                    Objects.equals(pawnsOwner(downKing), "red")) {
                                Winner = this.Attacker;
                                this.gameFinished = true;
                            }
                        }
                        else {
                            if (KING.getX() == 9) {
                                if (Objects.equals(pawnsOwner(leftKing), "red") &&
                                        Objects.equals(pawnsOwner(downKing), "red")) {
                                    Winner = this.Attacker;
                                    this.gameFinished = true;
                                }
                            }
                            else {
                                if (Objects.equals(pawnsOwner(rightKing), "red") &&
                                        Objects.equals(pawnsOwner(leftKing), "red") &&
                                        Objects.equals(pawnsOwner(downKing), "red")) {
                                    Winner = this.Attacker;
                                    this.gameFinished = true;
                                }
                            }
                        }
                    }
                    else {
                        if (KING.getY() == 10) {
                            if (KING.getX() == 1) {
                                if (Objects.equals(pawnsOwner(rightKing), "red") &&
                                        Objects.equals(pawnsOwner(upKing), "red")) {
                                    Winner = this.Attacker;
                                    this.gameFinished = true;
                                }
                            }
                            else {
                                if (KING.getX() == 9) {
                                    if (Objects.equals(pawnsOwner(leftKing), "red") &&
                                            Objects.equals(pawnsOwner(upKing), "red")) {
                                        Winner = this.Attacker;
                                        this.gameFinished = true;
                                    }
                                }
                                else {
                                    if (Objects.equals(pawnsOwner(rightKing), "red") &&
                                            Objects.equals(pawnsOwner(leftKing), "red") &&
                                            Objects.equals(pawnsOwner(upKing), "red")) {
                                        Winner = this.Attacker;
                                        this.gameFinished = true;
                                    }
                                }
                            }
                        }
                        else {
                            if (Objects.equals(pawnsOwner(leftKing), "red") &&
                                    Objects.equals(pawnsOwner(rightKing), "red") &&
                                    Objects.equals(pawnsOwner(upKing), "red") &&
                                    Objects.equals(pawnsOwner(downKing), "red")) {
                                Winner = this.Attacker;
                                this.gameFinished = true;
                            } else {
                                this.gameFinished = false;
                            }
                        }

                    }
                }
            }
        }
        return Winner;
    }

    private boolean allAttackerPiecesEaten() { // checking if all defenders were eaten-then defenders won
        for (ConcretePiece concretePiece : piecesBoard) {
            if (!concretePiece.getOwner().isPlayerOne()) {
                return false;
            }
        }
        return true;
    }
    private String pawnsOwner(Position p) {
        if (board[p.getX()][p.getY()] instanceof Pawn) {
            return ((Pawn) board[p.getX()][p.getY()]).colorType();
        }
        return null;
    }

    private void printComperatorResults() { //Printing of all the comparators we used
        DistanceComperator distanceComperator = new DistanceComperator();
        KillsComperator killsComperator = new KillsComperator();
        MovesComperator movesComperator = new MovesComperator();
        PositionComperator positionComperator = new PositionComperator();

        pieces.sort(movesComperator);
        for (ConcretePiece piece : pieces)
            if (piece.getMovesNun() > 1) {
                System.out.print(piece.getPlayersName() + ": [");
                for (int j = 0; j < piece.getMoves().size(); j++) {
                    System.out.print("(" + piece.getMoves().get(j).getX() + ", " + piece.getMoves().get(j).getY() + ")");
                    if (piece.getMoves().size() - j > 1)
                        System.out.print(", ");
                }
                System.out.print("]");
                System.out.println();
            }
        System.out.println("***************************************************************************");

        pieces.sort(killsComperator);
        for (ConcretePiece piece : pieces) {
            if (piece.getKills() > 0) {
                System.out.print(piece.getPlayersName() + ": ");
                System.out.print(piece.getKills() + " kills");
                System.out.println();
            }
        }
        System.out.println("***************************************************************************");

        pieces.sort(distanceComperator);
        for (ConcretePiece piece : pieces) {
            if (piece.getMovesNun() > 1) {
                System.out.print(piece.getPlayersName() + ": ");
                System.out.print(piece.distance() + " squares");
                System.out.println();
            }
        }

        System.out.println("***************************************************************************");

        for (int i = 0; i < stepedOnPosition.length; i++) {
            for (int j = 0; j < stepedOnPosition.length; j++) {
                int size = BoardsPositions[i][j].size();
                stepedOnPosition[i][j].setPiecesNum(size);
                RememberPosition.add(stepedOnPosition[i][j]);
            }
        }

        RememberPosition.sort(positionComperator);
        for (Position value : RememberPosition) {
            if (value.getPiecesNum() > 1) {
                System.out.print("(" + value.getX() + ", " + value.getY() + ")");
                System.out.print(value.getPiecesNum() + " pieces");
                System.out.println();
            }
        }

        System.out.println("***************************************************************************");
    }
}