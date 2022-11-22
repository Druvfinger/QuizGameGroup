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

    public boolean isLastRound() {
        return false;
    }

    public boolean isInTheLead() {
        return false;
    }

    public void chooseCategory() {

    }

    public String getRandomQuestionFromCategory() {
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
}

