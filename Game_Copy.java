import java.util.ArrayList;

public class Game_Copy {
    private final ConcretePiece[][] boardCopy;
    private final ArrayList<ConcretePiece>[][] positionsCopy;
    private final ArrayList<Position> rememberPositionCopy;
    private final boolean attackerTurnCopy;
    private final boolean gameFinishedCopy;
    private final ArrayList<Position> killsListCopy;
    private final ArrayList<ConcretePiece> piecesCopy;
    private final ArrayList<ConcretePiece> piecesBoardCopy;

    public Game_Copy(ConcretePiece[][] boardCopy, ArrayList<ConcretePiece>[][] positionsCopy,
                     ArrayList<Position> rememberPositionCopy, boolean attackerTurnCopy,
                     boolean gameFinishedCopy, ArrayList<Position> killsListCopy,
                     ArrayList<ConcretePiece> piecesCopy, ArrayList<ConcretePiece> piecesBoardCopy) {
        this.boardCopy = boardCopy;
        this.positionsCopy = positionsCopy;
        this.rememberPositionCopy = rememberPositionCopy;
        this.attackerTurnCopy = attackerTurnCopy;
        this.gameFinishedCopy = gameFinishedCopy;
        this.killsListCopy = killsListCopy;
        this.piecesCopy = piecesCopy;
        this.piecesBoardCopy = piecesBoardCopy;
    }

    public ConcretePiece[][] getBoardCopy() {
        return boardCopy;
    }

    public ArrayList<ConcretePiece>[][] getPositionsCopy() {
        return positionsCopy;
    }

    public ArrayList<Position> getRememberPositionCopy() {
        return rememberPositionCopy;
    }

    public boolean isAttackerTurn() {
        return attackerTurnCopy;
    }

    public boolean isGameFinished() {
        return gameFinishedCopy;
    }

    public ArrayList<Position> getKillsListCopy() {
        return killsListCopy;
    }

    public ArrayList<ConcretePiece> getPiecesCopy() {
        return piecesCopy;
    }

    public ArrayList<ConcretePiece> getPiecesBoardCopy() {
        return piecesBoardCopy;
    }
}
