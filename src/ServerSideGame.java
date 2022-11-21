public class ServerSideGame {

    ServerSidePlayer currentPlayer;

    public boolean isWinner(){
        if (isLastRound() && isInTheLead());{
            return true;
        }
    }
    public boolean isLastRound(){
        return false;
    }
    public boolean isInTheLead(){
        return false;
    }
    public void chooseCategory(){

    }
    public String getRandomQuestionFromCategory(){
        return null;
    }
}

