import javax.swing.*;
import java.util.List;

public class ServerSideGame {

    static ServerSidePlayer currentPlayer;
    static ServerSidePlayer opponentPlayer;
    GameScreen gameScreen;
    Database database = new Database();

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

    /*public Boolean isLastQuestion() {
        int numQuestions = getNumberOfQuestions();
        return gameScreen.currentQuestion == numQuestions; // true if currentQuestion == numQuestions
    }*/



    public void chooseCategory() {
        gameScreen.questionLabel.setText("What Category do you want to choose");
        for (int i = 0; i < 4; i++) {
            gameScreen.buttonList.get(i).setText(String.valueOf(database.getCategories()));
            gameScreen = new GameScreen(currentPlayer.player, null, null,null,null);// ändrat i metoden
            gameScreen.repaint();
            gameScreen.revalidate();
        }
    }


    /*public int getNumberOfRounds() {
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
    }*/


}