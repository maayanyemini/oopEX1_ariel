
public class Position {
    private int x;
    private int y;
    private int PiecesNum;
    Position (int x, int y) {
        this.x=x;
        this.y=y;
        this.PiecesNum = 0;
    }

    public int getX (){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setPiecesNum(int num){
        this.PiecesNum =num;
    }

    public int getPiecesNum() {
        return this.PiecesNum;
    }
}