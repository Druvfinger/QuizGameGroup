
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ServerSideGame {

    ServerSidePlayer currentPlayer;
    GameScreen gameScreen;
    Database database = new Database();

    boolean isCategoryChosen = false;
    public boolean hasWinner() {
        if (isLastRound() && isLastQuestion() && currentPlayer.score > currentPlayer.getOpponent().score
                || currentPlayer.score > currentPlayer.getOpponent().score){
            return true;
        }
        return false;
    }
    public boolean isTie(){
        if (isLastRound() && isLastQuestion() && currentPlayer.score == currentPlayer.getOpponent().score){
            return true;
        }
        return false;
    }

    public boolean isLastRound() {
        int numRounds = getNumberOfRounds();
        return gameScreen.currentRound == numRounds;
    }

    public void drawUpQuestion(GameScreen gameScreen, Database database) {
        gameScreen.questionLabel.setText(database.getQuestion());
        List<String> answers = database.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            gameScreen.buttonList.get(i).setText(String.valueOf(answers.get(i)));
        }
        gameScreen.revalidate();
    }


    public Boolean isLastQuestion() {
        int numQuestions = getNumberOfQuestions();
        return gameScreen.currentQuestion == numQuestions; // true if currentQuestion == numQuestions
    }

    public void newRound() {
        ChooseCategoryScreen categoryScreen = new ChooseCategoryScreen();
        if (isCategoryChosen){
            gameScreen = new GameScreen();
            if (!isLastQuestion()) {
                newQuestion(gameScreen);
                gameScreen.currentQuestion++;
            } else if (isLastQuestion()) {
                newQuestion(gameScreen);
                gameScreen.currentQuestion = 0;
                currentPlayer.currentScore = 0;
            }
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

    public void newQuestion(GameScreen gameScreen) {
        while (gameScreen.isAnswered) {
            drawUpQuestion(gameScreen,database);
            if (gameScreen.isAnswerCorrect){
                currentPlayer.score++; // funkar detta ?
                currentPlayer.currentScore++;
            }
            gameScreen.isAnswered = false;
            gameScreen.repaint();
            gameScreen.revalidate();
        }
    }
    public synchronized boolean hasPlayedRound(ServerSidePlayer player){
        if (player == currentPlayer){
            newRound();
            currentPlayer = currentPlayer.getOpponent();
            currentPlayer.otherPlayerMoved(currentPlayer.score);
            return true;
        }
        return false;
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