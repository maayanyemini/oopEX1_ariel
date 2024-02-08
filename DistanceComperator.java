import java.util.Comparator;

public class DistanceComperator implements Comparator<ConcretePiece>{
    @Override

    public int compare(ConcretePiece piece1, ConcretePiece piece2) {

        int nameP1 = Integer.parseInt(piece1.getPlayersName().substring(1));
        int nameP2 = Integer.parseInt(piece2.getPlayersName().substring(1));

        if (piece1.distance() > piece2.distance()) {
            return -1;
        }
        else {
            if (piece1.distance() == piece2.distance()) {
                if (nameP1 > nameP2) {
                    return 1;
                }
                else {
                    if (nameP1 < nameP2) {
                        return -1;
                    }
                    else {
                        if (piece1.getOwner().isPlayerOne() != GameLogic.IsWinner)
                            return -1;
                        else
                            return 1;
                    }
                }
            }
        }
        return 1;

    }

}