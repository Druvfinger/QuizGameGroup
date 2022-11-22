import com.sun.source.tree.UsesTree;

import javax.management.StringValueExp;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

public class ServerSideGame {

    ServerSidePlayer currentPlayer;
    GameScreen gameScreen;
    Database database;


    public boolean isWinner() {
        if (isLastRound() && isInTheLead()) ;
        {
            return true;
        }
    }

    public boolean isLastRound() {
        int numRounds = getNumberOfRounds();
        return gameScreen.currentRound == numRounds;
    }

    public boolean isInTheLead() {
        return false;
    }

    public void drawUpQuestion() {
        gameScreen = new GameScreen();
        for (int i = 0; i < 4; i++) {
            gameScreen.buttonList.get(i).setText(String.valueOf(database.getAnswers().get(i)));
            gameScreen.questionLabel.setText(database.getQuestion());
            gameScreen.repaint();
            gameScreen.revalidate();
        }
    }

    public Boolean isLastQuestion() {
        int numQuestions = getNumberOfQuestions();
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
            gameScreen = new GameScreen();
            gameScreen.repaint();
            gameScreen.revalidate();
        }
    }

    public ServerSidePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void newQuestion() {
        while (gameScreen.isAnswered) {
            drawUpQuestion();
            if (gameScreen.isAnswerCorrect){
                currentPlayer.score++; // funkar detta ?
                currentPlayer.currentScore++;
            }
            gameScreen.isAnswered = false;
        }
    }

    public int getNumberOfRounds() {
        try (InputStream input = new FileInputStream("src/Settings.properties")) {
            Properties prop = new Properties();
            return Integer.parseInt(prop.getProperty("numberOfRounds", "3"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNumberOfQuestions() {
        try (InputStream input = new FileInputStream("src/Settings.properties")) {
            Properties prop = new Properties();
            return Integer.parseInt(prop.getProperty("numberOfQuestions", "3"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showResults(){

    }
    public void showFinalResults(){}

    public void gameTest(){
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        if (!isLastRound()){
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

