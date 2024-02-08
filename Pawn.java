import java.util.ArrayList;

public class Pawn extends ConcretePiece {

    // public String name;
    public Pawn(ConcretePlayer owner) {
        this.playersOwner = owner;
        this.kills = 0;
        if (owner.isPlayerOne()) {
            this.type = "♟";
        } else {
            this.type = "♙";
        }
        this.moves = new ArrayList<>();
    }
    public void kill() {
        kills++;
    }

    public String getType() {
        if (!this.playersOwner.isPlayerOne())
            return  "♟";
        return "♙";
    }
    public String colorType(){
        if (!this.playersOwner.isPlayerOne())
            return "red";
        return "blue";
    }
    public int distance() {
        int dist = 0;
        for (int i = 0; i < this.moves.size() - 1; i++) {
            Position current = this.moves.get(i);
            Position next = this.moves.get(i + 1);

            if (current.getX() == next.getX()) {
                dist += Math.abs(current.getY() - next.getY());
            } else if (current.getY() == next.getY()) {
                dist += Math.abs(current.getX() - next.getX());
            }
        }
        return dist;
    }


}