import java.util.Comparator;

public class MovesComperator implements Comparator<ConcretePiece> {
    @Override

    public int compare(ConcretePiece piece1, ConcretePiece piece2) {

        int nameP1 = Integer.parseInt(piece1.getPlayersName().substring(1));
        int nameP2 = Integer.parseInt(piece2.getPlayersName().substring(1));

        if ((piece1.getOwner().isPlayerOne() && !piece2.getOwner().isPlayerOne()) ||
                (!piece1.getOwner().isPlayerOne() && piece2.getOwner().isPlayerOne())){
            if (piece1.getOwner().isPlayerOne() == GameLogic.IsWinner)
                return 1;
            else{
                return -1;
            }
        }else {
            if (piece1.getMovesNun() > piece2.getMovesNun())
                return 1;
            else {
                if (piece1.getMovesNun() < piece2.getMovesNun()) {
                    return -1;
                } else {
                    if (nameP1 > nameP2) { // if equal going to the second term
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }
    }
}