import java.util.Comparator;

public class PositionComperator implements Comparator<Position> {
    @Override
    public int compare(Position a, Position b) {

        if (a.getPiecesNum() > b.getPiecesNum()) return -1;
        else {
            if (a.getPiecesNum() < b.getPiecesNum()) {
                return 1;
            } else {
                if (a.getX() > b.getX()) {
                    return 1;
                } else {
                    if (a.getX() < b.getX()) {
                        return -1;
                    } else {
                        if (a.getY() > b.getY()) {
                            return 1;
                        } else {
                            if (a.getY() < b.getY()) {
                                return -1;
                            }
                        }
                    }
                }
            }
        }
        return 1;
    }
}