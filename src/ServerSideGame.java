import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerSideGame {

    ServerSidePlayer currentPlayer;
    GameScreen gameScreen;
    Database database;

    Boolean isAnswerCorrect = false;
    Boolean isAnswered = false;
    int currentQuestion = 0;
    int currentRound = 0;
    GameSettings gameSettings = new GameSettings();

    public int getCurrentRound() {
        return currentRound;
    }


    public boolean isWinner() {
        if (isLastRound() && isInTheLead()) ;
        {
            return true;
        }
    }

    public boolean isLastRound() {
        int numRounds = gameSettings.getNumberOfRounds();
        return currentRound == numRounds;
    }

    public boolean isInTheLead() {
        return false;
    }

    public void drawUpQuestion() {
        //gameScreen = new GameScreen();
        gameScreen.questionLabel.setText(database.getQuestion());
        for (int i = 0; i < 4; i++) {
            gameScreen.buttonList.get(i).setText(String.valueOf(database.getAnswers().get(i)));
            gameScreen.repaint();
            gameScreen.revalidate();
        }
    }

    public Boolean isLastQuestion() {
        int numQuestions = gameSettings.getNumberOfQuestions();
        return currentQuestion == numQuestions; // true if currentQuestion == numQuestions
    }

    /* actionperformed
     * if !(isLastQuestion){
     * new Question();
     * }
     */

    public void newRound() { // should this be synchronized??
        gameScreen = new GameScreen();
        currentRound++;
        if (!isLastQuestion()) {
            newQuestion();
            currentQuestion++;
        } else if (isLastQuestion()) {
            newQuestion();
            currentQuestion = 0;
            currentPlayer.currentScore = 0; // sätts den om innan vi får tag på den ?
        }
    }

    public void chooseCategory() {
        gameScreen.questionLabel.setText("What Category do you want to choose");
        for (int i = 0; i < 4; i++) {
            gameScreen.buttonList.get(i).setText(String.valueOf(database.getCategories()));
            gameScreen = new GameScreen();
            gameScreen.repaint();
            gameScreen.revalidate();
        }
    }

    public ServerSidePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void newQuestion() {
        while (isAnswered) {
            drawUpQuestion();
            if (isAnswerCorrect) {
                currentPlayer.score++; // funkar detta ?
                currentPlayer.currentScore++;
            }
            isAnswered = false;
        }
    }


    public void showResults() {

    }

    public void showFinalResults() {
    }

    public void gameTest() throws IOException {
        Client welcomeScreen = new Client();
        if (!isLastRound()) {
            chooseCategory();
            newRound();
            showResults();
        } else if (isLastRound()) {
            chooseCategory();
            newRound();
            showFinalResults();
        }
    }

    public boolean howIsCurrentPlayer(ServerSidePlayer player) {
        if (currentPlayer == player) {
            return true;
        } else {
            return false;
        }
    }


}


