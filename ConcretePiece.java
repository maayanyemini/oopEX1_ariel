import java.util.ArrayList;

public abstract class ConcretePiece implements Piece
{
    protected String type;
    protected ConcretePlayer playersOwner;
    public ArrayList<Position> moves = new ArrayList<>();
    public int kills;
    public String playersName;

    public ConcretePlayer getOwner (){
        return this.playersOwner;
    }

    public String getType(){
        return this.type;
    }

    public  ArrayList<Position> getMoves(){
        return this.moves;
    }

    public void addMoves(Position a){
        moves.add(a);
    }
    public int getMovesNun(){
        return this.moves.size();
    }
    public  abstract int distance();

    public String getPlayersName(){
        return this.playersName;
    }

    public void setPlayersName(String playersName){
        this.playersName = playersName;
    }

    public int getKills() {
        return this.kills;
    }
}