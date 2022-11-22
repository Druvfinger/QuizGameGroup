import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ServerSideGame {

    ServerSidePlayer currentPlayer;


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


