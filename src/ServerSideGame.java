import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ServerSideGame {
import javax.swing.*;

public class ServerSideGame extends Thread {

    ServerSidePlayer currentPlayer;
    GameScreen gameScreen;

    public boolean isWinner() {
        if (isLastRound() && isInTheLead()) ;
        {
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

    public void drawUpQuestion() {
        gameScreen = new GameScreen();
        for (int i = 0; i < 4; i++) {
            gameScreen.buttonList.get(i).setText(String.valueOf(getAnswers.get(i)));
            gameScreen.questionLabel.setText(getRandomQuestionFromCategory());
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

    public void newRound() { // repetera baserat på hur många frågor som inställda
       //set isAnswered to true when new game is pressed
        if (!isLastQuestion()) {
            newQuestion();
        }
        else if (isLastQuestion()){
            newQuestion();
            gameScreen.currentQuestion = 0;
            ResultsScreen resultsScreen = new ResultsScreen();
        }
    }

    public void newQuestion() {
        while (gameScreen.isAnswered) {
            drawUpQuestion();
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
}

