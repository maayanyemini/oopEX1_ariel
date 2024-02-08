
public class ConcretePlayer implements Player{

    private final boolean Defender;
    private int wins;
    public ConcretePlayer (boolean defender){
        this.Defender = defender;
        this.wins = 0;
    }
    public boolean isPlayerOne(){
        return !Defender;
    }

    @Override
    public int getWins(){
        return this.wins;
    }

    public void upgradeWins(){
        this.wins++;
    }

    public String getType(){
        if(Defender){
            return "defender";
        }
        return "attacker";
    }

}

