
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ServerSideGame {

    static ServerSidePlayer currentPlayer;
    static ServerSidePlayer opponentPlayer;
    GameScreen gameScreen;
    Database database = new Database();
    GameSettings gameSettings = new GameSettings();

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

    public boolean isWinner() {
        if (isLastRound() && isInTheLead()) ;
        {
            return true;
        }
    }

    public boolean isLastRound() {
        int numRounds = gameSettings.getNumberOfRounds();
        return gameScreen.currentRound == numRounds;
    }

    public boolean isInTheLead() {
        return false;
    }

    public String getQuestionText(String category) {
        return database.getQuestion(category);
    }

    public List<String> getAnswersText(String category) {
        return database.getAnswers(category);
    }

    // Delat upp metoden i två separata delar
    public void drawUpQuestion(JLabel questionLabel, List<JButton> buttonList) {
        questionLabel.setText(database.getQuestion(null));                  // OBS!!!! Null parameter!!!
        List<String> answers = database.getAnswers(null);                   // OBS!!!! Null parameter!!!
        for (int i = 0; i < answers.size(); i++) {
            buttonList.get(i).setText(String.valueOf(answers.get(i)));
            /*gameScreen.repaint();
            gameScreen.revalidate();*/
        }
    }

    public Boolean isLastQuestion() {
        int numQuestions = gameSettings.getNumberOfQuestions();
        return gameScreen.currentQuestion == numQuestions; // true if currentQuestion == numQuestions
    }

    /* actionperformed
     * if !(isLastQuestion){
     * new Question();
     * }
     */

    public void newRound() {
        gameScreen.currentRound++;
        if (!isLastQuestion()) {
            newQuestion();
            gameScreen.currentQuestion++;
        } else if (isLastQuestion()) {
            newQuestion();
            gameScreen.currentQuestion = 0;
            currentPlayer.currentScore = 0;
        }
    }

    public void chooseCategory() {
        gameScreen.questionLabel.setText("What Category do you want to choose");
        for (int i = 0; i < 4; i++) {
            gameScreen.buttonList.get(i).setText(String.valueOf(database.getCategories()));
            gameScreen = new GameScreen(currentPlayer.player, null, null,null);// ändrat i metoden
            gameScreen.repaint();
            gameScreen.revalidate();
        }
    }


    public void newQuestion() {
        while (gameScreen.isAnswered) {
            //drawUpQuestion();
            if (gameScreen.isAnswerCorrect) {
                currentPlayer.score++; // funkar detta ?
                currentPlayer.currentScore++;
            }
            gameScreen.isAnswered = false;
        }
    }


    public void showResults() {

    }

    public void showFinalResults() {
    }

    public void gameTest() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(null); // ändrade i metoden
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
}