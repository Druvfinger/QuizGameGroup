import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameSettings {

    Properties prop = new Properties();
    int numberOfRounds;
    int numberOfQuestions;


    protected GameSettings() {

        try {
            prop.load(new FileInputStream("src/Settings.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        numberOfRounds = Integer.parseInt(prop.getProperty("numberOfRounds", "3"));
        numberOfQuestions = Integer.parseInt(prop.getProperty("numberOfQuestions", "3"));

        if (numberOfRounds < 3 || numberOfRounds > 4) {
            numberOfRounds = 3;
            System.out.println("Antal rundor utanför intervall. SET TO DEFAULT VALUE 3.");
        }

        if (numberOfQuestions < 2 || numberOfQuestions > 5) {
            numberOfQuestions = 3;
            System.out.println("Antal frågor per runda utanför intervall. SET TO DEFAULT VALUE 3.");
        }
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }
}
