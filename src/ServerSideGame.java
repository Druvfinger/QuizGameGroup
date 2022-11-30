import java.util.List;


public class ServerSideGame {

    static ServerSidePlayer currentPlayer;
    static ServerSidePlayer opponentPlayer;
//    GameScreen gameScreen;
    Database database = new Database();
    static int currentRound = 0;

    public ServerSidePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ServerSidePlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ServerSidePlayer getOpponentPlayer() {
        return opponentPlayer;
    }

    public void setOpponentPlayer(ServerSidePlayer opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }

    public String getQuestionText(String category) {
        return database.getQuestion(category);
    }

    public List<String> getAnswersText(String category) {
        return database.getAnswers(category);
    }
}