import java.util.ArrayList;
public class King extends ConcretePiece {
    public King(ConcretePlayer p) {
        this.playersOwner = p;
        this.type = "♕";
        this.moves = new ArrayList<>();
        this.kills =0;
    }

    public String getType() {
        return "♕";
    }

    public ConcretePlayer getOwner() {
        return playersOwner;
    }

    @Override
    public int getKills() {
        return this.kills;
    }

    public int distance() {
        int dist = 0;
        for (int i = 0; i < this.moves.size() - 1; i++) {
            Position current = this.moves.get(i);
            var next = this.moves.get(i + 1);

            if (current.getX() == next.getX()) dist += Math.abs(current.getY() - next.getY());
            else if (current.getY() == next.getY()) {
                dist += Math.abs(current.getX() - next.getX());
            }
        }
        return dist;
    }
}
