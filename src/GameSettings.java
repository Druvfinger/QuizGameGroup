import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GameSettings {

    Properties prop = new Properties();
    int numberOfRounds;
    int numberOfQuestions;


    public GameSettings() {

        try {
            prop.load(new FileInputStream("src/Settings.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        numberOfRounds = Integer.parseInt(prop.getProperty("numberOfRounds", "3"));
        numberOfQuestions = Integer.parseInt(prop.getProperty("numberOfQuestions", "3"));
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }
}
